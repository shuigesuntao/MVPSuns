package me.jessyan.mvparms.demo.app.callbak

import com.kingja.loadsir.callback.Callback
import me.jessyan.mvparms.demo.R

/**
 * @author sun
 * @date 2018/1/31
 * EmptyCallback
 */
class EmptyCallback:Callback() {
    override fun onCreateView(): Int {
        return R.layout.layout_empty
    }
}