package sun.mercy.mvpsuns.demo.di.module

import android.app.Application
import com.mercy.suns.di.scope.AppScope

import dagger.Module
import dagger.Provides
import sun.mercy.mvpsuns.demo.app.UserInfoManager


/**
 * @author sun
 * @date 2018/1/31
 * AppModule
 */
@Module(includes = arrayOf(ActivitiesModuleApp::class))
class AppModule(private val application: Application){
    @AppScope
    @Provides
    fun provideUserInfoManager() :UserInfoManager{
        return UserInfoManager(application)
    }
}