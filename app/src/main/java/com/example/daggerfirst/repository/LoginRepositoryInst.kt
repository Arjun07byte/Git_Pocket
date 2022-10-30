package com.example.daggerfirst.repository

import com.example.daggerfirst.api.LoginApiRef
import javax.inject.Inject

class LoginRepositoryInst @Inject constructor(
    private val loginApiRef: LoginApiRef
) {
    suspend fun getUserDetails(userName: String) = loginApiRef.getUserDetails(userName)
}