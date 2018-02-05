package sun.mercy.mvpsuns.demo.di.module

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.mercy.suns.di.scope.ActivityScope
import dagger.Module
import dagger.Provides
import sun.mercy.mvpsuns.demo.mvp.contract.LoginContract
import sun.mercy.mvpsuns.demo.mvp.contract.UserContract
import sun.mercy.mvpsuns.demo.mvp.model.LoginModel
import sun.mercy.mvpsuns.demo.mvp.model.UserModel
import sun.mercy.mvpsuns.demo.mvp.model.entity.User
import sun.mercy.mvpsuns.demo.mvp.ui.activity.LoginActivity
import sun.mercy.mvpsuns.demo.mvp.ui.activity.UserActivity
import sun.mercy.mvpsuns.demo.mvp.ui.adapter.UserAdapter

import java.util.ArrayList

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

}