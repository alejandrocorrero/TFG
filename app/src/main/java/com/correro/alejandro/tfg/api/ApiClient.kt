package com.correro.alejandro.tfg.api


import android.content.Context

import com.readystatesoftware.chuck.ChuckInterceptor

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient private constructor(context: Context) {

    private val service: ApiService

    init {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        builder.addInterceptor(ChuckInterceptor(context))
        val client: OkHttpClient = builder.build();
        val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.1.213/tfgapi/api/web/app_dev.php/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        service = retrofit.create<ApiService>(ApiService::class.java)
    }
    companion object Factory {
        private var ourInstance: ApiClient?=null

        @Synchronized
        fun getInstance(contxt: Context): ApiClient {
            if (ourInstance == null) {
                ourInstance = ApiClient(contxt)
            }
            return ourInstance as ApiClient
        }
    }
    fun getService(): ApiService {
        return service
    }
}