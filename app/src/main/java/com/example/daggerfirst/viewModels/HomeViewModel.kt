package com.example.daggerfirst.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daggerfirst.models.UserInfoSuccessResponse
import com.example.daggerfirst.models.UserRepoInfoResponse
import com.example.daggerfirst.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repositoryInstance: HomeRepository
) : ViewModel() {
    val repoSearchResultStates: MutableLiveData<UserRepoInfoResponse?> = MutableLiveData()
    val signedInUserInfoResultStates: MutableLiveData<UserInfoSuccessResponse?> = MutableLiveData()
    val userSearchResultStates: MutableLiveData<List<UserInfoSuccessResponse?>> = MutableLiveData()

    fun getUserRepos(userName: String) {
        viewModelScope.launch {
            val repoSearchResult = repositoryInstance.getUserRepos(userName)
            repoSearchResultStates.postValue(handleRepoSearchResponses(repoSearchResult))
        }
    }

    private fun handleRepoSearchResponses(repoSearchResult: Response<UserRepoInfoResponse>): UserRepoInfoResponse? {
        return if (repoSearchResult.isSuccessful) {
            repoSearchResult.body()
        } else {
            null
        }
    }

    fun getSignedInUserInfo(userName: String) {
        viewModelScope.launch {
            val userInfoResult = repositoryInstance.getUserInfo(userName)
            signedInUserInfoResultStates.postValue(handleUserInfoResponses(userInfoResult))
        }
    }

    private fun handleUserInfoResponses(userInfoResult: Response<UserInfoSuccessResponse>): UserInfoSuccessResponse? {
        return if (userInfoResult.isSuccessful) {
            userInfoResult.body()
        } else {
            Log.e(">>", userInfoResult.raw().message)
            null
        }
    }

    fun searchUser(userName: String, pageNumber: Int) {
        if (viewModelScope.isActive) {
            viewModelScope.coroutineContext[Job]?.cancelChildren();
        }

        viewModelScope.launch {
            delay(1000)
            val userSearchResult = repositoryInstance.searchUser(userName, pageNumber)
            if (userSearchResult.isSuccessful && userSearchResult.body() != null) {
                val fetchedUserList: MutableList<UserInfoSuccessResponse?> = mutableListOf()
                for (eachUser in userSearchResult.body()!!.items) {
                    val userInfo = repositoryInstance.getUserInfo(eachUser.login)
                    fetchedUserList.add(handleUserInfoResponses(userInfo))
                }

                userSearchResultStates.postValue(fetchedUserList)
            }
        }
    }
}