package sun.mercy.mvpsuns.demo.mvp.model

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import com.mercy.suns.di.scope.ActivityScope
import com.mercy.suns.integration.IRepositoryManager
import com.mercy.suns.mvp.BaseModel
import io.reactivex.Observable
import sun.mercy.mvpsuns.demo.app.utils.convert
import sun.mercy.mvpsuns.demo.mvp.contract.LoginContract
import sun.mercy.mvpsuns.demo.mvp.model.api.protocol.LoginReq
import sun.mercy.mvpsuns.demo.mvp.model.api.service.AccountService
import sun.mercy.mvpsuns.demo.mvp.model.resp.*

import timber.log.Timber
import javax.inject.Inject

/**
 * @author sun
 * @date 2018/1/31
 * UserModel
 */
@ActivityScope
class LoginModel @Inject constructor(repositoryManager: IRepositoryManager):
        BaseModel(repositoryManager), LoginContract.Model {

    override fun login(region: String, phone: String, password: String): Observable<LoginResp> {
        return mRepositoryManager.obtainRetrofitService(AccountService::class.java)
                .login(LoginReq(region, phone, password)).convert()
    }

    override fun getToken(): Observable<LoginResp> {
        return mRepositoryManager.obtainRetrofitService(AccountService::class.java)
                .getToken().convert()
    }

    override fun getUserInfoById(userId: String): Observable<BaseResp<UserInfoResp>> {
        return mRepositoryManager.obtainRetrofitService(AccountService::class.java)
                .getUserInfoById(userId)
    }

    override fun fetchFriends(): Observable<List<FriendResp>> {
        return mRepositoryManager.obtainRetrofitService(AccountService::class.java)
                .getAllFriends().convert()
    }

    override fun fetchGroups(): Observable<List<GroupsResp>> {
        return mRepositoryManager.obtainRetrofitService(AccountService::class.java)
                .getGroups().convert()
    }

    override fun fetchGroupMembers(groupId:String): Observable<List<GroupMemberResp>> {
        return mRepositoryManager.obtainRetrofitService(AccountService::class.java)
                .getGroupMembers(groupId).convert()
    }

    override fun fetchBlackList(): Observable<List<BlackListResp>> {
        return mRepositoryManager.obtainRetrofitService(AccountService::class.java)
                .getBlackList().convert()
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    internal fun onPause() {
        Timber.d("Release Resource")
    }
}