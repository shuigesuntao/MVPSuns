package sun.mercy.mvpsuns.demo.mvp.model.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import sun.mercy.mvpsuns.demo.mvp.model.entity.*

/**
 * ================================================
 * UserDb
 * Created by sun on 2018/2/2
 * ================================================
 */
@Database(entities = arrayOf(User::class,Groups::class,GroupMember::class,Friend::class
        ,BlackList::class),version = 1)
abstract class UserInfoDb : RoomDatabase(){
    companion object {
        val DB_NAME = UserInfoDb::class.java.simpleName
    }

    abstract fun userDao():UserDao
    abstract fun groupsDap():GroupsDao
    abstract fun groupMemberDao():GroupMemberDao
    abstract fun friendDao():FriendDao
    abstract fun blackListDao():BlackListDao

}