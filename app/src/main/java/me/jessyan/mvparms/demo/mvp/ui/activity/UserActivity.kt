package me.jessyan.mvparms.demo.mvp.ui.activity

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.jess.arms.utils.ArmsUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_user.*
import me.jessyan.mvparms.demo.R
import me.jessyan.mvparms.demo.app.BaseLceActivity
import me.jessyan.mvparms.demo.app.callbak.EmptyCallback
import me.jessyan.mvparms.demo.app.callbak.ErrorCallback
import me.jessyan.mvparms.demo.mvp.contract.UserContract
import me.jessyan.mvparms.demo.mvp.presenter.UserPresenter
import me.jessyan.mvparms.demo.mvp.ui.adapter.UserAdapter
import timber.log.Timber
import javax.inject.Inject

/**
 * @author sun
 * @date 2018/1/31
 * UserActivity
 */
class UserActivity: BaseLceActivity<UserPresenter>(), UserContract.View {

    @Inject
    lateinit var mAdapter: UserAdapter
    @Inject
    lateinit var mLayoutManager: RecyclerView.LayoutManager


    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_user
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        initRecyclerView()
    }

    /**
     * 初始化RecyclerView
     */
    private fun initRecyclerView() {
        mSmartRefreshLayout.setOnRefreshListener { mPresenter.requestUsers(true) }

        mRecyclerView.apply {
            //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
            setHasFixedSize(true)
            layoutManager = mLayoutManager
        }

        mAdapter.apply {
            setOnLoadMoreListener({ mPresenter.requestUsers(false) }, mRecyclerView)
            setOnItemClickListener { _, _, position -> showMessage("position" + position) }
            openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT)
            isFirstOnly(false)
        }
        mRecyclerView.adapter = mAdapter
    }

    override fun onReload() {
        mPresenter.requestUsers(true)
    }

    override fun showLoading() {
        Timber.tag(TAG).w("showLoading")
//        mSmartRefreshLayout.isRefreshing = true
    }

    override fun hideLoading() {
        Timber.tag(TAG).w("hideLoading")
        mSmartRefreshLayout.finishRefresh()
    }

    override fun showMessage(message: String) {
        ArmsUtils.snackbarText(message)
    }

    override fun showEmpty() {
        mLoadService.showCallback(EmptyCallback::class.java)
    }

    override fun showError() {
        mLoadService.showCallback(ErrorCallback::class.java)
    }

    override fun showContent() {
        mLoadService.showSuccess()
    }


    override fun getRxPermissions(): RxPermissions {
        return RxPermissions(this)
    }

}