package com.correro.alejandro.tfg.data.api

import com.correro.alejandro.tfg.data.api.models.LoginResponse
import com.correro.alejandro.tfg.data.api.models.historialresponse.HistoricalResponse
import com.correro.alejandro.tfg.data.api.models.userresponse.UserResponse
import io.reactivex.Observable
import retrofit2.http.*


interface ApiService {
    @FormUrlEncoded
    @POST("oauth/v2/token")
    fun login(@Field("username") username: String, @Field("password") password: String, @Field("grant_type") type: String, @Field("client_id") clientid: String, @Field("client_secret") clientSecret: String): Observable<LoginResponse>

    @GET("api/patient/user")
    fun getUser(@Header("Authorization") token: String): Observable<UserResponse>

    @GET("api/patient/historical")
    fun getHistorical(@Header("Authorization") token: String): Observable<HistoricalResponse>
}


