package sun.mercy.mvpsuns.demo.mvp.model.db

import android.arch.persistence.room.*
import io.reactivex.Flowable
import sun.mercy.mvpsuns.demo.mvp.model.entity.Groups


/**
 * ================================================
 * GroupsDao
 * Created by sun on 2018/2/2
 * ================================================
 */

@Dao
interface GroupsDao {

    /**
     * 插入
     *
     * @param groups 群列表
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg groups: Groups)

    /**
     * 查询
     *
     * @return 所有群列表
     */
    @Query("SELECT * FROM groups")
    fun getAll(): Flowable<List<Groups>>

    /**
     * 查询指定用户
     *
     * @param name 群名称
     * @return 群列表
     */
    @Query("SELECT * FROM groups WHERE name = :name")
    fun getGroupsByName(name: String): Flowable<List<Groups>>

    /**
     * 更新好友信息
     *
     * @param groups 要更新的群列表
     */
    @Update
    fun updateAll(vararg groups: Groups)

    /**
     * 删除好友
     *
     * @param users 要删除的群列表
     */
    @Delete
    fun deleteGroups(vararg groups: Groups)
}