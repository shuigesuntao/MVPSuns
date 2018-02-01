package sun.mercy.mvpsuns.demo.mvp.model.api.service

import io.reactivex.Observable

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import sun.mercy.mvpsuns.demo.mvp.model.entity.User

/**
 * @author sun
 * @date 2018/1/31
 * UserService
 */
interface UserService {

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/users")
    fun getUsers(@Query("since") lastIdQueried: Int, @Query("per_page") perPage: Int): Observable<List<User>>
}