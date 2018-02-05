package sun.mercy.mvpsuns.demo.mvp.ui.widget.loading

import android.content.Context
import android.support.v7.app.AppCompatDialog
import android.view.Gravity
import com.mercy.suns.utils.SunsUtils
import sun.mercy.mvpsuns.demo.R


/**
 * ================================================
 * Loader
 * Created by sun on 2018/2/5
 * ================================================
 */
object Loader {
    private const val LOADER_SIZE_SCALE = 8
    private const val LOADER_OFFSET_SCALE = 10

    private val LOADERS = ArrayList<AppCompatDialog>()

    private val DEFAULT_LOADER = LoaderStyle.LineSpinFadeLoaderIndicator.name

    fun showLoading(context: Context, type: Enum<LoaderStyle>) {
        showLoading(context, type.name)
    }

    fun showLoading(context: Context, type: String) {

        val dialog = AppCompatDialog(context, R.style.dialog)

        val avLoadingIndicatorView = LoaderCreator.create(type, context)
        dialog.setContentView(avLoadingIndicatorView)

        val deviceWidth = SunsUtils.getScreenWidth(context)
        val deviceHeight = SunsUtils.getScreenHeidth(context)

        val dialogWindow = dialog.window

        if (dialogWindow != null) {
            val lp = dialogWindow.attributes
            lp.width = deviceWidth / LOADER_SIZE_SCALE
            lp.height = deviceHeight / LOADER_SIZE_SCALE
            lp.height = lp.height + deviceHeight / LOADER_OFFSET_SCALE
            lp.gravity = Gravity.CENTER
        }
        LOADERS.add(dialog)
        dialog.show()
    }

    fun showLoading(context: Context) {
        showLoading(context, DEFAULT_LOADER)
    }

    fun stopLoading() {
        LOADERS.filter { it.isShowing }
                .forEach { it.cancel() }
    }
}