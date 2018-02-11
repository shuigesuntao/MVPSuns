package sun.mercy.mvpsuns.demo.mvp.model.db.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index
import io.objectbox.annotation.NameInDb

/**
 * @author sun
 * @date 2018/2/8
 * Groups
 */
@Entity
data class Groups(@Id
                  var id:Long = 0,
                  @NameInDb("groups_id")
                  @Index
                  val groupsId: String,
                  @Index
                  val name:String,
                  @NameInDb("portrait_uri")
                  val portraitUri:String,
                  @NameInDb("display_name")
                  val displayName: String = "",
                  val role: String,
                  val bulletin: String = "",
                  val timestamp: String = "",
                  @NameInDb("name_spelling")
                  @Index
                  val nameSpelling: String = "")