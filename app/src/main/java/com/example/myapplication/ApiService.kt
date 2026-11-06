package com.example.myapplication

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun getUsers(): Response<UserResponse>

    @GET("products")
    suspend fun getProducts(): Response<ProductResponse>
}
