package edu.ucb.project.Exam.data.service

import edu.ucb.project.Exam.data.dataSource.PeopleRemoteDataSource
import edu.ucb.project.Exam.data.dto.PeopleDto
import edu.ucb.project.Exam.data.mapper.toDomain
import edu.ucb.project.Exam.domain.model.CharacterModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class PeopleService : PeopleRemoteDataSource {
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

    override suspend fun fetchData(page: Int): Result<List<CharacterModel>> {
        val response = client.get("https://swapi.dev/api/people/?page=$page")
        try {
            return Result.success(response.body<PeopleDto>().result.map { it.toDomain() })
        } catch (e: Exception) {
            throw e
        }
    }
}
