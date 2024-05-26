package co.id.fadlurahmanf.mediaislam.quran.data.dto.model

data class AudioQariModel(
    val qariId: String,
    val qariName: String,
    val qariAudio: String,
    val qariImage: String? = null,
    val qariImageKey: String? = null,
)
