package sun.mercy.mvpsuns.demo.mvp.model.api.protocol

import sun.mercy.mvpsuns.demo.mvp.model.api.Api


/**
 * @author sun
 * @date 2018/1/31
 * BaseJson
 */
data class BaseResp<out T>(val code:Int, val result:T)