package edu.ucb.project.userSearch.Presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.koin.compose.viewmodel.koinViewModel
import edu.ucb.project.navigation.NavRoute
import edu.ucb.project.userSearch.Domain.model.UserInfoModel
import edu.ucb.project.userSearch.Presentation.viewModel.UserSearchEffect
import edu.ucb.project.userSearch.Presentation.viewModel.UserSearchEvent
import edu.ucb.project.userSearch.Presentation.viewModel.UserSearchState
import edu.ucb.project.userSearch.Presentation.viewModel.UserSearchViewModel

@Composable
fun UserSearchScreen(
    navController: NavHostController,
    viewModel: UserSearchViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is UserSearchEffect.ShowToast -> {
                    //Todo
                }

                UserSearchEffect.NavigateToBack -> {
                    //Todo
                }
            }
        }
    }

    UserSearchContent(
        state = state.value,
        onAliasChange = { viewModel.emitEvent(UserSearchEvent.OnAliasChange(it)) },
        onSubmit = { viewModel.emitEvent(UserSearchEvent.OnSubmit) },
        onGoToHome = { navController.navigate(NavRoute.Login) }
    )
}

@Composable
private fun UserSearchContent(
    state: UserSearchState,
    onAliasChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onGoToHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        TextField(
            value = state.alias,
            onValueChange = onAliasChange,
            label = { Text("Usuario de GitHub") }
        )
        Button(onClick = onSubmit) {
            Text("Buscar")
        }
        Button(onClick = onGoToHome) {
            Text("go to home")
        }

        state.error?.let {
            Text("Error: $it")
        }

        state.user?.let { user ->
            Text("avatar: ${user.avatarUrl}")
            Text("login: ${user.login}")
            Text("id: ${user.id}")
            Text("type: ${user.type}")
            Text("name: ${user.name ?: "—"}")
            Text("company: ${user.company ?: "—"}")
            Text("location: ${user.location ?: "—"}")
            Text("email: ${user.email ?: "—"}")
            Text("bio: ${user.bio ?: "—"}")
            Text("public repos: ${user.publicRepos}")
            Text("public gists: ${user.publicGists}")
            Text("followers: ${user.followers}")
            Text("following: ${user.following}")
            Text("created at: ${user.createdAt}")
            Text("updated at: ${user.updatedAt}")
            Text("html url: ${user.htmlUrl}")
        }
    }
}

@Preview
@Composable
private fun UserSearchScreenPreview() {
    MaterialTheme {
        UserSearchContent(
            state = UserSearchState(
                alias = "reyruiz1",
                user = UserInfoModel(
                    login = "reyruiz1",
                    id = 143035315L,
                    avatarUrl = "https://avatars.githubusercontent.com/u/143035315?v=4",
                    htmlUrl = "https://github.com/reyruiz1",
                    type = "User",
                    publicRepos = 26,
                    publicGists = 0,
                    followers = 0,
                    following = 0,
                    createdAt = "2023-08-23T20:33:58Z",
                    updatedAt = "2026-08-17T22:46:46Z"
                )
            ),
            onAliasChange = {},
            onSubmit = {},
            onGoToHome = {}
        )
    }
}
