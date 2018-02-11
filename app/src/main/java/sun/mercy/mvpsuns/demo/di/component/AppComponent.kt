package sun.mercy.mvpsuns.demo.di.component

import com.mercy.suns.di.component.SunsComponent
import com.mercy.suns.di.scope.AppScope
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import sun.mercy.mvpsuns.demo.app.MainApp
import sun.mercy.mvpsuns.demo.app.UserInfoManager
import sun.mercy.mvpsuns.demo.di.module.AppModule



/**
 * @author sun
 * @date 2018/1/31
 * AppComponent
 */
@AppScope
@Component(dependencies = arrayOf(SunsComponent::class),
        modules = arrayOf(AndroidInjectionModule::class, AndroidSupportInjectionModule::class, AppModule::class))
interface AppComponent {
    fun userInfoManager():UserInfoManager
    fun inject(mainApp: MainApp)
}