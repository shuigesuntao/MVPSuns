package me.jessyan.mvparms.demo.app

import android.content.Context
import android.net.ParseException
import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import com.jess.arms.utils.ArmsUtils
import me.jessyan.rxerrorhandler.handler.listener.ResponseErrorListener
import org.json.JSONException
import retrofit2.HttpException
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @author sun
 * @date 2018/1/31
 * ResponseErrorListenerImpl
 */
class ResponseErrorListenerImpl:ResponseErrorListener {
    override fun handleResponseError(context: Context?, t: Throwable?) {
        Timber.tag("Catch-Error").w(t?.message)
        //这里不光是只能打印错误,还可以根据不同的错误作出不同的逻辑处理
        var msg = "未知错误"
        when (t) {
            is UnknownHostException -> msg = "网络不可用"
            is SocketTimeoutException -> msg = "请求网络超时"
            is HttpException -> msg = convertStatusCode(t)
            is JsonParseException, is ParseException, is JSONException, is JsonIOException -> msg = "数据解析错误"
        }
        ArmsUtils.snackbarText(msg)
    }

    private fun convertStatusCode(httpException: HttpException): String {
        return when {
            httpException.code() == 500 -> "服务器发生错误"
            httpException.code() == 404 -> "请求地址不存在"
            httpException.code() == 403 -> "请求被服务器拒绝"
            httpException.code() == 307 -> "请求被重定向到其他页面"
            else -> httpException.message()
        }
    }

}