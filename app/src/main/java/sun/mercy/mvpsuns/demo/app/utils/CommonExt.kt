package sun.mercy.mvpsuns.demo.app.utils

import android.view.View
import android.widget.Button
import android.widget.EditText
import com.mercy.suns.mvp.IView
import com.mercy.suns.utils.RxLifecycleUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber
import sun.mercy.mvpsuns.demo.app.rx.BaseFunc
import sun.mercy.mvpsuns.demo.app.rx.BaseFuncBoolean
import sun.mercy.mvpsuns.demo.mvp.model.resp.BaseResp
import sun.mercy.mvpsuns.demo.mvp.ui.widget.DefaultTextWatcher

/**
 * ================================================
 * Kotlin通用扩展函数
 * Created by sun on 2018/2/5
 * ================================================
 */
/**
 * 扩展数据转换
 */
fun <T> Observable<BaseResp<T>>.convert(): Observable<T> {
    return this.flatMap(BaseFunc())
}

/**
 * 扩展Boolean类型数据转换
 */
fun <T> Observable<BaseResp<T>>.convertBoolean(): Observable<Boolean> {
    return this.flatMap(BaseFuncBoolean())
}

/**
 * 扩展Observable执行
 */
fun <T> Observable<T>.execute(view: IView, subscriber: ErrorHandleSubscriber<T>) {
    this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .compose(RxLifecycleUtils.bindToLifecycle(view))
            .subscribe(subscriber)
}

/**
 * 扩展Observable执行
 */
fun <T> Observable<T>.executeWithLoading(view: IView, subscriber: ErrorHandleSubscriber<T>) {
    this.subscribeOn(Schedulers.io())
            .doOnSubscribe { view.showLoading() }
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { view.hideLoading() }
            .compose(RxLifecycleUtils.bindToLifecycle(view))
            .subscribe(subscriber)
}

/**
 * 扩展点击事件
 */
fun View.onClick(listener:View.OnClickListener):View{
    setOnClickListener(listener)
    return this
}

/**
 * 扩展点击事件，参数为方法
 */
fun View.onClick(method:() -> Unit):View{
    setOnClickListener { method() }
    return this
}

/**
 * 扩展Button可用性
 */
fun Button.enable(et: EditText, method: () -> Boolean){
    val btn = this
    et.addTextChangedListener(object : DefaultTextWatcher(){
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            btn.isEnabled = method()
        }
    })
}

/**
 * 扩展视图可见性
 */
fun View.setVisible(visible:Boolean){
    this.visibility = if (visible) View.VISIBLE else View.GONE
}


