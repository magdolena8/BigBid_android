package com.begdev.bigbid.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.begdev.bigbid.data.repository.UsersRepo
import com.begdev.bigbid.utils.ConnectivityChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val usersRepo: UsersRepo,
    private val connectivityChecker: ConnectivityChecker

):ViewModel(){
    private val _uiState = MutableStateFlow(ProfileUiState(UsersRepo.currentUser))
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    val isOnline: StateFlow<Boolean> = connectivityChecker.isOnline


    init {
        viewModelScope.launch {
//            val item = itemRepo.getItem(itemId.toInt())
            _uiState.value.copy(
                person = UsersRepo.currentUser
            )
        }
    }
}