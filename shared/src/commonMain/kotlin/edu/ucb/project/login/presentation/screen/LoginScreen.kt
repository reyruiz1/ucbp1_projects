package edu.ucb.project.login.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import org.koin.compose.viewmodel.koinViewModel
import edu.ucb.project.login.presentation.state.LoginEffects
import edu.ucb.project.login.presentation.state.LoginEvent
import edu.ucb.project.login.presentation.state.LoginState
import edu.ucb.project.login.presentation.state.LoginViewModel
import edu.ucb.project.navigation.NavRoute

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                LoginEffects.NavigateToHome -> {
                    navController.navigate(NavRoute.UserSearch)
                }
                is LoginEffects.ShowToast -> {
                    println("ERROR ${effect.message}")
                }
            }
        }
    }

    LoginContent(
        state = state.value,
        onEmailChange = { viewModel.emitEvent(LoginEvent.EmailChanged(it)) },
        onPasswordChange = { viewModel.emitEvent(LoginEvent.PasswordChanged(it)) },
        onLogin = { viewModel.emitEvent(LoginEvent.LoginPressed) },
        onSearch = { navController.navigate(NavRoute.UserSearch) }
    )
}

@Composable
private fun LoginContent(
    state: LoginState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
    onSearch: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = state.email,
            onValueChange = onEmailChange,
            label = { Text("Email / Usuario") }
        )

        TextField(
            value = state.password,
            onValueChange = onPasswordChange,
            label = { Text("Contraseña") }
        )

        Button(onClick = onLogin) {
            Text("Login")
        }

        Button(onClick = onSearch) {
            Text("search")
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    MaterialTheme {
        LoginContent(
            state = LoginState(email = "demo@correo.com", password = "123456"),
            onEmailChange = {},
            onPasswordChange = {},
            onLogin = {},
            onSearch = {}
        )
    }
}
