package sun.mercy.mvpsuns.demo.mvp.model.entity

import sun.mercy.mvpsuns.demo.mvp.model.api.Api


/**
 * @author sun
 * @date 2018/1/31
 * BaseJson
 */
class BaseJson<out T> {
    private val data: T? = null
    private val code: String? = null
    private val msg: String? = null

    fun getData(): T? {
        return data
    }

    fun getCode(): String? {
        return code
    }

    fun getMsg(): String? {
        return msg
    }

    /**
     * 请求是否成功
     *
     * @return
     */
    fun isSuccess(): Boolean {
        return code == Api.RequestSuccess
    }
}