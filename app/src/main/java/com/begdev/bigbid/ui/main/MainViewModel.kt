package com.begdev.bigbid.ui.main

import androidx.lifecycle.ViewModel
import com.begdev.bigbid.nav_utils.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
//    appNavigator: AppNavigator,
//    private val appNavigator: AppNavigator,

) : ViewModel() {
    val navigationChannel = appNavigator.navigationChannel
}