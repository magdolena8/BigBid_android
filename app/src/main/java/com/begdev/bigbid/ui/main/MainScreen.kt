package com.begdev.bigbid.ui.main

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.begdev.bigbid.nav_utils.Destination
import com.begdev.bigbid.nav_utils.NavHost
import com.begdev.bigbid.nav_utils.NavigationIntent
import com.begdev.bigbid.nav_utils.composable
import com.begdev.bigbid.ui.auth.AuthenticationScreen
import com.begdev.bigbid.ui.auth.RegisterScreen
import com.begdev.bigbid.ui.home.HomeScreen
import com.begdev.bigbid.ui.item.ItemScreen
import com.begdev.bigbid.ui.theme.BigBidTheme
import com.google.accompanist.insets.navigationBarsWithImePadding
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@Preview
@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    NavigationEffects(
        navigationChannel = mainViewModel.navigationChannel,
        navHostController = navController
    )
    BigBidTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsWithImePadding(),
        ) {
            NavHost(
                navController = navController,
                startDestination = Destination.HomeScreen
            ) {
                composable(destination = Destination.LoginScreen) {
                    AuthenticationScreen()
                }
                composable(destination = Destination.RegisterScreen) {
                    RegisterScreen()
                }
                composable(destination = Destination.HomeScreen) {
                    HomeScreen()
                }
                composable(destination = Destination.ItemScreen) {
                    ItemScreen()
                }
//                composable(destination = Destination.UserDetailsScreen) {
//                    UserDetailsScreen()
//                }
            }
        }
    }
}

@Composable
fun NavigationEffects(
    navigationChannel: Channel<NavigationIntent>, navHostController: NavHostController
) {
    val activity = (LocalContext.current as? Activity)
    LaunchedEffect(activity, navHostController, navigationChannel) {
        navigationChannel.receiveAsFlow().collect { intent ->
            if (activity?.isFinishing == true) {
                return@collect
            }
            when (intent) {
                is NavigationIntent.NavigateBack -> {
                    if (intent.route != null) {
                        navHostController.popBackStack(intent.route, intent.inclusive)
                    } else {
                        navHostController.popBackStack()
                    }
                }
                is NavigationIntent.NavigateTo -> {
                    navHostController.navigate(intent.route) {
                        launchSingleTop = intent.isSingleTop
                        intent.popUpToRoute?.let { popUpToRoute ->
                            popUpTo(popUpToRoute) { inclusive = intent.inclusive }
                        }
                    }
                }
            }
        }
    }
}