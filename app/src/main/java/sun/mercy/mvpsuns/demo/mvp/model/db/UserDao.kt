package sun.mercy.mvpsuns.demo.mvp.model.db

import android.arch.persistence.room.*
import android.location.Location
import io.reactivex.Flowable
import io.reactivex.Observable
import sun.mercy.mvpsuns.demo.mvp.model.entity.User

/**
 * ================================================
 * UserDao
 * Created by sun on 2018/2/2
 * ================================================
 */

@Dao
interface UserDao {

    /**
     * 插入
     *
     * @param users 用户列表
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: User)

    /**
     * 查询
     *
     * @return 所有用户列表
     */
    @Query("SELECT * FROM users")
    fun getAll(): Flowable<List<User>>

    /**
     * 查询指定用户
     *
     * @param name 用户名称
     * @return 用户列表
     */
    @Query("SELECT * FROM users WHERE name = :name")
    fun getUserByLogin(name: String): Flowable<List<User>>

    /**
     * 更新用户信息
     *
     * @param users 要更新的用户列表
     */
    @Update
    fun updateAll(vararg users: User)

    /**
     * 删除用户
     *
     * @param users 要删除的用户列表
     */
    @Delete
    fun deleteLocation(vararg users: User)
}