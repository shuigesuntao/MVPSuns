package sun.mercy.mvpsuns.demo.app.rx

import io.reactivex.Observable
import io.reactivex.functions.Function
import sun.mercy.mvpsuns.demo.app.BaseException
import sun.mercy.mvpsuns.demo.mvp.model.api.Api
import sun.mercy.mvpsuns.demo.mvp.model.resp.BaseResp

/**
 * ================================================
 * 通用数据类型转换封装
 * Created by sun on 2018/2/5
 * ================================================
 */
class BaseFuncBoolean<T>:Function<BaseResp<T>, Observable<Boolean>>{
    override fun apply(t: BaseResp<T>): Observable<Boolean> {
        if (t.code != Api.RequestSuccess){
            return Observable.error(BaseException(t.code))
        }

        return Observable.just(true)
    }
}