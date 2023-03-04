package com.eati.pexels.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

private const val ACCESS_TOKEN = "fbkMMuiOT48ANL0m612gRhn8kfkOOehtHVaNZ20uXD1CyH6EhN4GE5cR"

// https://www.pexels.com/api/documentation/#photos-search
interface PexelsApi {

    @GET("search")
    suspend fun getPhotos(
        @Query("query") query: String, @Query("page") page: Int = 1,
        @Header("Authorization")
        token: String = ACCESS_TOKEN
    ): PhotosResponse

    companion object {
        private const val BASE_URL = "https://api.pexels.com/v1/"

        private val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        private val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        fun create(): PexelsApi {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            return retrofit.create(PexelsApi::class.java)
        }
    }
}
