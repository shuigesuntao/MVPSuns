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

    private var mDialog: AppCompatDialog? = null

    /*
        创建加载对话框
     */
    fun showLoading(context: Context) {
        //样式引入
        mDialog = AppCompatDialog(context, R.style.LightProgressDialog)
        //设置布局
        mDialog?.apply {
            setContentView(R.layout.progress_dialog)
            setCancelable(true)
            setCanceledOnTouchOutside(false)
            window.attributes.dimAmount = 0.2f
            window.attributes.gravity =  Gravity.CENTER
            show()
        }
    }

    /*
        隐藏加载对话框，动画停止
     */
    fun stopLoading() {
        mDialog?.apply {
            dismiss()
            mDialog = null
        }
    }
}