package sun.mercy.mvpsuns.demo.app

import com.mercy.suns.base.BaseApplication
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
    }


    fun getAppComponent(): AppComponent {
        return this.mAppComponent
    }

}