package sun.mercy.mvpsuns.demo.mvp.model.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * @author sun
 * @date 2018/2/8
 * Blacklist
 */
@Entity(tableName = "black_list")
data class BlackList(@PrimaryKey
                     val userId:String,
                     val status:String,
                     val timestamp: Long)