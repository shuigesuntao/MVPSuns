@file:Suppress("unused")

package sun.mercy.mvpsuns.demo.app


import android.content.Context
import android.net.Uri
import com.mercy.suns.utils.DataHelper
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import io.objectbox.rx.RxQuery
import io.reactivex.Observable
import io.rong.imlib.model.UserInfo
import org.json.JSONException
import sun.mercy.mvpsuns.demo.app.utils.CharacterParser
import sun.mercy.mvpsuns.demo.app.utils.RongGenerate
import sun.mercy.mvpsuns.demo.app.utils.convert
import sun.mercy.mvpsuns.demo.mvp.model.api.service.AccountService
import sun.mercy.mvpsuns.demo.mvp.model.db.entity.*
import sun.mercy.mvpsuns.demo.mvp.model.resp.*
import javax.inject.Singleton


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
            if (!fetchFriends()) {
                setGetAllUserInfoDone()
                return
            }
        }
        if (!hasGetGroups()) {
            if (!fetchGroups()) {
                setGetAllUserInfoDone()
                return
            }
            if (!hasGetAllGroupMembers()) {
                if (!fetchGroupMembers()) {
                    setGetAllUserInfoDone()
                    return
                }
            }
        }
        if (!hasGetAllGroupMembers()) {
            if (hasGetPartGroupMembers()) {
                deleteGroupMembers()
            }
            getGroups()
                ?.flatMap {
                if (it.isNotEmpty()) {
                    deleteGroupMembers()
                    Observable.fromIterable(it)
                }else{
                    setGetAllUserInfoWtihAllGroupMembersState()
                }

            }
            fetchGroupMembers()
        }
        if (!hasGetBlackList()) {
            fetchBlackList()
        }
        setGetAllUserInfoDone()
    }

    fun getGroups(): Observable<MutableList<Groups>>? {
        return mGroupsBox?.let {
            RxQuery.observable(it.query().build())
        }
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
        var fetchGroupCount = 0
        if (mGroupsList!!.isNotEmpty()) {
            deleteGroupMembers()
            for (group in mGroupsList!!) {
                val groupMemberResponse: GetGroupMemberResponse?
                try {
                    mAccountService.getGroupMembers(group.groupsId).convert()
                            .map {
                                fetchGroupCount++
                                if (it.isNotEmpty()) {
                                    if (mGroupMemberBox != null) {
                                        addGroupMembers(it, group.groupsId)
                                    } else if (mBoxStore == null) {
                                        //如果这两个都为null,说明是被踢,已经关闭数据库,没要必要继续执行
                                        false
                                    }
                                } else {
                                    if (fetchGroupCount > 0) {
                                        setGetAllUserInfoWithPartGroupMembersState()
                                    }
                                    false
                                }
                            }
                } catch (e: JSONException) {
                    fetchGroupCount++
                    continue
                }

                if (groupMemberResponse != null && groupMemberResponse!!.getCode() === 200) {
                    fetchGroupCount++
                    val list = groupMemberResponse!!.getResult()
                    if (list != null && list!!.size > 0) {
                        if (mGroupMemberDao != null) {
                            addGroupMembers(list, group.getGroupsId())
                        } else if (mDBManager == null) {
                            //如果这两个都为null,说明是被踢,已经关闭数据库,没要必要继续执行
                            return false
                        }
                    }
                } else {
                    if (fetchGroupCount > 0) {
                        setGetAllUserInfoWithPartGroupMembersState()
                    }
                    return false
                }
            }
            if (mGroupsList != null && fetchGroupCount == mGroupsList.size) {
                setGetAllUserInfoWtihAllGroupMembersState()
                return true
            }
        } else {
            setGetAllUserInfoWtihAllGroupMembersState()
            return true
        }
        return false
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
            mGroupMemberBox?.query()?.equal(GroupMember_.userId, userId)?.build()?.find()
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

    private fun setGetAllUserInfoWithPartGroupMembersState() {
        mGetAllUserInfoState = mGetAllUserInfoState and GROUPMEMBERS.inv()
        mGetAllUserInfoState = mGetAllUserInfoState or PARTGROUPMEMBERS
    }

    private fun setGetAllUserInfoWtihAllGroupMembersState() {
        mGetAllUserInfoState = mGetAllUserInfoState and PARTGROUPMEMBERS.inv()
        mGetAllUserInfoState = mGetAllUserInfoState or GROUPMEMBERS
    }

}