package sun.mercy.mvpsuns.demo.mvp.contract

import com.mercy.suns.mvp.IModel
import com.mercy.suns.mvp.IView
import io.reactivex.Observable
import sun.mercy.mvpsuns.demo.mvp.model.entity.LoginResp
import sun.mercy.mvpsuns.demo.mvp.model.entity.UserInfo


/**
 * ================================================
 * LoginContract
 * Created by sun on 2018/2/5
 * ================================================
 */
interface LoginContract {
    interface View : IView {
        fun onLoginSuccess(userInfo: UserInfo)
    }

    interface Model : IModel {
        fun login(region:String,phone:String,password:String): Observable<LoginResp>
        fun getToken(): Observable<LoginResp>
        fun getUserInfoById(userId:String): Observable<UserInfo>
    }
}