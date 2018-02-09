package sun.mercy.mvpsuns.demo.mvp.presenter

import android.app.Application
import android.net.Uri
import com.mercy.suns.mvp.BasePresenter
import com.mercy.suns.utils.DataHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.rong.imkit.RongIM
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.UserInfo
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber
import sun.mercy.mvpsuns.demo.app.BaseException
import sun.mercy.mvpsuns.demo.app.Const
import sun.mercy.mvpsuns.demo.mvp.contract.LoginContract
import sun.mercy.mvpsuns.demo.mvp.model.api.Api
import sun.mercy.mvpsuns.demo.mvp.model.resp.BaseResp
import sun.mercy.mvpsuns.demo.mvp.model.resp.UserInfoResp
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

    private var mUserId:String? = null

    /**
     * 用户信息全部未同步
     */
    private val NONE = 0//00000
    /**
     * 好友信息同步成功
     */
    private val FRIEND = 1//00001
    /**
     * 群组信息同步成功
     */
    private val GROUPS = 2//00010
    /**
     * 群成员信息部分同步成功,n个群组n次访问网络,存在部分同步的情况
     */
    private val PARTGROUPMEMBERS = 4//00100
    /**
     * 群成员信息同步成功
     */
    private val GROUPMEMBERS = 8//01000
    /**
     * 黑名单信息同步成功
     */
    private val BLACKLIST = 16//10000
    /**
     * 用户信息全部同步成功
     */
    private val ALL = 27//11011

    private val mGetAllUserInfoState = null


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
                .subscribe(object : ErrorHandleSubscriber<BaseResp<UserInfoResp>>(mErrorHandler){
                    override fun onNext(t: BaseResp<UserInfoResp>) {
                        if(t.code == Api.RequestSuccess){
                            if(t.result.portraitUri.isEmpty().not()){
                                //TODO 如果没设置头像 生成默认头像
                            }
                            //sp存储
                            val nickName = t.result.nickname
                            val portraitUri = t.result.portraitUri
                            DataHelper.setStringSF(mApplication, Const.KEY_SP_NICKNAME, nickName)
                            DataHelper.setStringSF(mApplication, Const.KEY_SP_PORTRAIT, portraitUri)
                            RongIM.getInstance().refreshUserInfoCache(UserInfo(mUserId, nickName, Uri.parse(portraitUri)))
                        }
                        getAllUserInfo()
                        DataHelper.setStringSF(mApplication, Const.KEY_SP_PHONE, phone)
                        DataHelper.setStringSF(mApplication, Const.KEY_SP_PASSWORD, password)
                        mRootView.onLoginSuccess()
                    }

                })
    }

    private fun getAllUserInfo() {
//        if(){
//
//        }
    }

    private fun needGetAllUserInfo(): Boolean {
        return mGetAllUserInfoState == NONE
    }


    private fun connectRongCloud(token: String): Observable<String> {
        DataHelper.setStringSF(mApplication, Const.KEY_SP_TOKEN, token)
        return Observable.create<String> {
            RongIM.connect(token, object : RongIMClient.ConnectCallback() {
                override fun onSuccess(userId: String) {
                    Timber.tag("Sun").e("onSuccess userId:$userId")
                    mUserId = userId
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