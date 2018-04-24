package com.correro.alejandro.tfg.api

import com.correro.alejandro.tfg.api.models.LoginResponse
import com.correro.alejandro.tfg.api.models.userresponse.UserResponse
import io.reactivex.Observable
import retrofit2.http.*


interface ApiService {
    @FormUrlEncoded
    @POST("oauth/v2/token")
    fun login(@Field("username") username: String, @Field("password") password: String, @Field("grant_type") type: String, @Field("client_id") clientid: String, @Field("client_secret") clientSecret: String): Observable<LoginResponse>

    @GET("api/patient/user")
    fun getUser(@Header("Authorization") token: String): Observable<UserResponse>

}


