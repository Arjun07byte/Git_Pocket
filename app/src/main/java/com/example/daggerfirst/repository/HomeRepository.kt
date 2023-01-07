package com.example.daggerfirst.repository

import com.example.daggerfirst.api.HomeApiRef
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val homeApiRef: HomeApiRef
) {
    suspend fun getUserRepos(userName: String) = homeApiRef.getUserRepos(userName)
    suspend fun getUserInfo(userName: String) = homeApiRef.getUserDetails(userName)
    suspend fun searchUser(userName: String, pageNumber: Int) =
        homeApiRef.searchUser(userName, pageNumber = pageNumber)
}