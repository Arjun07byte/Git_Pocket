package com.example.daggerfirst.api

import com.example.daggerfirst.models.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeApiRef {
    @GET("users/{userName}/repos")
    suspend fun getUserRepos(
        @Path("userName")
        userName: String
    ): Response<UserRepoInfoResponse>

    @GET("users/{userName}")
    suspend fun getUserDetails(
        @Path("userName")
        userName: String
    ): Response<UserInfoSuccessResponse>

    @GET("search/users")
    suspend fun searchUser(
        @Query("q")
        userName: String,
        @Query("per_page")
        perPage: Int = 8,
        @Query("page")
        pageNumber: Int,
        @Query("sort")
        sortBy: String = "followers"
    ): Response<SearchUserResponse>

    @GET("search/repositories")
    suspend fun searchRepo(
        @Query("q")
        userName: String,
        @Query("per_page")
        perPage: Int = 8,
        @Query("page")
        pageNumber: Int,
        @Query("sort")
        sortBy: String = "stars"
    ): Response<SearchRepoResponse>

    @GET("orgs/{orgName}/repos")
    suspend fun searchOrg(
        @Path("orgName")
        orgName: String,
        @Query("per_page")
        perPage: Int = 8,
        @Query("page")
        pageNumber: Int
    ): Response<SearchOrgResponse>
}