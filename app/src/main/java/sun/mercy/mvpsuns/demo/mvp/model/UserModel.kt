package sun.mercy.mvpsuns.demo.mvp.model

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import com.mercy.suns.di.scope.ActivityScope
import com.mercy.suns.integration.IRepositoryManager
import com.mercy.suns.mvp.BaseModel
import io.reactivex.Observable
import io.rx_cache2.DynamicKey
import io.rx_cache2.EvictDynamicKey
import sun.mercy.mvpsuns.demo.mvp.contract.UserContract
import sun.mercy.mvpsuns.demo.mvp.model.api.cache.CommonCache
import sun.mercy.mvpsuns.demo.mvp.model.api.service.AccountService
import sun.mercy.mvpsuns.demo.mvp.model.db.UserInfoDb
import sun.mercy.mvpsuns.demo.mvp.model.db.entity.User

import timber.log.Timber
import javax.inject.Inject

/**
 * @author sun
 * @date 2018/1/31
 * UserModel
 */
@ActivityScope
class UserModel @Inject constructor(repositoryManager: IRepositoryManager):
        BaseModel(repositoryManager), UserContract.Model {



    private val USERS_PER_PAGE = 10

    override fun getUsers(lastIdQueried: Int, update: Boolean): Observable<List<User>> {
        //使用rxcache缓存,上拉刷新则不读取缓存,加载更多读取缓存
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(AccountService::class.java)
                .getUsers(lastIdQueried, USERS_PER_PAGE))
                .flatMap { listObservable ->
                    mRepositoryManager.obtainCacheService(CommonCache::class.java)
                            .getUsers(listObservable, DynamicKey(lastIdQueried), EvictDynamicKey(update))
                            .map { listReply -> listReply.data }
                }

    }

    override fun getAllUsersFromDb(): Observable<List<User>> {
        return mRepositoryManager
                .obtainRoomDatabase(UserInfoDb::class.java, UserInfoDb.DB_NAME)
                .userDao()
                .getAll()
                .toObservable()
    }

    override fun saveUsers(users: List<User>) {
        mRepositoryManager
                .obtainRoomDatabase(UserInfoDb::class.java, UserInfoDb.DB_NAME)
                .userDao()
                .insertAll(*users.toTypedArray())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    internal fun onPause() {
        Timber.d("Release Resource")
    }
}