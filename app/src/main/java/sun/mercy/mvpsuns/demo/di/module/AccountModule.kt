package sun.mercy.mvpsuns.demo.di.module

import android.app.Application
import com.mercy.suns.di.scope.ActivityScope
import dagger.Module
import dagger.Provides
import sun.mercy.mvpsuns.demo.app.UserInfoManager
import sun.mercy.mvpsuns.demo.mvp.contract.LoginContract
import sun.mercy.mvpsuns.demo.mvp.model.LoginModel
import sun.mercy.mvpsuns.demo.mvp.ui.activity.LoginActivity
import com.tbruyelle.rxpermissions2.RxPermissions



/**
 * @author sun
 * @date 2018/1/31
 * UserActivityModule
 */
@Module
class AccountModule {
    @Provides
    fun provideLoginView(activity: LoginActivity): LoginContract.View {
        return activity
    }

    @ActivityScope
    @Provides
    fun provideLoginModel(model: LoginModel): LoginContract.Model {
        return model
    }

    @ActivityScope
    @Provides
    fun provideRxPermissions(activity: LoginActivity): RxPermissions {
        return RxPermissions(activity)
    }

}