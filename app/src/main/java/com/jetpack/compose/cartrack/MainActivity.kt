package com.jetpack.compose.cartrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jetpack.compose.cartrack.ui.theme.CartrackTheme

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
                        UsernameInputField(
                            value = "",
                            onInput = {},
                            modifier = Modifier.padding(8.dp)
                        )
                        PasswordInputField(
                            value = TextFieldValue(),
                            onInput = {},
                            label = { Text("Foo") },
                            modifier = Modifier.padding(8.dp)
                        )
                        RememberMe()
                    }
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
fun RememberMe() {
    val isChecked = remember { mutableStateOf(false) }
    Row(modifier = Modifier.padding(8.dp)) {
        Checkbox(
            checked = isChecked.value,
            onCheckedChange = { isChecked.value = it }
        )
        Text(text = stringResource(id = R.string.rememberme))
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
            UsernameInputField(value = "", onInput = {}, modifier = Modifier.padding(8.dp))
            PasswordInputField(
                value = TextFieldValue(),
                onInput = {},
                modifier = Modifier.padding(8.dp),
                label = { Text("Foo") }
            )
            RememberMe()
        }
    }
}