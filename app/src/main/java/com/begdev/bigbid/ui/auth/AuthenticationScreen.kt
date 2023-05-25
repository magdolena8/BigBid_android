package com.begdev.bigbid.ui.auth

//import com.google.accompanist.insets.navigationBarsWithImePadding

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.begdev.bigbid.R
import com.begdev.bigbid.ui.theme.BigBidTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationScreen(
    navController: NavController,
    viewModel: AuthenticationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isOnline by viewModel.isOnline.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.navigationEvent.collect { destination ->
            navController.navigate(destination)
        }
    }
    LaunchedEffect(viewModel.toastEvent) {
        viewModel.toastEvent.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    Surface(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        BigBidTheme {
            AuthenticationContent(
                modifier = Modifier.fillMaxSize(),
                authenticationState = viewModel.uiState.collectAsState().value,
                handleEvent = viewModel::handleEvent,
                navController = navController,
                isOnline = isOnline
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationContent(
    modifier: Modifier,
    authenticationState: AuthenticationState,
    handleEvent: (event: AuthenticationEvent) -> Unit,
    navController: NavController,
    isOnline: Boolean
) {
//    LaunchedEffect(viewModel) {
//        viewModel.navigationEvent.collect { destination ->
//            navController.navigate(destination)
//        }
//    }

    AuthenticationForm(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
            .navigationBarsWithImePadding(),
        authenticationMode = authenticationState.authenticationMode,
        email = authenticationState.email,
        password = authenticationState.password,
        username = authenticationState.username,
        onEmailChanged = { handleEvent(AuthenticationEvent.EmailChanged(it)) },
        onUsernameChanged = { handleEvent(AuthenticationEvent.UsernameChanged(it)) },
        onPasswordChanged = { handleEvent(AuthenticationEvent.PasswordChanged(it)) },
        onToggleMode = { handleEvent(AuthenticationEvent.ToggleAuthenticationMode) },
        onAuthenticate = { handleEvent(AuthenticationEvent.Authenticate) },
        isLoggedIn = authenticationState.isSuccess,
        navController = navController,
        isOnline = isOnline
    )
}


@Composable
fun AuthenticationForm(
    modifier: Modifier,
    authenticationMode: AuthenticationMode,
    email: String,
    username: String,
    password: String,
    onEmailChanged: (email: String) -> Unit,
    onUsernameChanged: (email: String) -> Unit,
    onPasswordChanged: (password: String) -> Unit,
    onToggleMode: () -> Unit,
    onAuthenticate: () -> Unit,
    isLoggedIn: Boolean,
    navController: NavController,
    isOnline: Boolean

) {
    ProvideWindowInsets {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthenticationTitle(
                authenticationMode = authenticationMode
            )
            EmailInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(9.dp),
                email = email ?: "",
                onEmailChanged = onEmailChanged
            )
            if (authenticationMode == AuthenticationMode.SIGN_UP) {
                UsernameInput(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(9.dp),
                    username = username ?: "",
                    onUsernameChanged = onUsernameChanged
                )
            }

            PasswordInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                password = password ?: "",
                onPasswordChanged = onPasswordChanged
            )
            val mainScope = CoroutineScope(Dispatchers.Main)

            Button(
                onClick = {
//                    mainScope.launch {
                        onAuthenticate()
//                        if (isLoggedIn) {
//                            navController.navigate("main")
//                        }
//                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    stringResource(
                        if (authenticationMode == AuthenticationMode.SIGN_IN) {
                            R.string.label_sign_in_to_account
                        } else {
                            R.string.label_sign_up_for_account
                        }
                    )
                )
            }
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.padding(top = 100.dp)
            )
            if(isOnline){
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(
                        if (authenticationMode == AuthenticationMode.SIGN_IN) {
                            R.string.question_to_login_mode
                        } else {
                            R.string.question_to_register_mode
                        }
                    )
                )
                TextButton(onClick = onToggleMode) {
                    Text(
                        text = stringResource(
                            if (authenticationMode == AuthenticationMode.SIGN_IN) {
                                R.string.label_sign_up_for_account
                            } else {
                                R.string.label_sign_in_to_account
                            }
                        )
                    )
                }
            }}
        }
    }
//    if (isLoggedIn) {
//        navController.navigate("main")
//    }
}

@Composable
fun AuthenticationTitle(
    modifier: Modifier = Modifier,
    authenticationMode: AuthenticationMode
) {
    Text(
        text = stringResource(
            if (authenticationMode == AuthenticationMode.SIGN_IN) {
                R.string.label_sign_in_to_account
            } else {
                R.string.label_sign_up_for_account
            }
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    email: String?,
    onEmailChanged: (email: String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = email ?: "",
        onValueChange = { email ->
            onEmailChanged(email)
        },
        singleLine = true,
        placeholder = { Text(stringResource(R.string.placeholder_email)) }

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsernameInput(
    modifier: Modifier = Modifier,
    username: String?,
    onUsernameChanged: (email: String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = username ?: "",
        onValueChange = { username ->
            onUsernameChanged(username)
        },
        singleLine = true,
        placeholder = { Text(stringResource(R.string.placeholder_username)) }

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordInput(
    modifier: Modifier = Modifier,
    password: String,
    onPasswordChanged: (email: String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = password,
        onValueChange = {
            onPasswordChanged(it)
        },
        singleLine = true,
        placeholder = { Text(stringResource(R.string.placeholder_password)) },
        visualTransformation = PasswordVisualTransformation()
    )
}


