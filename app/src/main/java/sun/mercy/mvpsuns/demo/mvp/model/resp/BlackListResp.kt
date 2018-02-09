package sun.mercy.mvpsuns.demo.mvp.model.resp

/**
 * ================================================
 * BlackListResp
 * Created by sun on 2018/2/9
 * ================================================
 */
data class BlackListResp(val user: BlackListUserResp){

    data class BlackListUserResp(val id:String,
                                 val nickname:String,
                                 val portraitUri:String,
                                 val updatedAt:String)
}


