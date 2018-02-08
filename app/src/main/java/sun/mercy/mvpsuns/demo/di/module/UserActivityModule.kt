package sun.mercy.mvpsuns.demo.di.module

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.mercy.suns.di.scope.ActivityScope
import dagger.Module
import dagger.Provides
import sun.mercy.mvpsuns.demo.mvp.contract.UserContract
import sun.mercy.mvpsuns.demo.mvp.model.UserModel
import sun.mercy.mvpsuns.demo.mvp.model.db.entity.User
import sun.mercy.mvpsuns.demo.mvp.ui.activity.UserActivity
import sun.mercy.mvpsuns.demo.mvp.ui.adapter.UserAdapter

import java.util.ArrayList

/**
 * @author sun
 * @date 2018/1/31
 * UserActivityModule
 */
@Module
class UserActivityModule {
    @Provides
    fun provideUserView(activity: UserActivity): UserContract.View {
        return activity
    }

    @ActivityScope
    @Provides
    fun provideUserModel(model: UserModel): UserContract.Model {
        return model
    }

    @ActivityScope
    @Provides
    fun provideLayoutManager(activity: UserActivity): RecyclerView.LayoutManager {
        return LinearLayoutManager(activity)
    }

    @ActivityScope
    @Provides
    fun provideUserList(): List<User> {
        return ArrayList()
    }

    @ActivityScope
    @Provides
    fun provideUserAdapter(list: List<User>): UserAdapter {
        return UserAdapter(list)
    }
}