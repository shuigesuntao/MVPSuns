package sun.mercy.mvpsuns.demo.mvp.presenter

import android.app.Application
import com.mercy.suns.mvp.BasePresenter
import com.mercy.suns.utils.DataHelper
import io.reactivex.Observable
import io.rong.imkit.RongIM
import io.rong.imlib.RongIMClient
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber
import sun.mercy.mvpsuns.demo.app.BaseException
import sun.mercy.mvpsuns.demo.app.Const
import sun.mercy.mvpsuns.demo.app.utils.executeWithLoding
import sun.mercy.mvpsuns.demo.mvp.contract.LoginContract
import sun.mercy.mvpsuns.demo.mvp.model.entity.UserInfo
import timber.log.Timber


import javax.inject.Inject

/**
 * ================================================
 * LoginPresenter
 * Created by sun on 2018/2/5
 * ================================================
 */
class LoginPresenter @Inject constructor(model: LoginContract.Model, rootView: LoginContract.View) :
        BasePresenter<LoginContract.Model, LoginContract.View>(model, rootView) {
    @Inject
    var mApplication: Application? = null
    @Inject
    var mErrorHandler: RxErrorHandler? = null

    fun login(region: String, phone: String, password: String) {
        mModel.login(region, phone, password)
                .map { it.token }
                .flatMap { connectRongIM(it) }
                .flatMap { mModel.getUserInfoById(it) }
                .executeWithLoding(mRootView, object : ErrorHandleSubscriber<UserInfo>(mErrorHandler) {
                    override fun onNext(t: UserInfo) {
                        mRootView.onLoginSuccess(t)
                    }
                })

    }


    fun connectRongIM(token: String): Observable<String>{
        return Observable.create<String>{
            RongIM.connect(token, object : RongIMClient.ConnectCallback() {
                override fun onSuccess(s: String) {
                    Timber.tag("connect").e("onSuccess userId:$s")
                    DataHelper.setStringSF(mApplication, Const.LOGIN_ID, s)
                    it.onNext(s)
                }

                override fun onError(errorCode: RongIMClient.ErrorCode) {
                    Timber.tag("connect").e("onError errorCode:${errorCode.value}")
                    it.onError(BaseException(errorCode.value))
                }

                override fun onTokenIncorrect() {
                    Timber.tag("connect").e("onTokenIncorrect")
                }
            })
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        this.mApplication = null
        this.mErrorHandler = null
    }
}