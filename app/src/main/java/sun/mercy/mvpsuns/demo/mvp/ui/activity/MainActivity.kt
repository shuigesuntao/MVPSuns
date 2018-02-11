package sun.mercy.mvpsuns.demo.mvp.ui.activity


import android.os.Bundle
import com.mercy.suns.base.BaseMvpActivity
import sun.mercy.mvpsuns.demo.R
import sun.mercy.mvpsuns.demo.mvp.contract.MainContract
import sun.mercy.mvpsuns.demo.mvp.presenter.MainPresenter

class MainActivity : BaseMvpActivity<MainPresenter>(), MainContract.View{


    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_main
    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showMessage(message: String?) {
    }


}
