package me.jessyan.mvparms.demo.mvp.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v4.app.Fragment
import android.support.v4.app.SupportActivity
import com.jess.arms.mvp.BasePresenter
import com.jess.arms.utils.PermissionUtil
import com.jess.arms.utils.RxLifecycleUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.jessyan.mvparms.demo.mvp.contract.UserContract
import me.jessyan.mvparms.demo.mvp.model.entity.User
import me.jessyan.mvparms.demo.mvp.ui.adapter.UserAdapter
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber
import me.jessyan.rxerrorhandler.handler.RetryWithDelay
import javax.inject.Inject

/**
 * @author sun
 * @date 2018/1/31
 * UserPresenter
 */
class UserPresenter @Inject constructor(model: UserContract.Model, rootView: UserContract.View,
                                        var mErrorHandler: RxErrorHandler?,
                                        var mAdapter: UserAdapter?) :
        BasePresenter<UserContract.Model, UserContract.View>(model, rootView) {
    private var lastUserId = 1
    private var isFirst = true

    /**
     * 使用 2017 Google IO 发布的 Architecture Components 中的 Lifecycles 的新特性 (此特性已被加入 Support library)
     * 使 `Presenter` 可以与 [SupportActivity] 和 [Fragment] 的部分生命周期绑定
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    internal fun onCreate() {
        requestUsers(true)//打开 App 时自动加载列表
    }

    fun requestUsers(pullToRefresh: Boolean) {
        //请求外部存储权限用于适配android6.0的权限管理机制
        PermissionUtil.externalStorage(object : PermissionUtil.RequestPermission {
            override fun onRequestPermissionSuccess() {
                //request permission success, do something.
            }

            override fun onRequestPermissionFailure(permissions: List<String>) {
                mRootView.showMessage("Request permissions failure")
            }

            override fun onRequestPermissionFailureWithAskNeverAgain(permissions: List<String>) {
                mRootView.showMessage("Need to go to the settings")
            }
        }, mRootView.getRxPermissions(), mErrorHandler)


        if (pullToRefresh) lastUserId = 1//下拉刷新默认只请求第一页

        //关于RxCache缓存库的使用请参考 http://www.jianshu.com/p/b58ef6b0624b

        var isEvictCache = pullToRefresh//是否驱逐缓存,为ture即不使用缓存,每次下拉刷新即需要最新数据,则不使用缓存

        if (pullToRefresh && isFirst) {//默认在第一次下拉刷新时使用缓存
            isFirst = false
            isEvictCache = false
        }

        mModel.getUsers(lastUserId, isEvictCache)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe {
                    if (pullToRefresh) {
                        mRootView.showLoading()
                    }//显示下拉刷新的进度条
                }.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    if (pullToRefresh) {
                        mRootView.hideLoading()
                    }//隐藏下拉刷新的进度条
                }
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(object : ErrorHandleSubscriber<List<User>>(mErrorHandler) {
                    override fun onNext(users: List<User>) {
                        lastUserId = users[users.size - 1].id//记录最后一个id,用于下一次请求
                        if (users.isEmpty()) {
                            mRootView.showEmpty()
                        } else {
                            mRootView.showContent()
                            mAdapter?.apply {
                                if (pullToRefresh) {
                                    setNewData(users)
                                } else {
                                    addData(users)
                                }
                                if (users.size < 10) {
                                    //第一页如果不够一页就不显示没有更多数据布局
                                    loadMoreEnd(pullToRefresh)
                                    mRootView.showMessage("no more data")
                                } else {
                                    loadMoreComplete()
                                }
                            }
                        }
                    }

                    override fun onError(t: Throwable) {
                        mRootView.showError()
                        super.onError(t)
                    }
                })
    }


    override fun onDestroy() {
        super.onDestroy()
        this.mAdapter = null
        this.mErrorHandler = null
    }
}