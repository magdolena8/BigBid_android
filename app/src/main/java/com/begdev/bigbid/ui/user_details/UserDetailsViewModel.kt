package com.begdev.bigbid.ui.user_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.begdev.bigbid.nav_utils.AppNavigator
import com.begdev.bigbid.nav_utils.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _viewState = MutableStateFlow(UserDetailsViewState())
    val viewState = _viewState.asStateFlow()

    init {
        val firstName =
            savedStateHandle.get<String>(Destination.UserDetailsScreen.FIST_NAME_KEY) ?: ""
        val lastName =
            savedStateHandle.get<String>(Destination.UserDetailsScreen.LAST_NAME_KEY) ?: ""

        _viewState.update {
            it.copy(
                firstName = firstName,
                lastName = lastName
            )
        }
    }

    fun onBackButtonClicked() {
        appNavigator.tryNavigateBack()
    }
}