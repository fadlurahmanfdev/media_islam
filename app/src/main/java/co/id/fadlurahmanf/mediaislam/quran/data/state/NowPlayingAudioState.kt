package co.id.fadlurahmanf.mediaislam.quran.data.state
sealed class NowPlayingAudioState {
    data object IDLE : NowPlayingAudioState()
    data class SUCCESS(
        val url: String,
        val qariId: String,
        val qariName: String,
        val qariImage: String? = null,
        val qariImageKey: String? = null,
    ) : NowPlayingAudioState()
}