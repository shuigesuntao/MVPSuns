package sun.mercy.mvpsuns.demo.mvp.model.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import sun.mercy.mvpsuns.demo.mvp.model.entity.User

/**
 * ================================================
 * UserDb
 * Created by sun on 2018/2/2
 * ================================================
 */
@Database(entities = arrayOf(User::class),version = 1)
abstract class UserDb : RoomDatabase(){
    companion object {
        val DB_NAME = UserDb::class.java.simpleName
    }

    abstract fun userDao():UserDao
}