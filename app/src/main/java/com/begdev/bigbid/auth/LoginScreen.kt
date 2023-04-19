package com.begdev.bigbid.auth

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.begdev.bigbid.auth.LoginViewModel
import com.begdev.bigbid.nav_utils.Destination
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel()
) {

//    Surface(modifier = Modifier.fillMaxSize()) {
//
////        val loginViewModel: LoginViewModel = hiltViewModel()
//
//        var email by rememberSaveable(stateSaver = TextFieldValue.Saver) {
//            mutableStateOf(TextFieldValue(selection = TextRange(0, 7)))
//        }
//        var password by rememberSaveable(stateSaver = TextFieldValue.Saver) {
//            mutableStateOf(TextFieldValue(selection = TextRange(0, 7)))
//        }
//        var repeatPassword by rememberSaveable(stateSaver = TextFieldValue.Saver) {
//            mutableStateOf(TextFieldValue(selection = TextRange(0, 7)))
//        }
//        Column(
//            modifier = Modifier.padding(30.dp),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            OutlinedTextField(value = email,
//                modifier = Modifier.fillMaxWidth(),
//                onValueChange = { email = it },
//                label = { Text("E-Mail") })
//            OutlinedTextField(modifier = Modifier.fillMaxWidth(),
//
//                value = password, onValueChange = { password = it }, label = { Text("Password") })
//            Button(
//                onClick = { Log.d(ContentValues.TAG, "authColumn: button pressed") },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text(text = "Login")
//            }
//            TextButton(onClick = { loginViewModel.onNavigateToRegisterBtnClicked() }) { Text("Register") }
//            OutlinedTextField(modifier = Modifier.fillMaxWidth(),
//                value = repeatPassword,
//                singleLine = true,
//                onValueChange = { repeatPassword = it },
//                label = { Text("Repeat Password") })
//            Button(
//                onClick = { Log.d(ContentValues.TAG, "authColumn: button pressed") },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text(text = "Register")
//            }
//}
//}
}

//@Preview(name = "LoginScreenPreview", showBackground = true)
//@Composable
//fun loginScreenPreview(){
////    val loginViewModel: LoginViewModel = hiltViewModel()
//    LoginScreen(loginViewModel = null)
////    Destination.LoginScreen()
//}
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun loginScreenContent() {
    Surface(modifier = Modifier.fillMaxSize()) {

//        val loginViewModel: LoginViewModel = hiltViewModel()

        var email by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(selection = TextRange(0, 7)))
        }
        var password by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(selection = TextRange(0, 7)))
        }
        var repeatPassword by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(selection = TextRange(0, 7)))
        }

        Column(
            modifier = Modifier.padding(30.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(value = email,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { email = it },
                label = { Text("E-Mail") })
            OutlinedTextField(modifier = Modifier.fillMaxWidth(),

                value = password, onValueChange = { password = it }, label = { Text("Password") })
            Button(
                onClick = { Log.d(ContentValues.TAG, "authColumn: button pressed") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Login")
            }
            TextButton(onClick = { loginViewModel.onNavigateToRegisterBtnClicked() }) { Text("Register") }
        }
    }
}
