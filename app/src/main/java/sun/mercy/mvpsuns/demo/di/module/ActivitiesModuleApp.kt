package sun.mercy.mvpsuns.demo.di.module

import com.mercy.suns.di.component.BaseActivityComponent
import com.mercy.suns.di.scope.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector
import sun.mercy.mvpsuns.demo.mvp.ui.activity.LoginActivity
import sun.mercy.mvpsuns.demo.mvp.ui.activity.MainActivity
import sun.mercy.mvpsuns.demo.mvp.ui.activity.UserActivity


/**
 * @author sun
 * @date 2018/1/31
 * ActivitiesModuleApp
 */
@Module(subcomponents = arrayOf(BaseActivityComponent::class))
abstract class ActivitiesModuleApp {
    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(UserActivityModule::class))
    abstract fun contributeUserActivity(): UserActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(AccountModule::class))
    abstract fun contributeLoginActivity(): LoginActivity


    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(AccountModule::class))
    abstract fun contributeMainActivity(): MainActivity

}