package me.jessyan.mvparms.demo.app.callbak

import android.content.Context
import android.view.View
import android.widget.Toast
import com.kingja.loadsir.callback.Callback
import me.jessyan.mvparms.demo.R

/**
 * @author sun
 * @date 2018/1/31
 * EmptyCallback
 */
class TimeoutCallback :Callback() {
    override fun onCreateView(): Int {
        return R.layout.layout_timeout
    }

    override fun onReloadEvent(context: Context?, view: View?): Boolean {
        Toast.makeText(context?.applicationContext,"Connecting to the network again!",Toast.LENGTH_SHORT).show()
        return false
    }
}