package sun.mercy.mvpsuns.demo.di.module

import android.app.Application
import dagger.Module

/**
 * @author sun
 * @date 2018/1/31
 * AppModule
 */
@Module(includes = arrayOf(ActivitiesModuleApp::class))
class AppModule(private val application: Application)