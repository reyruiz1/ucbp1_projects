package edu.ucb.project.puenteweb.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
enum class TipoMensaje {
    @SerialName("event")
    EVENT,

    @SerialName("request")
    REQUEST,

    @SerialName("response")
    RESPONSE
}

@Serializable
data class MensajePuente(
    val type: TipoMensaje,
    val action: String,
    val id: String? = null,
    val payload: JsonObject = JsonObject(emptyMap()),
    val ok: Boolean? = null,
    val error: String? = null
)
