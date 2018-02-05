package sun.mercy.mvpsuns.demo.mvp.ui.widget.loading

import android.content.Context
import com.wang.avi.Indicator
import com.wang.avi.AVLoadingIndicatorView
import java.util.*


/**
 * ================================================
 * LoaderCreator
 * Created by sun on 2018/2/5
 * ================================================
 */
object LoaderCreator {
    private val LOADING_MAP = WeakHashMap<String, Indicator>()

    fun create(type: String, context: Context): AVLoadingIndicatorView {

        val avLoadingIndicatorView = AVLoadingIndicatorView(context)
        if (LOADING_MAP[type] == null) {
            val indicator = getIndicator(type)
            LOADING_MAP[type] = indicator
        }
        avLoadingIndicatorView.indicator = LOADING_MAP[type]
        return avLoadingIndicatorView
    }

    private fun getIndicator(name: String?): Indicator? {
        if (name.isNullOrBlank().not()) {
            return null
        }
        val drawableClassName = StringBuilder()
        if (name!!.contains(".").not()) {
            val defaultPackageName = AVLoadingIndicatorView::class.java.`package`.name
            drawableClassName.append(defaultPackageName)
                    .append(".indicators")
                    .append(".")
        }
        drawableClassName.append(name)
        return try {
            val drawableClass = Class.forName(drawableClassName.toString())
            drawableClass.newInstance() as Indicator
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}