package sun.mercy.mvpsuns.demo.mvp.model.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.ColumnInfo
import com.google.gson.annotations.SerializedName

/**
 * @author sun
 * @date 2018/1/31
 * User
 */
@Entity(tableName = "users")
data class User(@PrimaryKey
                val id: Int = 0,
                @ColumnInfo(name = "name")
                val login: String,
                @SerializedName("avatar_url")
                @ColumnInfo(name = "avatar_url")
                val avatarUrl: String)