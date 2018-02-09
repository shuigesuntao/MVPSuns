package sun.mercy.mvpsuns.demo.mvp.model.resp


/**
 * @author sun
 * @date 2018/2/8
 * FriendResp
 */
data class FriendResp(val displayName:String,
                      val message:String,
                      val status:String,
                      val updatedAt:String,
                      val user: UserInfoResp)