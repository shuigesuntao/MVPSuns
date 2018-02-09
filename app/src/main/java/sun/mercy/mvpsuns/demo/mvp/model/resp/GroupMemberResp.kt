package sun.mercy.mvpsuns.demo.mvp.model.resp

/**
 * ================================================
 * GroupMemberResp
 * Created by sun on 2018/2/9
 * ================================================
 */
data class GroupMemberResp(val displayName: String,
                           val role: Int,
                           val createdAt: String,
                           val user:UserInfoResp)