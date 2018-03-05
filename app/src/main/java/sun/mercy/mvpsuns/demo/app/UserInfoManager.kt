@file:Suppress("unused")

package sun.mercy.mvpsuns.demo.app


import android.content.Context
import android.net.Uri

import com.mercy.suns.utils.DataHelper
import io.objectbox.Box
import io.objectbox.BoxStore

import io.objectbox.kotlin.boxFor

import io.reactivex.Observable

import io.rong.imlib.model.UserInfo

import sun.mercy.mvpsuns.demo.app.utils.CharacterParser

import sun.mercy.mvpsuns.demo.app.utils.RongGenerate
import sun.mercy.mvpsuns.demo.app.utils.convert

import sun.mercy.mvpsuns.demo.mvp.model.api.service.AccountService
import sun.mercy.mvpsuns.demo.mvp.model.db.entity.*
import sun.mercy.mvpsuns.demo.mvp.model.resp.*
import java.util.*
import javax.inject.Singleton
import kotlin.collections.ArrayList


/**
 * ================================================
 * UserInfoManager
 * Created by sun on 2018/2/11
 * ================================================
 */
@Singleton
class UserInfoManager constructor(private val context: Context) {


    companion object {
        /**
         * 用户信息全部未同步
         */
        private const val NONE = 0//00000
        /**
         * 好友信息同步成功
         */
        private const val FRIEND = 1//00001
        /**
         * 群组信息同步成功
         */
        private const val GROUPS = 2//00010
        /**
         * 群成员信息部分同步成功,n个群组n次访问网络,存在部分同步的情况
         */
        private const val PARTGROUPMEMBERS = 4//00100
        /**
         * 群成员信息同步成功
         */
        private const val GROUPMEMBERS = 8//01000
        /**
         * 黑名单信息同步成功
         */
        private const val BLACKLIST = 16//10000
        /**
         * 用户信息全部同步成功
         */
        private const val ALL = 27//11011
    }

    private var mGetAllUserInfoState = 0

    private val mRepositoryManager = (context.applicationContext as MainApp).getAppComponent().repositoryManager()

    private var mBoxStore: BoxStore? = (context.applicationContext as MainApp).getAppComponent().boxStore()

    private var mAccountService: AccountService
    private var mFriendBox: Box<Friend>? = null
    private var mGroupsBox: Box<Groups>? = null
    private var mGroupMemberBox: Box<GroupMember>? = null
    private var mBlackLisBox: Box<BlackList>? = null
    private var mUserInfoCache: LinkedHashMap<String, UserInfo>? = null
    private var mGroupsList: MutableList<Groups>? = null//同步群组成员信息时需要这个数据

    init {
        mAccountService = mRepositoryManager.obtainRetrofitService(AccountService::class.java)
    }

    fun openDB() {
        mBoxStore?.apply {
            mFriendBox = boxFor()
            mGroupsBox = boxFor()
            mGroupMemberBox = boxFor()
            mBlackLisBox = boxFor()
        }
        mUserInfoCache = LinkedHashMap()
        mGetAllUserInfoState = DataHelper.getIntergerSF(context, Const.KEY_SP_GET_ALL_USERINFO_STATE)
    }

    fun closeDB() {
        mBoxStore?.apply {
            close()
            mBoxStore = null
            mFriendBox = null
            mGroupsBox = null
            mGroupMemberBox = null
            mBlackLisBox = null
        }
        mUserInfoCache?.apply {
            clear()
            mUserInfoCache = null
        }
        mGroupsList = null
//        UserInfoEngine.getInstance(mContext).setListener(null)
//        GroupInfoEngine.getInstance(mContext).setmListener(null)
    }


    fun getAllUserInfo() {
        if (hasGetAllUserInfo()) return
//        doingGetAllUserInfo = true
        //在获取用户信息时无论哪一个步骤出错,都不继续往下执行,因为网络出错,很可能再次的网络访问还是有问题
        if (!hasGetFriends()) {
            fetchFriends().doOnError { setGetAllUserInfoDone() }
        }
        if (!hasGetGroups()) {
            fetchGroups()
                    .filter { !hasGetAllGroupMembers() }
                    .flatMap { Observable.fromIterable(it) }
                    .flatMap { fetchGroupMembers(it.group.id) }
                    .doOnError { setGetAllUserInfoDone() }
        }
        if (!hasGetAllGroupMembers()) {
            deleteGroupMembers()
            Observable.fromIterable(getGroups())
                    .flatMap { fetchGroupMembers(it.groupsId) }
                    .doOnError { setGetAllUserInfoDone() }

        }
        if (!hasGetBlackList()) {
            fetchBlackList()
        }
        setGetAllUserInfoDone()
    }

