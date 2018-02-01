package me.jessyan.mvparms.demo.app

import com.jess.arms.base.BaseApplication
import me.jessyan.mvparms.demo.di.component.AppComponent
import me.jessyan.mvparms.demo.di.component.DaggerAppComponent


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