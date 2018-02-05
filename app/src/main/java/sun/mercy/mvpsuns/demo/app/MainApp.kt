package sun.mercy.mvpsuns.demo.app

import com.mercy.suns.base.BaseApplication
import io.rong.imkit.RongIM
import sun.mercy.mvpsuns.demo.di.component.AppComponent
import sun.mercy.mvpsuns.demo.di.component.DaggerAppComponent


/**
 * @author sun
 * @date 2018/1/31
 * MainApp
 */
class MainApp:BaseApplication() {
    private lateinit var mAppComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        mAppComponent = DaggerAppComponent
                .builder()
                .sunsComponent(sunsComponent)
                .build()
        mAppComponent.inject(this)

        /**
         * 注意：
         *
         * IMKit SDK调用第一步 初始化
         *
         * context上下文
         *
         * 只有两个进程需要初始化，主进程和 push 进程
         */
        RongIM.init(this)

    }


    fun getAppComponent(): AppComponent {
        return this.mAppComponent
    }

}