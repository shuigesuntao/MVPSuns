package me.jessyan.mvparms.demo.app.utils

import com.jess.arms.mvp.IView
import com.jess.arms.utils.RxLifecycleUtils
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author sun
 * @date 2018/1/31
 * RxUtils
 */
object RxUtils {

    @JvmStatic
    fun <T> applySchedulers(view: IView): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable.subscribeOn(Schedulers.io())
                    .doOnSubscribe { view.showLoading() }
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally { view.hideLoading() }
                    .compose(RxLifecycleUtils.bindToLifecycle(view))
        }
    }

    @JvmStatic
    fun <T> applySimpleSchedulers(view: IView): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(RxLifecycleUtils.bindToLifecycle(view))
        }
    }
}