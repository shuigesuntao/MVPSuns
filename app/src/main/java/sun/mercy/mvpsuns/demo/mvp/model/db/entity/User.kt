package sun.mercy.mvpsuns.demo.mvp.model.db.entity

import com.google.gson.annotations.SerializedName
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.NameInDb

/**
 * @author sun
 * @date 2018/1/31
 * User
 */
@Entity
data class User(@Id(assignable = true)
                var id: Long = 0,
                @NameInDb("name")
                val login: String,
                @SerializedName("avatar_url")
                @NameInDb("avatar_url")
                val avatarUrl: String)