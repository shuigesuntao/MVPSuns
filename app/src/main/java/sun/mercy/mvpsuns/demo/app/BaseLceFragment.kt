package sun.mercy.mvpsuns.demo.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mercy.suns.base.BaseMvpFragment
import com.mercy.suns.mvp.IPresenter
import com.kingja.loadsir.core.LoadSir
import com.kingja.loadsir.core.LoadService


/**
 * @author sun
 * @date 2018/1/31
 * BaseLceFragment
 */
abstract class BaseLceFragment<P : IPresenter> : BaseMvpFragment<P>() {
    protected lateinit var mLoadService: LoadService<*>
    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView: View = setUpView(inflater,container,savedInstanceState)
        mLoadService = LoadSir.getDefault().register(rootView) {
            // 重新加载逻辑
            onReload()
        }
        return mLoadService.loadLayout
    }

    abstract fun setUpView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View
    abstract fun onReload()
}