package com.begdev.bigbid.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

//class ConnectivityChecker @Inject constructor(
//    private val connectivityManager: ConnectivityManager,
//    @ApplicationContext private val context: Context
//) : DefaultLifecycleObserver {
//
//    private val _isOnline = MutableStateFlow<Boolean>(false)
//    val isOnline: StateFlow<Boolean> = _isOnline
//
//    private val networkReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//            checkIsOnline()
//        }
//    }
//
//    init {
//        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
//    }
//
//    override fun onStart(owner: LifecycleOwner) {
//        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
//        context.registerReceiver(networkReceiver, filter)
//        checkIsOnline()
//    }
//
//    override fun onStop(owner: LifecycleOwner) {
//        context.unregisterReceiver(networkReceiver)
//    }
//
//    private fun checkIsOnline() {
//        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
//        _isOnline.value = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
//    }
//}

class ConnectivityChecker @Inject constructor(
    private val connectivityManager: ConnectivityManager,
    @ApplicationContext private val context: Context
) : DefaultLifecycleObserver {

    private val _isOnline = MutableStateFlow<Boolean>(false)
    val isOnline: StateFlow<Boolean> = _isOnline

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            checkIsOnline()
        }
    }

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(networkReceiver, filter)
        checkIsOnline()
    }

    override fun onStop(owner: LifecycleOwner) {
        context.unregisterReceiver(networkReceiver)
    }

    private fun checkIsOnline() {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        _isOnline.value = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}

