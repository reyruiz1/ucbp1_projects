package edu.ucb.project.catalogo.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import edu.ucb.project.catalogo.domain.model.MovieModel
import edu.ucb.project.catalogo.presentation.viewmodel.CatalogEffect
import edu.ucb.project.catalogo.presentation.viewmodel.CatalogEvent
import edu.ucb.project.catalogo.presentation.viewmodel.CatalogState
import edu.ucb.project.catalogo.presentation.viewmodel.CatalogViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CatalogScreen(
    navController: NavHostController,
    viewModel: CatalogViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.emitEvent(CatalogEvent.OnLoad)
        viewModel.effect.collect { effect ->
            when (effect) {
                is CatalogEffect.ShowToast -> {
                    //Todo
                }

                is CatalogEffect.NavigateToDetail -> {
                    //Todo
                }
            }
        }
    }

    CatalogContent(
        state = state.value,
        onMovieClick = { viewModel.emitEvent(CatalogEvent.OnShowDetail(it)) }
    )
}

@Composable
private fun CatalogContent(
    state: CatalogState,
    onMovieClick: (MovieModel) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        state.error?.let {
            Text("Error: $it", modifier = Modifier.align(Alignment.Center).padding(16.dp))
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize()
        ) {
            items(state.movies) { movie ->
                MovieItem(movie = movie, onClick = { onMovieClick(movie) })
            }
        }
    }
}

@Composable
private fun MovieItem(
    movie: MovieModel,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = movie.posterPath,
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
        )
        Text(
            text = movie.title,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun CatalogScreenPreview() {
    MaterialTheme {
        CatalogContent(
            state = CatalogState(
                movies = listOf(
                    MovieModel(id = "", title = "The Super Mario Bros. Movie", posterPath = ""),
                    MovieModel(id = "", title = "Creed III", posterPath = ""),
                    MovieModel(id = "", title = "65", posterPath = "")
                )
            ),
            onMovieClick = {}
        )
    }
}
