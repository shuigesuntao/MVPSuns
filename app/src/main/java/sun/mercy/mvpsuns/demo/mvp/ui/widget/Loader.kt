package sun.mercy.mvpsuns.demo.mvp.ui.widget

import android.content.Context
import android.support.v7.app.AppCompatDialog
import android.view.Gravity
import sun.mercy.mvpsuns.demo.R


/**
 * ================================================
 * Loader
 * Created by sun on 2018/2/7
 * ================================================
 */
object Loader {

    private lateinit var mDialog: AppCompatDialog

    /*
        创建加载对话框
     */
    fun showLoading(context: Context) {
        //样式引入
        mDialog = AppCompatDialog(context, R.style.LightProgressDialog)
        //设置布局
        mDialog.setContentView(R.layout.progress_dialog)
        mDialog.setCancelable(true)
        mDialog.setCanceledOnTouchOutside(false)

        val lp = mDialog.window.attributes
        lp.dimAmount = 0.2f
        lp.gravity = Gravity.CENTER
        //设置属性
        mDialog.show()
    }

    /*
        隐藏加载对话框，动画停止
     */
    fun stopLoading() {
        if (mDialog.isShowing) {
            mDialog.cancel()
        }
    }
}