package me.jessyan.mvparms.demo.app

import android.os.Bundle
import com.jess.arms.base.BaseActivity
import com.jess.arms.mvp.IPresenter
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir



/**
 * @author sun
 * @date 2018/1/31
 * BaseLceActivity
 */
abstract class BaseLceActivity<P:IPresenter>:BaseActivity<P>() {

    protected lateinit var mLoadService: LoadService<*>

    override fun initData(savedInstanceState: Bundle?) {
        mLoadService = LoadSir.getDefault().register(this) {
            //重新加载逻辑
            onReload()
        }

    }
    abstract fun onReload()
}