    fun getGroups(): MutableList<Groups> {
        return mGroupsBox?.query()?.build()?.find()?:ArrayList()
    }


    fun fetchFriends(): Observable<List<FriendResp>> {
        return mAccountService
                .getAllFriends()
                .convert()
                .flatMap {
                    if (it.isNotEmpty()) {
                        deleteFriends()
                        insertFriends(it)
                    }
                    mGetAllUserInfoState = mGetAllUserInfoState or FRIEND
                    Observable.just(it)
                }
    }

    fun fetchGroups(): Observable<List<GroupsResp>> {
        return mAccountService.getGroups().convert()
                .filter { it.isNotEmpty() }
                .flatMap {
                    val groups = it.map {
                        Groups(groupsId = it.group.id,
                                name = it.group.name,
                                portraitUri = it.group.portraitUri,
                                role = it.role.toString())
                    }
                    deleteGrouops()
                    insertGroups(groups)
                    Observable.just(it)
                }
    }

    fun fetchGroupMembers(groupId: String): Observable<List<GroupMemberResp>> {
        return mAccountService.getGroupMembers(groupId).convert()
                .filter { it.isNotEmpty() && mBoxStore != null && mGroupMemberBox != null }
                .flatMap {
                    deleteGroupMembers()
                    insertGroupMembers(it, groupId)
                    Observable.just(it)
                }
    }

    fun fetchBlackList(): Observable<List<BlackListResp>> {
        return mAccountService.getBlackList().convert()
    }

    fun insertFriends(friends: List<FriendResp>) {
        friends.filter { it.status == 20 }
                .map {
                    Friend(userId = it.user.id,
                            name = it.user.nickname,
                            portraitUri = it.user.portraitUri,
                            displayName = it.displayName,
                            nameSpelling = CharacterParser.getSpelling(it.user.nickname),
                            displayNameSpelling = CharacterParser.getSpelling(it.displayName))
                }
                .map {
                    if (it.portraitUri.isEmpty()) {
                        it.portraitUri = getPortrait(it) ?: ""
                    }
                    it
                }
                .takeIf { it.isNotEmpty() }
                ?.let {
                    mFriendBox?.put(it)
                }
    }

    fun insertGroupMembers(list: List<GroupMemberResp>, groupId: String) {

        val groupsMembersList = setCreatedToTop(list, groupId)
        if (groupsMembersList.isNotEmpty()) {
            groupsMembersList.forEach {
                if (it.portraitUri.isEmpty()) {
                    it.portraitUri = getPortrait(it) ?: ""
                }
            }
            mGroupMemberBox?.put(groupsMembersList)
        }
    }


    fun deleteFriends() {
        mFriendBox?.removeAll()
    }


    fun insertGroups(groups: List<Groups>) {
        mGroupsBox?.put(groups)
    }

    fun deleteGrouops() {
        mGroupsBox?.removeAll()
    }

    fun deleteGroupMembers() {
        mGroupMemberBox?.removeAll()
    }

    /**
     * 获取用户头像,头像为空时会生成默认的头像,此默认头像可能已经存在数据库中,不重新生成
     * 先从缓存读,再从数据库读
     */
    private fun getPortrait(friend: Friend): String? {
        if (friend.portraitUri.isEmpty()) {
            if (friend.userId.isEmpty()) {
                return null
            } else {
                var userInfo: UserInfo? = mUserInfoCache?.get(friend.userId)
                if (userInfo != null) {
                    if (!userInfo.portraitUri?.toString().isNullOrEmpty()) {
                        return userInfo.portraitUri.toString()
                    } else {
                        mUserInfoCache?.remove(friend.userId)
                    }
                }
                val groupMemberList = getGroupMembersWithUserId(friend.userId)
                if (groupMemberList != null && groupMemberList.isNotEmpty()) {
                    val groupMember = groupMemberList[0]
                    if (groupMember.portraitUri.isNotEmpty())
                        return groupMember.portraitUri
                }
                val portrait = RongGenerate.generateDefaultAvatar(friend.name, friend.userId)
                //缓存信息kit会使用,备注名存在时需要缓存displayName
                val name = if (friend.displayName.isNotEmpty()) {
                    friend.displayName
                } else {
                    friend.name
                }
                userInfo = UserInfo(friend.userId, name, Uri.parse(portrait))
                mUserInfoCache?.put(friend.userId, userInfo)
                return portrait
            }
        } else {
            return friend.portraitUri
        }
    }

