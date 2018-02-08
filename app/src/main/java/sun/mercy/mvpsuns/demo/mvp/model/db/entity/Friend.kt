package sun.mercy.mvpsuns.demo.mvp.model.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * @author sun
 * @date 2018/2/8
 * Friend
 */
@Entity(tableName = "friend")
data class Friend (@PrimaryKey
                   @ColumnInfo(name = "user_id")
                   val userId: String,
                   @ColumnInfo(index = true)
                   val name:String,
                   @ColumnInfo(name = "portrait_uri")
                   val portraitUri:String,
                   @ColumnInfo(name = "display_name",index = true)
                   val displayName: String,
                   val region: String,
                   @ColumnInfo(name = "phone_number")
                   val phoneNumber: String,
                   val status: String,
                   val timestamp: Long,
                   val letters: String,
                   @ColumnInfo(name = "name_spelling",index = true)
                   val nameSpelling: String,
                   @ColumnInfo(name = "display_name_spelling", index = true)
                   val displayNameSpelling: String)
