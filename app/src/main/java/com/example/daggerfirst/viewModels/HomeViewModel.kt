package com.example.daggerfirst.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daggerfirst.models.*
import com.example.daggerfirst.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repositoryInstance: HomeRepository
) : ViewModel() {
    val signedInUserRepoResultStates: MutableLiveData<UserRepoInfoResponse?> = MutableLiveData()
    val signedInUserInfoResultStates: MutableLiveData<UserInfoSuccessResponse?> = MutableLiveData()
    val userSearchResultStates: MutableLiveData<List<UserInfoSuccessResponse?>> = MutableLiveData()
    val repoSearchResultStates: MutableLiveData<List<SearchRepoResponseItem?>?> = MutableLiveData()
    val orgSearchResultStates: MutableLiveData<List<SearchRepoResponseItem?>?> = MutableLiveData()

    fun getUserRepos(userName: String) {
        viewModelScope.launch {
            val repoSearchResult = repositoryInstance.getUserRepos(userName)
            signedInUserRepoResultStates.postValue(handleRepoSearchResponses(repoSearchResult))
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
            null
        }
    }

    fun searchUser(userName: String, pageNumber: Int) {
        cancelAllSearch()

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

    fun searchRepo(repoSearchQuery: String, pageNumber: Int) {
        cancelAllSearch()

        viewModelScope.launch {
            delay(1000)
            val repoSearchResult = repositoryInstance.searchRepo(repoSearchQuery, pageNumber)
            repoSearchResultStates.postValue(handleSearchRepoInfoResponses(repoSearchResult))
        }
    }

    private fun handleSearchRepoInfoResponses(repoInfoResult: Response<SearchRepoResponse>): List<SearchRepoResponseItem?>? {
        return if (repoInfoResult.isSuccessful) {
            repoInfoResult.body()?.items
        } else {
            null
        }
    }

    fun searchOrg(orgNameQuery: String, pageNumber: Int) {
        cancelAllSearch()

        viewModelScope.launch {
            delay(1000)
            val orgSearchResult = repositoryInstance.searchOrg(orgNameQuery, pageNumber)
            orgSearchResultStates.postValue(handleSearchOrgInfoResponses(orgSearchResult))
        }
    }

    private fun handleSearchOrgInfoResponses(orgSearchResult: Response<SearchOrgResponse>): List<SearchRepoResponseItem?>? {
        return if (orgSearchResult.isSuccessful) orgSearchResult.body() else null
    }

    fun cancelAllSearch() {
        if (viewModelScope.isActive) viewModelScope.coroutineContext[Job]?.cancelChildren()

    }
}