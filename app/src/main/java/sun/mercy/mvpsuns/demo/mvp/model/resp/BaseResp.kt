package sun.mercy.mvpsuns.demo.mvp.model.resp


/**
 * @author sun
 * @date 2018/1/31
 * BaseJson
 */
data class BaseResp<out T>(val code:Int, val result:T)