package me.jessyan.mvparms.demo.di.module

import com.jess.arms.di.component.BaseActivityComponent
import com.jess.arms.di.scope.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.jessyan.mvparms.demo.mvp.ui.activity.UserActivity

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