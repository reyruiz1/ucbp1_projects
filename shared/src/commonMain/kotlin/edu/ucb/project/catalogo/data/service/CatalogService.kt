package edu.ucb.project.catalogo.data.service

import edu.ucb.project.catalogo.data.dataSource.CatalogRemoteDataSource
import edu.ucb.project.catalogo.data.dto.CatalogDto
import edu.ucb.project.catalogo.data.mapper.toDomain
import edu.ucb.project.catalogo.domain.model.MovieModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class CatalogService : CatalogRemoteDataSource {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    override suspend fun fetchData(): Result<List<MovieModel>> {
        val response = client.get("https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=fa3e844ce31744388e07fa47c7c5d8c3")
        try {
            return Result.success(response.body<CatalogDto>().result.map { it.toDomain() })
        } catch (e: Exception) {
            throw e
        }
    }
}
