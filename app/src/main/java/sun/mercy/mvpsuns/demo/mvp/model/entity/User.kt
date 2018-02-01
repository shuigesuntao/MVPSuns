package sun.mercy.mvpsuns.demo.mvp.model.entity

import com.google.gson.annotations.SerializedName

/**
 * @author sun
 * @date 2018/1/31
 * User
 */
data class User(val id: Int = 0,
                val login: String = "",
                @SerializedName("avatar_url") val avatarUrl: String = "")