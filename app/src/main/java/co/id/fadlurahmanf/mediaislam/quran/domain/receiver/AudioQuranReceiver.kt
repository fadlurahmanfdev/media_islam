package co.id.fadlurahmanf.mediaislam.quran.domain.receiver

import android.content.Context
import co.id.fadlurahmanf.mediaislam.quran.domain.service.AudioQuranService
import co.id.fadlurahmanfdev.kotlin_feature_media_player.domain.manager.FeatureMusicPlayerManager
import co.id.fadlurahmanfdev.kotlin_feature_media_player.domain.receiver.FeatureMusicPlayerReceiver

class AudioQuranReceiver : FeatureMusicPlayerReceiver() {
    override fun onPauseAudio(context: Context) {
        FeatureMusicPlayerManager.pause(
            context,
            AudioQuranService::class.java
        )
    }

    override fun onResumeAudio(context: Context) {
        FeatureMusicPlayerManager.resume(
            context,
            AudioQuranService::class.java
        )
    }

    override fun onSeekToPosition(context: Context, position: Long) {
        FeatureMusicPlayerManager.seekToPosition(
            context,
            position = position,
            clazz = AudioQuranService::class.java
        )
    }

    override fun onPreviousAudio(context: Context) {
        FeatureMusicPlayerManager.seekToPrevious(
            context,
            AudioQuranService::class.java
        )
    }

    override fun onNextAudio(context: Context) {
        FeatureMusicPlayerManager.seekToNext(
            context,
            AudioQuranService::class.java,
        )
    }
}