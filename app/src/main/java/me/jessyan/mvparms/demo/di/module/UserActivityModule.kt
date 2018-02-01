package me.jessyan.mvparms.demo.di.module

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.jess.arms.di.scope.ActivityScope
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.Module
import dagger.Provides
import me.jessyan.mvparms.demo.mvp.contract.UserContract
import me.jessyan.mvparms.demo.mvp.model.UserModel
import me.jessyan.mvparms.demo.mvp.model.entity.User
import me.jessyan.mvparms.demo.mvp.ui.activity.UserActivity
import me.jessyan.mvparms.demo.mvp.ui.adapter.UserAdapter
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