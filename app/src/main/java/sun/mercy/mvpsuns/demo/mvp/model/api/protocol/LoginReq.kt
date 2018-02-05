package sun.mercy.mvpsuns.demo.mvp.model.api.protocol

/**
 * ================================================
 * 登录请求体
 * Created by sun on 2018/2/5
 * ================================================
 */
data class LoginReq(val region:String,val phone:String,val password:String)