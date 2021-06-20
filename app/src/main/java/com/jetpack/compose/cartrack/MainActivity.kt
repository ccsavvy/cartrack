package com.jetpack.compose.cartrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.jetpack.compose.cartrack.ui.theme.CartrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CartrackTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun UsernameInputField(
    value: String,
    onInput: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    placeholder: @Composable (() -> Unit)? = null,
) {
    var textValue by remember { mutableStateOf(value) }
    TextField(
        value = textValue,
        onValueChange = { onInput(it) },
        label = { Text(stringResource(id = R.string.username)) },
        modifier = modifier
    )
}

@Composable
fun PasswordInputField(
    value: TextFieldValue,
    onInput: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var textValue by remember { mutableStateOf(value) }
    TextField(
        value = textValue,
        onValueChange = { onInput(it) },
        label = { Text(stringResource(id = R.string.password)) },
        modifier = modifier
    )
}
@Composable
fun DefaultPreview() {
    CartrackTheme {
        Greeting("Android")
    }
}