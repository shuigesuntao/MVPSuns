package sun.mercy.mvpsuns.demo.mvp.presenter

import android.app.Application
import com.mercy.suns.mvp.BasePresenter
import com.mercy.suns.utils.DataHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.rong.imkit.RongIM
import io.rong.imlib.RongIMClient
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber
import sun.mercy.mvpsuns.demo.app.BaseException
import sun.mercy.mvpsuns.demo.app.Const
import sun.mercy.mvpsuns.demo.mvp.contract.LoginContract
import sun.mercy.mvpsuns.demo.mvp.model.entity.UserInfo
import timber.log.Timber


import javax.inject.Inject

/**
 * ================================================
 * 登录Presenter
 * Created by sun on 2018/2/5
 * ================================================
 */
class LoginPresenter @Inject constructor(model: LoginContract.Model, rootView: LoginContract.View, var mApplication: Application?, var mErrorHandler: RxErrorHandler?) :
        BasePresenter<LoginContract.Model, LoginContract.View>(model, rootView) {
//    @Inject
//    var mApplication: Application? = null
//    @Inject
//    var mErrorHandler: RxErrorHandler? = null

    fun login(region: String, phone: String, password: String) {
        mModel.login(region, phone, password)
                .map { it.token }
                .flatMap { connectRongCloud(it) }
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { mRootView.showLoading() }
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .flatMap { mModel.getUserInfoById(it)}
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { mRootView.hideLoading() }
                .subscribe(object :ErrorHandleSubscriber<UserInfo>(mErrorHandler){
                    override fun onNext(t: UserInfo) {
                        DataHelper.setStringSF(mApplication, Const.KEY_SP_PHONE, phone)
                        DataHelper.setStringSF(mApplication, Const.KEY_SP_PASSWORD, password)
                        mRootView.onLoginSuccess(t)
                    }
                })


    }


    private fun connectRongCloud(token: String): Observable<String> {
        DataHelper.setStringSF(mApplication, Const.KEY_SP_TOKEN, token)
        return Observable.create<String> {
            RongIM.connect(token, object : RongIMClient.ConnectCallback() {
                override fun onSuccess(userId: String) {
                    Timber.tag("Sun").e("onSuccess userId:$userId")
                    DataHelper.setStringSF(mApplication, Const.KEY_SP_USER_ID, userId)
                    it.onNext(userId)
                    it.onComplete()
                }

                override fun onError(errorCode: RongIMClient.ErrorCode) {
                    Timber.tag("Sun").e("onError errorCode:${errorCode.value}")
                    it.onError(BaseException(errorCode.value))
                }

                override fun onTokenIncorrect() {
                    Timber.tag("Sun").e("onTokenIncorrect")
                    it.onComplete()
                }
            })
        }.subscribeOn(Schedulers.io())
    }


    override fun onDestroy() {
        super.onDestroy()
        this.mApplication = null
        this.mErrorHandler = null
    }
}