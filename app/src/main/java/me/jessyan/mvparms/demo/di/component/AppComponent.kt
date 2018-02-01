package me.jessyan.mvparms.demo.di.component

import com.jess.arms.di.component.SunsComponent
import com.jess.arms.di.scope.AppScope
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import me.jessyan.mvparms.demo.app.MainApp
import me.jessyan.mvparms.demo.di.module.AppModule

/**
 * @author sun
 * @date 2018/1/31
 * AppComponent
 */
@AppScope
@Component(dependencies = arrayOf(SunsComponent::class),
        modules = arrayOf(AndroidInjectionModule::class, AndroidSupportInjectionModule::class, AppModule::class))
interface AppComponent {
    fun inject(mainApp: MainApp)
}