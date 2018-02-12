package sun.mercy.mvpsuns.demo.di.module

import android.app.Application
import com.mercy.suns.di.scope.AppScope
import com.mercy.suns.integration.RepositoryManager

import dagger.Module
import dagger.Provides
import io.objectbox.BoxStore
import sun.mercy.mvpsuns.demo.app.UserInfoManager
import sun.mercy.mvpsuns.demo.mvp.model.db.entity.MyObjectBox



/**
 * @author sun
 * @date 2018/1/31
 * AppModule
 */
@Module(includes = arrayOf(ActivitiesModuleApp::class))
class AppModule{
    @AppScope
    @Provides
    fun provideBoxStore(application: Application) :BoxStore{
        return MyObjectBox.builder().androidContext(application).build()
    }

    @AppScope
    @Provides
    fun provideUserInfoManager(application: Application) :UserInfoManager{
        return UserInfoManager(application)
    }
}