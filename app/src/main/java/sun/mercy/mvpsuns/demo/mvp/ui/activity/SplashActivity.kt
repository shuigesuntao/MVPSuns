package sun.mercy.mvpsuns.demo.mvp.ui.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mercy.suns.utils.DataHelper
import com.mercy.suns.utils.PermissionUtil
import com.mercy.suns.utils.SunsUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import sun.mercy.mvpsuns.demo.app.Const
import java.util.concurrent.TimeUnit



class SplashActivity : AppCompatActivity() {

    private lateinit var mDisposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val token = DataHelper.getStringSF(this@SplashActivity, Const.KEY_SP_TOKEN)
        PermissionUtil.externalStorage(object : PermissionUtil.RequestPermission {
            override fun onRequestPermissionSuccess() {
                mDisposable = Observable.timer(2, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            if (token.isNullOrEmpty()) {
                                goToLogin()
                            } else {
                                goToMain()
                            }
                        })
            }

            override fun onRequestPermissionFailure(permissions: List<String>) {
                SunsUtils.snackbarText("Request permissions failure")
            }

            override fun onRequestPermissionFailureWithAskNeverAgain(permissions: List<String>) {
                SunsUtils.snackbarText("Need to go to the settings")
            }
        }, RxPermissions(this@SplashActivity), SunsUtils.obtainAppComponentFromContext(this).rxErrorHandler())

    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun goToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(mDisposable.isDisposed.not()){
            mDisposable.dispose()
        }
    }

}
