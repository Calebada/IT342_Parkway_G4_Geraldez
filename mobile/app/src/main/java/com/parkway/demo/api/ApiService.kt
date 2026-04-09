package com.parkway.demo.api

import com.parkway.demo.model.LoginRequest
import com.parkway.demo.model.LoginResponse
import com.parkway.demo.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/users/register")
    suspend fun registerUser(@Body user: User): Response<User>

    @POST("/api/users/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>
}
