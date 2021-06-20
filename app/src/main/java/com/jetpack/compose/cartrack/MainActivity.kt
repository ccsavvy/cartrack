package com.jetpack.compose.cartrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jetpack.compose.cartrack.ui.theme.CartrackTheme
import com.jetpack.compose.cartrack.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CartrackTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    ) {

                        val mainViewModel: MainViewModel =
                            viewModel(modelClass = MainViewModel::class.java)

                        // username
                        val username: String by mainViewModel.username.observeAsState(initial = "")
                        var usernameErrorState by remember { mutableStateOf(false) }
                        var userNameErrorMessage by remember { mutableStateOf("") }

                        // password
                        val password: String by mainViewModel.password.observeAsState(initial = "")
                        var passwordErrorState by remember { mutableStateOf(false) }
                        var passwordErrorMessage by remember { mutableStateOf("") }

                        // remember me
                        val isChecked = remember { mutableStateOf(false) }

                        UsernameInputField(
                            value = username,
                            onValueChange = {
                                mainViewModel.onUserNameChange(it)
                                when (mainViewModel.validateUsername(username)) {
                                    is MainViewModel.LoginUsernameException.EmptyUsernameException -> {
                                        usernameErrorState = true
                                        userNameErrorMessage =
                                            resources.getString(R.string.error_username_empty)
                                    }
                                    is MainViewModel.LoginUsernameException.InvalidUsernameException -> {
                                        usernameErrorState = true
                                        userNameErrorMessage =
                                            resources.getString(R.string.error_invalid_credentials)
                                    }
                                    null -> {
                                        usernameErrorState = false
                                        userNameErrorMessage = ""
                                    }
                                }
                            },
                            modifier = Modifier.padding(8.dp),
                            errorMessage = userNameErrorMessage,
                            errorState = usernameErrorState
                        )

                        PasswordInputField(
                            value = password,
                            onValueChange = {
                                mainViewModel.onPasswordChange(it)
                                when (mainViewModel.validatePassword(password)) {
                                    is MainViewModel.LoginPasswordException.EmptyPasswordException -> {
                                        passwordErrorState = true
                                        passwordErrorMessage =
                                            resources.getString(R.string.error_password_empty)
                                    }
                                    null -> {
                                        passwordErrorState = false
                                        passwordErrorMessage = ""
                                    }
                                }
                            },
                            label = { Text("Foo") },
                            modifier = Modifier.padding(8.dp),
                            errorMessage = passwordErrorMessage,
                            errorState = passwordErrorState
                        )
                        RememberMeCheckBox(isChecked = isChecked)
                        LoginButton()
                    }
                }
            }
        }
    }
}

@Composable
fun UsernameInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String,
    errorState: Boolean,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    placeholder: @Composable (() -> Unit)? = null,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = if (errorState) errorMessage
                else stringResource(id = R.string.username)
            )
        },
        modifier = modifier,
        isError = errorState
    )
}

@Composable
fun PasswordInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String,
    errorState: Boolean,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
) {

    var passwordVisibility by remember { mutableStateOf(false) }
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = if (errorState) errorMessage
                else stringResource(id = R.string.password)
            )
        },
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisibility)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            IconButton(onClick = {
                passwordVisibility = !passwordVisibility
            }) {
                Icon(imageVector = image, "")
            }
        },
        modifier = modifier,
        isError = errorState
    )
}

@Composable
fun RememberMeCheckBox(isChecked: MutableState<Boolean>) {
    Row(modifier = Modifier.padding(8.dp)) {
        Checkbox(
            checked = isChecked.value,
            onCheckedChange = { isChecked.value = it }
        )
        Text(text = stringResource(id = R.string.rememberme))
    }
}

@Composable
fun LoginButton() {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.padding(8.dp)
    ) {
        Button(
            onClick = { /* Do something! */ },
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = Color.Blue,
                contentColor = Color.White
            )
        ) {
            Text(stringResource(id = R.string.login))
        }
    }
}

@Preview(widthDp = 640, heightDp = 620, showBackground = true)
@Composable
fun DefaultPreview() {
    CartrackTheme {
        Column(
            Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            // username
            var usernameValue by remember { mutableStateOf("") }
            var usernameErrorState by remember { mutableStateOf(false) }
            var usernameErrorMessage by remember { mutableStateOf("") }

            // password
            var passwordValue by remember { mutableStateOf("") }
            var passwordErrorState by remember { mutableStateOf(false) }
            var passwordErrorMessage by remember { mutableStateOf("") }

            // remember me
            val isChecked = remember { mutableStateOf(false) }

            UsernameInputField(
                value = "",
                onValueChange = { usernameValue = it },
                modifier = Modifier.padding(8.dp),
                errorMessage = usernameErrorMessage,
                errorState = usernameErrorState
            )
            PasswordInputField(
                value = "",
                onValueChange = { passwordValue = it },
                label = { Text("Foo") },
                modifier = Modifier.padding(8.dp),
                errorMessage = passwordErrorMessage,
                errorState = passwordErrorState
            )
            RememberMeCheckBox(isChecked = isChecked)
            LoginButton()
        }
    }
}