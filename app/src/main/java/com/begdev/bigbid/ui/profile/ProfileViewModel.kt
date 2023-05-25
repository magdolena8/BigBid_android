package com.begdev.bigbid.ui.profile

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.begdev.bigbid.data.repository.UsersRepo
import com.begdev.bigbid.utils.ConnectivityChecker
import com.begdev.bigbid.utils.fileFromContentUri
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val usersRepo: UsersRepo,
    private val connectivityChecker: ConnectivityChecker,
    private val application: Application,


    ) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState(UsersRepo.currentUser))
    val uiState: MutableStateFlow<ProfileUiState> = _uiState
    val isOnline: StateFlow<Boolean> = connectivityChecker.isOnline


    init {
        viewModelScope.launch {
//            val item = itemRepo.getItem(itemId.toInt())
            _uiState.value.copy(
                person = UsersRepo.currentUser
            )
        }
    }

    fun handleEvent(profileEvent: ProfileEvent) {
        when (profileEvent) {
            is ProfileEvent.ToggleEditMode -> {
                toggleEditMode()
            }

            is ProfileEvent.ApplyChanges -> {
                applyChanges()
            }

            is ProfileEvent.AvatarChanged -> {
                updateAvatar()
            }
        }
    }

    private fun toggleEditMode() {
        val profileMode = uiState.value.profileMode
        val newProfileMode = if (
            profileMode == EditMode.EDIT
        ) {
            EditMode.NO_EDIT
        } else {
            EditMode.EDIT
        }
        uiState.value = uiState.value.copy(
            profileMode = newProfileMode
        )
    }

    private fun updateAvatar() {

    }

    private fun applyChanges() {
        viewModelScope.launch {
            val result = usersRepo.editUser(
                username = UsersRepo.currentUser?.username!!,
                imageFile = fileFromContentUri(application, uiState.value.imageUri!!),
            )
        }
        toggleEditMode()
    }

    fun updateImageUri(uri: Uri?) {
        uiState.value = uiState.value.copy(
            imageUri = uri
        )
    }

    fun updateBitmap(bitmap: Bitmap?) {
        uiState.value = uiState.value.copy(
            bitmap = bitmap
        )
    }

}