    private fun getPortrait(groupMember: GroupMember?): String? {
        if (groupMember != null) {
            if (groupMember.portraitUri.isEmpty()) {
                if (groupMember.userId.isEmpty()) {
                    return null
                } else {
                    var userInfo: UserInfo? = mUserInfoCache?.get(groupMember.userId)
                    if (userInfo != null) {
                        if (userInfo.portraitUri != null && userInfo.portraitUri.toString().isNotEmpty()) {
                            return userInfo.portraitUri.toString()
                        } else {
                            mUserInfoCache?.remove(groupMember.userId)
                        }
                    }
                    val friend = getFriendByID(groupMember.userId)
                    if (friend != null) {
                        if (friend.portraitUri.isNotEmpty()) {
                            return friend.portraitUri
                        }
                    }
                    val groupMemberList = getGroupMembersWithUserId(groupMember.userId)
                    if (groupMemberList != null && groupMemberList.isNotEmpty()) {
                        val member = groupMemberList[0]
                        if (member.portraitUri.isNotEmpty()) {
                            return member.portraitUri
                        }
                    }
                    val portrait = RongGenerate.generateDefaultAvatar(groupMember.name, groupMember.userId)
                    return if (portrait.isNotEmpty()) {
                        userInfo = UserInfo(groupMember.userId, groupMember.name, Uri.parse(portrait))
                        mUserInfoCache?.set(groupMember.userId, userInfo)
                        portrait
                    } else {
                        null
                    }
                }
            } else {
                return groupMember.portraitUri
            }
        }
        return null
    }

    /**
     * 同步获取群组成员信息
     *
     * @param userId 用户Id
     * @return List<GroupMember> 群组成员列表
     */
    fun getGroupMembersWithUserId(userId: String): List<GroupMember>? {
        return mGroupMemberBox?.query()?.equal(GroupMember_.userId, userId)?.build()?.find()
    }

    private fun setCreatedToTop(groupMember: List<GroupMemberResp>, groupID: String): List<GroupMember> {
        val newList = ArrayList<GroupMember>()
        var created: GroupMember? = null
        for (group in groupMember) {

            val groups = getGroupsByID(groupID)
            val groupName = groups?.name
            val groupPortraitUri = groups?.portraitUri
            val newMember = GroupMember(groupsId = groupID,
                    userId = group.user.id,
                    name = group.user.nickname,
                    portraitUri = group.user.portraitUri,
                    displayName = group.displayName,
                    nameSpelling = CharacterParser.getSpelling(group.user.nickname),
                    displayNameSpelling = CharacterParser.getSpelling(group.displayName),
                    groupName = groupName,
                    groupNameSpelling = CharacterParser.getSpelling(groupName),
                    groupPortraitUri = groupPortraitUri)
            if (group.role == 0) {
                created = newMember
            } else {
                newList.add(newMember)
            }
        }
        if (created != null) {
            newList.add(created)
        }
        Collections.reverse(newList)
        return newList
    }

    /**
     * 同步接口,获取1个好友信息
     *
     * @param userID 好友ID
     * @return Friend 好友信息
     */
    fun getFriendByID(userID: String): Friend? {
        return if (userID.isEmpty()) {
            null
        } else {
            mFriendBox?.query()?.equal(Friend_.userId, userID)?.build()?.findUnique()
        }
    }

    /**
     * 同步接口,获取1个群组信息
     *
     * @param groupID 群组ID
     * @return Groups 群组信息
     */
    fun getGroupsByID(groupID: String): Groups? {
        return if (groupID.isEmpty()) {
            null
        } else {
            mGroupsBox?.query()?.equal(Groups_.groupsId, groupID)?.build()?.findUnique()
        }
    }

    private fun setGetAllUserInfoDone() {
        DataHelper.setIntergerSF(context, Const.KEY_SP_GET_ALL_USERINFO_STATE, mGetAllUserInfoState)
    }


    private fun hasGetAllUserInfo(): Boolean {
        return mGetAllUserInfoState == ALL
    }


    private fun hasGetFriends(): Boolean {
        return mGetAllUserInfoState and FRIEND != 0
    }

    private fun hasGetGroups(): Boolean {
        return mGetAllUserInfoState and GROUPS != 0
    }

    private fun hasGetAllGroupMembers(): Boolean {
        return mGetAllUserInfoState and GROUPMEMBERS != 0 && mGetAllUserInfoState and PARTGROUPMEMBERS == 0
    }

    private fun hasGetPartGroupMembers(): Boolean {
        return mGetAllUserInfoState and GROUPMEMBERS == 0 && mGetAllUserInfoState and PARTGROUPMEMBERS != 0
    }

    private fun hasGetBlackList(): Boolean {
        return mGetAllUserInfoState and BLACKLIST != 0
    }


    private fun setGetAllUserInfoWtihAllGroupMembersState() {
        mGetAllUserInfoState = mGetAllUserInfoState and PARTGROUPMEMBERS.inv()
        mGetAllUserInfoState = mGetAllUserInfoState or GROUPMEMBERS
    }

}