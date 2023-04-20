package com.begdev.bigbid.ui.auth

//import com.google.accompanist.insets.navigationBarsWithImePadding

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.begdev.bigbid.R
import com.begdev.bigbid.ui.theme.BigBidTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun AuthenticationScreen(
    viewModel: AuthenticationViewModel = hiltViewModel()
) {
    Surface(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        BigBidTheme {
            AuthenticationContent(
                modifier = Modifier.fillMaxSize(),
                authenticationState = viewModel.uiState.collectAsState().value,
                handleEvent = viewModel::handleEvent
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

    ) {
    AuthenticationForm(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
            .navigationBarsWithImePadding(),
        authenticationMode = authenticationState.authenticationMode,
        email = authenticationState.email,
        password = authenticationState.password,
        onEmailChanged = { handleEvent(AuthenticationEvent.EmailChanged(it)) },
        onPasswordChanged = { handleEvent(AuthenticationEvent.PasswordChanged(it)) },
        onToggleMode = { handleEvent(AuthenticationEvent.ToggleAuthenticationMode) },
        onAuthenticate = { handleEvent(AuthenticationEvent.Authenticate) }
    )
}


@Composable
fun AuthenticationForm(
    modifier: Modifier,
    authenticationMode: AuthenticationMode,
    email: String,
    password: String,
    onEmailChanged: (email: String) -> Unit,
    onPasswordChanged: (password: String) -> Unit,
    onToggleMode: () -> Unit,
    onAuthenticate: () -> Unit


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
            PasswordInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                password = password ?: "",
                onPasswordChanged = onPasswordChanged
            )
            Button(
                onClick = onAuthenticate,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Login")
            }
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.padding(top = 100.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Don`t have an account?")
                TextButton(onClick = onToggleMode) {
                    Text("SIGN UP")
                }
            }
        }
    }
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
//        label = {
//            Text(text = stringResource(id = R.string.label_password)
//            )
//        }
//                leadingIcon = {
//            Icon(
//                imageVector = Icons.Default.Lock,
//                contentDescription = null
//            )
//        }
    )
}