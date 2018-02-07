package sun.mercy.mvpsuns.demo.mvp.ui.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import com.mercy.suns.utils.DataHelper
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
