package com.example.daggerfirst.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daggerfirst.models.UserInfoSuccessResponse
import com.example.daggerfirst.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repositoryInstance: LoginRepository
) : ViewModel() {
    private val loginStates: MutableLiveData<UserInfoSuccessResponse?> = MutableLiveData()

    fun getUserDetails(givenUsername: String) {
        viewModelScope.launch {
            val userDetails = repositoryInstance.getUserDetails(givenUsername)
            loginStates.postValue(handleResponse(userDetails))
        }
    }

    fun getLoginStates() = loginStates

    private fun handleResponse(userDetails: Response<UserInfoSuccessResponse>): UserInfoSuccessResponse? {
        return if(userDetails.isSuccessful) {
            userDetails.body()
        } else {
            null
        }
    }
}