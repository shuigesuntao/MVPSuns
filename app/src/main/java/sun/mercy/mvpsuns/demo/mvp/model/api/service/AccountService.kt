package sun.mercy.mvpsuns.demo.mvp.model.api.service

import io.reactivex.Observable
import retrofit2.http.*
import sun.mercy.mvpsuns.demo.mvp.model.api.protocol.BaseResp
import sun.mercy.mvpsuns.demo.mvp.model.api.protocol.LoginReq
import sun.mercy.mvpsuns.demo.mvp.model.entity.LoginResp

import sun.mercy.mvpsuns.demo.mvp.model.entity.User
import sun.mercy.mvpsuns.demo.mvp.model.entity.UserInfo

/**
 * @author sun
 * @date 2018/1/31
 * AccountService
 */
interface AccountService {

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/users")
    fun getUsers(@Query("since") lastIdQueried: Int, @Query("per_page") perPage: Int):
            Observable<List<User>>

    /**
     * 登录: 登录成功后，会设置 Cookie，后续接口调用需要登录的权限都依赖于 Cookie。
     *
     * @param region   国家码
     * @param phone    手机号
     * @param password 密码
     */
    @POST("/user/login")
    fun login(@Body req: LoginReq):Observable<BaseResp<LoginResp>>


    /**
     * 获取 token 前置条件需要登录
     */
    @POST("/user/get_token")
    fun getToken():Observable<BaseResp<LoginResp>>

    /**
     * 根据 id 查询用户信息
     */
    @GET("/user/{userid}")
    fun getUserInfoById(@Path("userid") userId:String):Observable<BaseResp<UserInfo>>
}