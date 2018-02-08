package sun.mercy.mvpsuns.demo.mvp.model.resp

import sun.mercy.mvpsuns.demo.mvp.model.db.entity.User

/**
 * @author sun
 * @date 2018/2/8
 * RelationshipResp
 */
data class RelationshipResp(val displayName:String,
                            val message:String,
                            val status:String,
                            val updatedAt:String,
                            val user: User)