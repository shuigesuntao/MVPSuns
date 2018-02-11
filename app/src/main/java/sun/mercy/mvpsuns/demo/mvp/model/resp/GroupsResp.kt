package sun.mercy.mvpsuns.demo.mvp.model.resp

import sun.mercy.mvpsuns.demo.mvp.model.db.entity.Groups

/**
 * ================================================
 * GroupsResp
 * Created by sun on 2018/2/9
 * ================================================
 */
data class GroupsResp(val role:Int,
                      val group:GroupsEntity) {


    data class GroupsEntity(val id: String,
                            val name: String,
                            val portraitUri: String,
                            val creatorId: String,
                            val memberCount: Int)
}

