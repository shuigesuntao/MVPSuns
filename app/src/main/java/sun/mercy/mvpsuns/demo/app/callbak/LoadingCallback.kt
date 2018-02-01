package sun.mercy.mvpsuns.demo.app.callbak

import android.content.Context
import android.view.View
import com.kingja.loadsir.callback.Callback
import sun.mercy.mvpsuns.demo.R

/**
 * @author sun
 * @date 2018/1/31
 * EmptyCallback
 */
class LoadingCallback :Callback() {
    override fun onCreateView(): Int {
        return R.layout.layout_loading
    }

    override fun onReloadEvent(context: Context?, view: View?): Boolean {
        return true
    }
}