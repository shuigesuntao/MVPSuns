package sun.mercy.mvpsuns.demo.mvp.model.db.entity


import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * @author sun
 * @date 2018/2/8
 * Blacklist
 */
@Entity
data class BlackList(@Id
                     var id: Long = 0,
                     val userId: String,
                     val status: String,
                     val timestamp: Long)