package sun.mercy.mvpsuns.demo.mvp.presenter

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
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
import sun.mercy.mvpsuns.demo.app.utils.CharacterParser
import sun.mercy.mvpsuns.demo.app.utils.executeWithLoading
import sun.mercy.mvpsuns.demo.mvp.contract.LoginContract
import sun.mercy.mvpsuns.demo.mvp.contract.MainContract
import sun.mercy.mvpsuns.demo.mvp.model.api.Api
import sun.mercy.mvpsuns.demo.mvp.model.db.entity.Friend
import sun.mercy.mvpsuns.demo.mvp.model.resp.BaseResp
import sun.mercy.mvpsuns.demo.mvp.model.resp.UserInfoResp
import timber.log.Timber
import java.math.BigInteger
import java.util.Locale.filter


import javax.inject.Inject

/**
 * ================================================
 * MainPresenter
 * Created by sun on 2018/2/5
 * ================================================
 */
class MainPresenter @Inject constructor(model: MainContract.Model, rootView: MainContract.View,
                                        var mApplication: Application?, var mErrorHandler: RxErrorHandler?) :
        BasePresenter<MainContract.Model, MainContract.View>(model, rootView) {





    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    internal fun onCreate() {
//        getAllUserInfo()
    }






    override fun onDestroy() {
        super.onDestroy()
        this.mApplication = null
        this.mErrorHandler = null
    }
}