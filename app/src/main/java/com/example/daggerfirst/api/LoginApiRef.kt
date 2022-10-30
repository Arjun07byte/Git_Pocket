package com.example.daggerfirst.api

import com.example.daggerfirst.models.UserInfoSuccessResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface LoginApiRef {
    @GET("users/{userName}")
    suspend fun getUserDetails(
        @Path("userName")
        userName: String
    ): Response<UserInfoSuccessResponse>
}