package sun.mercy.mvpsuns.demo.mvp.model.db

import android.arch.persistence.room.*
import android.location.Location
import io.reactivex.Flowable
import io.reactivex.Observable
import sun.mercy.mvpsuns.demo.mvp.model.entity.Friend
import sun.mercy.mvpsuns.demo.mvp.model.entity.GroupMember
import sun.mercy.mvpsuns.demo.mvp.model.entity.Groups
import sun.mercy.mvpsuns.demo.mvp.model.entity.User

/**
 * ================================================
 * GroupMemberDao
 * Created by sun on 2018/2/2
 * ================================================
 */

@Dao
interface GroupMemberDao {

    /**
     * 插入
     *
     * @param groupMembers 群成员列表
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg groupMembers: GroupMember)

    /**
     * 查询
     *
     * @return 所有群成员列表
     */
    @Query("SELECT * FROM group_member")
    fun getAll(): Flowable<List<GroupMember>>

    /**
     * 查询指定群成员
     *
     * @param name 群成员名称
     * @return 群成员列表
     */
    @Query("SELECT * FROM group_member WHERE name = :name")
    fun getGroupMemberByName(name: String): Flowable<List<GroupMember>>

    /**
     * 更新群成员信息
     *
     * @param groupMembers 要更新的群成员列表
     */
    @Update
    fun updateAll(vararg groupMembers: GroupMember)

    /**
     * 删除群成员
     *
     * @param groupMembers 要删除的群成员列表
     */
    @Delete
    fun deleteGroupMembers(vararg groupMembers: GroupMember)
}