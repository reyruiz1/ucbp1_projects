package edu.ucb.project.Exam.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import edu.ucb.project.Exam.domain.model.CharacterModel
import edu.ucb.project.Exam.presentation.viewmodel.PeopleEffect
import edu.ucb.project.Exam.presentation.viewmodel.PeopleEvent
import edu.ucb.project.Exam.presentation.viewmodel.PeopleState
import edu.ucb.project.Exam.presentation.viewmodel.PeopleViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PeopleScreen(
    navController: NavHostController,
    viewModel: PeopleViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.emitEvent(PeopleEvent.OnLoad)
        viewModel.effect.collect { effect ->
            when (effect) {
                is PeopleEffect.ShowToast -> {
                    //Todo
                }

                is PeopleEffect.NavigateToDetail -> {
                    //Todo
                }
            }
        }
    }

    PeopleContent(
        state = state.value,
        onCharacterClick = { viewModel.emitEvent(PeopleEvent.OnShowDetail(it)) },
        onLoadNextPage = { viewModel.emitEvent(PeopleEvent.OnLoadNextPage) }
    )
}

@Composable
private fun PeopleContent(
    state: PeopleState,
    onCharacterClick: (CharacterModel) -> Unit,
    onLoadNextPage: () -> Unit
) {
    val listState = rememberLazyListState()

    // cuando el usuario llega a los ultimos 3 elementos se pide la siguiente pagina
    LaunchedEffect(listState) {
        snapshotFlow {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val total = listState.layoutInfo.totalItemsCount
            total > 1 && lastVisible >= total - 3
        }.collect { isNearEnd ->
            if (isNearEnd) onLoadNextPage()
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(state.characters) { character ->
            CharacterItem(character = character, onClick = { onCharacterClick(character) })
        }

        item {
            PeopleFooter(state = state, onLoadNextPage = onLoadNextPage)
        }
    }
}

@Composable
private fun CharacterItem(
    character: CharacterModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = character.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text("Altura: ${character.height}")
            Text("Peso: ${character.mass}")
            Text("Color de cabello: ${character.hairColor}")
            Text("Color de piel: ${character.skinColor}")
            Text("Color de ojos: ${character.eyeColor}")
            Text("Género: ${character.gender}")
        }
    }
}

@Composable
private fun PeopleFooter(
    state: PeopleState,
    onLoadNextPage: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            state.isLoading -> CircularProgressIndicator()

            state.error != null -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Error: ${state.error}")
                Button(onClick = onLoadNextPage) {
                    Text("Reintentar")
                }
            }

            state.isLastPage -> Text("No hay más personajes")

            else -> Button(onClick = onLoadNextPage) {
                Text("Siguiente")
            }
        }
    }
}

@Preview
@Composable
private fun PeopleScreenPreview() {
    MaterialTheme {
        PeopleContent(
            state = PeopleState(
                characters = listOf(
                    CharacterModel(id = "", name = "Luke Skywalker", height = "172", mass = "77", hairColor = "blond", skinColor = "fair", eyeColor = "blue", gender = "male"),
                    CharacterModel(id = "", name = "C-3PO", height = "167", mass = "75", hairColor = "n/a", skinColor = "gold", eyeColor = "yellow", gender = "n/a"),
                    CharacterModel(id = "", name = "R2-D2", height = "96", mass = "32", hairColor = "n/a", skinColor = "white, blue", eyeColor = "red", gender = "n/a")
                ),
                page = 1
            ),
            onCharacterClick = {},
            onLoadNextPage = {}
        )
    }
}
