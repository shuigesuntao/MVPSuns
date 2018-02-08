package sun.mercy.mvpsuns.demo.mvp.model

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import com.mercy.suns.di.scope.ActivityScope
import com.mercy.suns.integration.IRepositoryManager
import com.mercy.suns.mvp.BaseModel
import io.reactivex.Observable
import sun.mercy.mvpsuns.demo.app.utils.convert
import sun.mercy.mvpsuns.demo.mvp.contract.LoginContract
import sun.mercy.mvpsuns.demo.mvp.model.resp.BaseResp
import sun.mercy.mvpsuns.demo.mvp.model.api.protocol.LoginReq
import sun.mercy.mvpsuns.demo.mvp.model.api.service.AccountService
import sun.mercy.mvpsuns.demo.mvp.model.resp.LoginResp
import sun.mercy.mvpsuns.demo.mvp.model.resp.RelationshipResp
import sun.mercy.mvpsuns.demo.mvp.model.resp.UserInfoResp

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

    override fun fetchFriends(): Observable<List<RelationshipResp>> {
        return mRepositoryManager.obtainRetrofitService(AccountService::class.java)
                .getAllUserRelationship().convert()
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    internal fun onPause() {
        Timber.d("Release Resource")
    }
}