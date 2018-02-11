package sun.mercy.mvpsuns.demo.mvp.model.db.entity


import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index
import io.objectbox.annotation.NameInDb

/**
 * @author sun
 * @date 2018/2/8
 * Friend
 */
@Entity
data class Friend(@Id
                  var id: Long = 0,
                  @NameInDb("user_id")
                  @Index
                  val userId: String,
                  @Index
                  val name: String,
                  @NameInDb("portrait_uri")
                  var portraitUri: String = "",
                  @NameInDb("display_name")
                  @Index
                  val displayName: String = "",
                  val region: String = "",
                  @NameInDb("phone_number")
                  val phoneNumber: String = "",
                  val status: String = "",
                  val timestamp: Long = 0,
                  val letters: String = "",
                  @NameInDb("name_spelling")
                  @Index
                  val nameSpelling: String? = "",
                  @NameInDb("display_name_spelling")
                  @Index
                  val displayNameSpelling: String? = "")
