package sun.mercy.mvpsuns.demo.mvp.contract

import com.mercy.suns.mvp.IModel
import com.mercy.suns.mvp.IView
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import sun.mercy.mvpsuns.demo.mvp.model.resp.*


/**
 * ================================================
 * LoginContract
 * Created by sun on 2018/2/5
 * ================================================
 */
interface LoginContract {
    interface View : IView {
        fun onLoginSuccess()
    }

    interface Model : IModel {
        fun login(region: String, phone: String, password: String): Observable<LoginResp>
        fun getToken(): Observable<LoginResp>
        fun getUserInfoById(userId: String): Observable<BaseResp<UserInfoResp>>
    }
}