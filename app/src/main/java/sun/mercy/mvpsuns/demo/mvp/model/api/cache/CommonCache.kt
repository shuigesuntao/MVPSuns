package sun.mercy.mvpsuns.demo.mvp.model.api.cache

import io.reactivex.Observable
import io.rx_cache2.DynamicKey
import io.rx_cache2.EvictProvider
import io.rx_cache2.LifeCache
import io.rx_cache2.Reply
import sun.mercy.mvpsuns.demo.mvp.model.entity.User

import java.util.concurrent.TimeUnit

/**
 * @author sun
 * @date 2018/1/31
 * CommonCache
 */
interface CommonCache {
    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    fun getUsers(users: Observable<List<User>>, idLastUserQueried: DynamicKey, evictProvider: EvictProvider): Observable<Reply<List<User>>>
}