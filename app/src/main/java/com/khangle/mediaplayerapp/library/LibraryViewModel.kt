package com.khangle.mediaplayerapp.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.mediaplayerapp.data.model.UserInfo
import com.khangle.mediaplayerapp.data.network.retrofit.DeezerBaseUserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(private val baseUserService: DeezerBaseUserService) : ViewModel() {

    private val _user = MutableLiveData<UserInfo>()
    val user: LiveData<UserInfo> = _user
    fun getUserInfo(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userInfo = baseUserService.getUserInfo(token)
            _user.postValue(userInfo)
        }

    }

}