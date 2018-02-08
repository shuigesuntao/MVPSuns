package sun.mercy.mvpsuns.demo.mvp.model.db

import android.arch.persistence.room.*
import android.location.Location
import io.reactivex.Flowable
import io.reactivex.Observable
import sun.mercy.mvpsuns.demo.mvp.model.entity.BlackList
import sun.mercy.mvpsuns.demo.mvp.model.entity.Friend
import sun.mercy.mvpsuns.demo.mvp.model.entity.User

/**
 * ================================================
 * BlackListDao
 * Created by sun on 2018/2/2
 * ================================================
 */

@Dao
interface BlackListDao {

    /**
     * 插入
     *
     * @param blackList 黑名单列表
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg blackList: BlackList)

    /**
     * 查询
     *
     * @return 所有黑名单列表
     */
    @Query("SELECT * FROM black_list")
    fun getAll(): Flowable<List<BlackList>>

    /**
     * 查询指定用户
     *
     * @param userId 黑名单成员id
     * @return 黑名单成员
     */
    @Query("SELECT * FROM black_list WHERE userId = :userId")
    fun getBlackListById(userId: String): Flowable<BlackList>

    /**
     * 更新黑名单信息
     *
     * @param blackList 要更新的黑名单列表
     */
    @Update
    fun updateAll(vararg blackList: BlackList)

    /**
     * 删除黑名单成员
     *
     * @param blackList 要删除的黑名单用户列表
     */
    @Delete
    fun deleteBlackList(vararg blackList: BlackList)
}