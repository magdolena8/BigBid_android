package com.begdev.bigbid.ui.main

import androidx.lifecycle.ViewModel
import com.begdev.bigbid.utils.ConnectivityChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val connectivityChecker: ConnectivityChecker
) : ViewModel() {
    val isOnline: StateFlow<Boolean> = connectivityChecker.isOnline

}