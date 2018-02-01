package sun.mercy.mvpsuns.demo.di.module

import com.mercy.suns.di.component.BaseActivityComponent
import com.mercy.suns.di.scope.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector
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
}