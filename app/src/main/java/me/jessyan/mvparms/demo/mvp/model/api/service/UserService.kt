package me.jessyan.mvparms.demo.mvp.model.api.service

import io.reactivex.Observable
import me.jessyan.mvparms.demo.mvp.model.entity.User
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

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