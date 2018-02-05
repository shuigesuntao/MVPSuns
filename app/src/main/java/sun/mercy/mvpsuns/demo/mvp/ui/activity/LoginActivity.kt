package sun.mercy.mvpsuns.demo.mvp.ui.activity

import android.os.Bundle
import android.view.View
import com.mercy.suns.base.BaseMvpActivity
import com.mercy.suns.utils.DataHelper
import io.rong.imkit.RongIM
import io.rong.imlib.RongIMClient
import kotlinx.android.synthetic.main.activity_login.*
import sun.mercy.mvpsuns.demo.R
import sun.mercy.mvpsuns.demo.app.Const
import sun.mercy.mvpsuns.demo.app.utils.enable
import sun.mercy.mvpsuns.demo.app.utils.onClick
import sun.mercy.mvpsuns.demo.mvp.contract.LoginContract
import sun.mercy.mvpsuns.demo.mvp.model.entity.LoginResp
import sun.mercy.mvpsuns.demo.mvp.presenter.LoginPresenter
import sun.mercy.mvpsuns.demo.mvp.ui.widget.loading.Loader
import timber.log.Timber

class LoginActivity : BaseMvpActivity<LoginPresenter>(), LoginContract.View, View.OnClickListener{

    private lateinit var mPhone: String
    private lateinit var mPassword: String
    private var mToken:String? = null
    private var connectResultId: String? = null

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

    }

    override fun onLoginSuccess(result:LoginResp) {
        mToken = result.token
        if(mToken.isNullOrEmpty().not()){
           RongIM.connect(mToken,object : RongIMClient.ConnectCallback(){
               override fun onSuccess(s: String?) {
                   connectResultId = s
                   Timber.tag("connect").e("onSuccess userId:$s")
                   DataHelper.setStringSF(this@LoginActivity,Const.LOGIN_ID, s)
               }

               override fun onError(errorCode: RongIMClient.ErrorCode) {
                   Timber.tag("connect").e("onError errorCode:${errorCode.value}")
               }

               override fun onTokenIncorrect() {
                   Timber.tag("connect").e("onTokenIncorrect")
                   mPresenter.getToken()
               }

           })
        }
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
        return mEtPhone.text.isNullOrBlank().not() &&
                mEtPassword.text.isNullOrBlank().not()
    }


}
