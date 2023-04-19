package com.begdev.bigbid.auth

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.begdev.bigbid.BigBidApplication
import com.begdev.bigbid.nav_utils.AppNavigator
import com.begdev.bigbid.nav_utils.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
//    application: BigBidApplication
) : ViewModel() {

    fun onNavigateToRegisterBtnClicked() {
        Log.d(TAG, "onNavigateToRegisterBtnClicked: before navigation")
        appNavigator.tryNavigateTo(Destination.RegisterScreen())
        Log.d(TAG, "onNavigateToRegisterBtnClicked: after navigation click !!!!!!!!!!")
    }


//    fun onNavigateToMessagesButtonClicked() {
//        appNavigator.tryNavigateTo(Destination.MessagesScreen())
//    }
//
//    fun onNavigateToDetailsButtonClicked() {
//        appNavigator.tryNavigateTo(Destination.DetailsScreen())
//    }
}