package sun.mercy.mvpsuns.demo.app.callbak

import com.kingja.loadsir.callback.Callback
import sun.mercy.mvpsuns.demo.R

/**
 * @author sun
 * @date 2018/1/31
 * EmptyCallback
 */
class ErrorCallback :Callback() {
    override fun onCreateView(): Int {
        return R.layout.layout_error
    }
}