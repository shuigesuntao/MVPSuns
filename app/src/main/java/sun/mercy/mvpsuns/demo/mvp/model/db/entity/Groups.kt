package sun.mercy.mvpsuns.demo.mvp.model.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * @author sun
 * @date 2018/2/8
 * Groups
 */
@Entity(tableName = "groups")
data class Groups(@PrimaryKey
                  @ColumnInfo(name = "groups_id")
                  val groupsId: String,
                  @ColumnInfo(index = true)
                  val name:String,
                  @ColumnInfo(name = "portrait_uri")
                  val portraitUri:String,
                  @ColumnInfo(name = "display_name")
                  val displayName: String,
                  val role: String,
                  val bulletin: String,
                  val timestamp: String,
                  @ColumnInfo(name = "name_spelling",index = true)
                  val nameSpelling: String)