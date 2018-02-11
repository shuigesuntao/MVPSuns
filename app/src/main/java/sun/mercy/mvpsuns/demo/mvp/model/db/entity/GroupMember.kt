package sun.mercy.mvpsuns.demo.mvp.model.db.entity


import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index
import io.objectbox.annotation.NameInDb

/**
 * @author sun
 * @date 2018/2/8
 * GroupMember
 */
@Entity
data class GroupMember(@Id
                       var id: Long = 0,
                       @NameInDb("groups_id")
                       val groupsId: String,
                       @NameInDb("user_id")
                       val userId: String,
                       @Index
                       val name: String,
                       @NameInDb("portrait_uri")
                       val portraitUri: String,
                       @NameInDb("display_name")
                       @Index
                       val displayName: String,
                       @NameInDb("name_spelling")
                       @Index
                       val nameSpelling: String,
                       @NameInDb("display_name_spelling")
                       @Index
                       val displayNameSpelling: String,
                       @NameInDb("group_name")
                       @Index
                       val groupName: String,
                       @NameInDb("group_name_spelling")
                       @Index
                       val groupNameSpelling: String,
                       @NameInDb("group_portrait_uri")
                       val groupPortraitUri: String)