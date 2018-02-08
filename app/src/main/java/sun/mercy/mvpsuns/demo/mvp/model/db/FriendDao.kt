package sun.mercy.mvpsuns.demo.mvp.model.db

import android.arch.persistence.room.*
import android.location.Location
import io.reactivex.Flowable
import io.reactivex.Observable
import sun.mercy.mvpsuns.demo.mvp.model.entity.Friend
import sun.mercy.mvpsuns.demo.mvp.model.entity.User

/**
 * ================================================
 * FriendDao
 * Created by sun on 2018/2/2
 * ================================================
 */

@Dao
interface FriendDao {

    /**
     * 插入
     *
     * @param friends 好友列表
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg friends: Friend)

    /**
     * 查询
     *
     * @return 所有好友列表
     */
    @Query("SELECT * FROM friend")
    fun getAll(): Flowable<List<Friend>>

    /**
     * 查询指定用户
     *
     * @param name 好友名称
     * @return 好友列表
     */
    @Query("SELECT * FROM friend WHERE name = :name")
    fun getFriendByName(name: String): Flowable<List<Friend>>

    /**
     * 更新好友信息
     *
     * @param friends 要更新的好友列表
     */
    @Update
    fun updateAll(vararg friends: Friend)

    /**
     * 删除好友
     *
     * @param users 要删除的用户列表
     */
    @Delete
    fun deleteFriends(vararg friends: Friend)
}