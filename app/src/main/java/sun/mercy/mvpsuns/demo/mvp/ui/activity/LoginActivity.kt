package sun.mercy.mvpsuns.demo.mvp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.mercy.suns.base.BaseMvpActivity
import com.mercy.suns.utils.SunsUtils
import io.rong.imkit.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import sun.mercy.mvpsuns.demo.R
import sun.mercy.mvpsuns.demo.app.utils.enable
import sun.mercy.mvpsuns.demo.app.utils.onClick
import sun.mercy.mvpsuns.demo.mvp.contract.LoginContract
import sun.mercy.mvpsuns.demo.mvp.presenter.LoginPresenter
import sun.mercy.mvpsuns.demo.mvp.ui.widget.Loader

class LoginActivity : BaseMvpActivity<LoginPresenter>(), LoginContract.View, View.OnClickListener{

    private lateinit var mPhone: String
    private lateinit var mPassword: String

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_login
    }

    override fun initData(savedInstanceState: Bundle?) {
        mBtnLogin.enable(mEtPhone,{isBtnEnable()})
        mBtnLogin.enable(mEtPassword,{isBtnEnable()})
        mBtnLogin.onClick(this)
    }

    override fun showLoading() {
        Loader.showLoading(this)
    }

    override fun hideLoading() {
        Loader.stopLoading()
    }

    override fun showMessage(message: String?) {
        SunsUtils.snackbarText(message)
    }


    override fun onLoginSuccess() {
        showMessage("登录成功")
        startActivity(Intent(this@LoginActivity, UserActivity::class.java))
        finish()
    }


    override fun onClick(v: View) {
       when(v.id){
            R.id.mBtnLogin -> {
                mPhone = mEtPhone.text.toString()
                mPassword = mEtPassword.text.toString()
                mPresenter.login("86",mPhone,mPassword)
            }
       }
    }


    /**
     * 判断按钮是否可用
     */
    private fun isBtnEnable():Boolean{
        return mEtPhone.text.isNullOrEmpty().not() &&
                mEtPassword.text.isNullOrEmpty().not()
    }


}
