@file:Suppress("unused")

package sun.mercy.mvpsuns.demo.app


import android.content.Context
import android.net.Uri
import com.mercy.suns.di.scope.AppScope
import com.mercy.suns.integration.IRepositoryManager
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
import java.util.LinkedHashMap
import javax.inject.Inject


/**
 * ================================================
 * UserInfoManager
 * Created by sun on 2018/2/11
 * ================================================
 */
@AppScope
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

    @Inject
    lateinit var mRepositoryManager: IRepositoryManager

    @Inject
    lateinit var mBoxStore: BoxStore

    private var mAccountService: AccountService
    private var mFriendBox: Box<Friend>? = null
    private var mGroupsBox: Box<Groups>? = null
    private var mGroupMemberBox: Box<GroupMember>? = null
    private var mBlackLisBox: Box<BlackList>? = null
    private var mUserInfoCache: LinkedHashMap<String, UserInfo>? = null

    init {
        mAccountService = mRepositoryManager.obtainRetrofitService(AccountService::class.java)
    }

    fun openDB() {
        mBoxStore.apply {
            mFriendBox = boxFor()
            mGroupsBox = boxFor()
            mGroupMemberBox = boxFor()
            mBlackLisBox = boxFor()
        }
        mUserInfoCache = LinkedHashMap()
        mGetAllUserInfoState = DataHelper.getIntergerSF(context, Const.KEY_SP_GET_ALL_USERINFO_STATE)
    }

    fun closeDB() {

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
        return mAccountService
                .getGroups()
                .convert()
                .flatMap {
                    if (it.isNotEmpty()) {
                        val groups = it.map {
                            Groups(groupsId = it.group.id,
                                    name = it.group.name,
                                    portraitUri = it.group.portraitUri,
                                    role = it.role.toString())
                        }
                        deleteGrouops()
                        insertGroups(groups)
                    }
                    mGetAllUserInfoState = mGetAllUserInfoState or GROUPS
                    Observable.just(it)
                }
    }

    fun fetchGroupMembers(groupId: String): Observable<List<GroupMemberResp>> {
        return mAccountService.getGroupMembers(groupId).convert()
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

    fun deleteFriends() {
        mFriendBox?.removeAll()
    }


    fun insertGroups(groups: List<Groups>) {
        mGroupsBox?.put(groups)
    }

    fun deleteGrouops() {
        mGroupsBox?.removeAll()
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
                var name = friend.name
                if (friend.displayName.isNotEmpty()) {
                    name = friend.displayName
                }
                userInfo = UserInfo(friend.userId, name, Uri.parse(portrait))
                mUserInfoCache?.put(friend.userId, userInfo)
                return portrait
            }
        } else {
            return friend.portraitUri
        }
    }

    /**
     * 同步获取群组成员信息
     *
     * @param userId 用户Id
     * @return List<GroupMember> 群组成员列表
     */
    fun getGroupMembersWithUserId(userId: String): List<GroupMember>? {
        return if (userId.isEmpty()) {
            null
        } else {
            mGroupMemberBox?.query()?.equal(GroupMember_.userId,userId)?.build()?.find()
        }
    }

//    private fun getAllUserInfo() {
//        if (hasGetAllUserInfo()) return
//        Observable.just(!hasGetFriends())
//                .filter { it }
//                .flatMap { fetchFriends() }
//                .map {
//                    if (it.isNotEmpty()) {
//                        mModel.deleteAllFriends()
//                        mModel.insertFriends(it.map { it.convertFriendEntity() })
//                    }
//                    mGetAllUserInfoState = mGetAllUserInfoState or FRIEND
//                }
//                .filter { !hasGetGroups() }
//                .flatMap { mModel.fetchGroups() }
//                .map {
//                    if (it.isNotEmpty()) {
//                        mModel.deleteGroups()
//                        mModel.addGroups(it)
//                        mModel.deleteGroupMembers()
//                    }
//                    mGetAllUserInfoState = mGetAllUserInfoState or GROUPS
//                    it
//                }
//                .filter { !hasGetAllGroupMembers() }
//                .flatMap {
//                    Observable.fromIterable(it)
//                }
//                .flatMap {
//                    mModel.fetchGroupMembers(it.id)
//                }
//                .map {
//                    if (it.isNotEmpty()) {
//                        mModel.addGroupMembers(it, group.getGroupsId())
//                    }
//                }
//                .filter { !hasGetAllGroupMembers() }
//                .filter { hasGetPartGroupMembers() }
//                .flatMap { mModel.fetchGroups() }
//                .map {
//                    if (it.isNotEmpty()) {
//                        mModel.deleteGroups()
//                        mModel.addGroups(it)
//                        mModel.deleteGroupMembers()
//                    }
//                    mGetAllUserInfoState = mGetAllUserInfoState or GROUPS
//                    it
//                }
//                .filter { !hasGetAllGroupMembers() }
//                .flatMap {
//                    Observable.fromIterable(it)
//                }
//                .flatMap {
//                    mModel.fetchGroupMembers(it.id)
//                }
//                .map {
//                    if (it.isNotEmpty()) {
//                        mModel.addGroupMembers(it, group.getGroupsId())
//                    }
//                }
//                .filter { !hasGetBlackList() }
//                .flatMap { mModel.fetchBlackList() }
//                .doOnError {
//                    setGetAllUserInfoDone()
//                }
//                .executeWithLoading(mRootView, object : ErrorHandleSubscriber<>)
//
//
//    }

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

    private fun setGetAllUserInfoWithPartGroupMembersState() {
        mGetAllUserInfoState = mGetAllUserInfoState and GROUPMEMBERS.inv()
        mGetAllUserInfoState = mGetAllUserInfoState or PARTGROUPMEMBERS
    }

    private fun setGetAllUserInfoWtihAllGroupMembersState() {
        mGetAllUserInfoState = mGetAllUserInfoState and PARTGROUPMEMBERS.inv()
        mGetAllUserInfoState = mGetAllUserInfoState or GROUPMEMBERS
    }

}