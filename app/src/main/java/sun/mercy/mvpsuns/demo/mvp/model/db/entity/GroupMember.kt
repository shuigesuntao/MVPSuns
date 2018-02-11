package sun.mercy.mvpsuns.demo.mvp.model.db.entity


import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * @author sun
 * @date 2018/2/8
 * GroupMember
 */
@Entity(tableName = "group_member",primaryKeys = arrayOf("groups_id","user_id"))
data class GroupMember(@ColumnInfo(name = "groups_id")
                       val groupsId: String,
                       @ColumnInfo(name = "user_id")
                       val userId: String,
                       @ColumnInfo(index = true)
                       val name: String,
                       @ColumnInfo(name = "portrait_uri")
                       val portraitUri: String,
                       @ColumnInfo(name = "display_name", index = true)
                       val displayName: String,
                       @ColumnInfo(name = "name_spelling", index = true)
                       val nameSpelling: String,
                       @ColumnInfo(name = "display_name_spelling", index = true)
                       val displayNameSpelling: String,
                       @ColumnInfo(name = "group_name", index = true)
                       val groupName: String,
                       @ColumnInfo(name = "group_name_spelling", index = true)
                       val groupNameSpelling: String,
                       @ColumnInfo(name = "group_portrait_uri")
                       val groupPortraitUri: String)