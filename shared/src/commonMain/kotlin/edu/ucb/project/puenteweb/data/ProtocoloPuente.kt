package edu.ucb.project.puenteweb.data

import edu.ucb.project.puenteweb.domain.model.MensajePuente
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.encodeToJsonElement

object ProtocoloPuente {

    val json = Json { ignoreUnknownKeys = true }

    fun leer(texto: String): MensajePuente? {
        return try {
            json.decodeFromString(MensajePuente.serializer(), texto)
        } catch (e: Exception) {
            null
        }
    }

    fun escribir(carta: MensajePuente): String = json.encodeToString(carta)

    inline fun <reified T> empaquetar(clave: String, valor: T): JsonObject {
        return buildJsonObject { put(clave, json.encodeToJsonElement(valor)) }
    }
}

fun MensajePuente.texto(clave: String): String? {
    return (payload[clave] as? JsonPrimitive)?.contentOrNull
}
