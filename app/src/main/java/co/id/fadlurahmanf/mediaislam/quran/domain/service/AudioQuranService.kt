package co.id.fadlurahmanf.mediaislam.quran.domain.service

import android.app.Notification
import android.support.v4.media.session.MediaSessionCompat
import co.id.fadlurahmanf.mediaislam.quran.data.repository.QuranNotificationRepository
import co.id.fadlurahmanf.mediaislam.quran.data.repository.QuranNotificationRepositoryImpl
import co.id.fadlurahmanfdev.kotlin_feature_media_player.data.state.AudioNotificationState
import co.id.fadlurahmanfdev.kotlin_feature_media_player.data.state.MusicPlayerState
import co.id.fadlurahmanfdev.kotlin_feature_media_player.domain.service.FeatureMusicPlayerService

class AudioQuranService : FeatureMusicPlayerService() {
    private lateinit var quranNotificationRepository: QuranNotificationRepository
    override fun onCreate() {
        super.onCreate()
        quranNotificationRepository = QuranNotificationRepositoryImpl(
            this
        )
    }

    override fun onIdleAudioNotification(
        notificationId: Int,
        title: String,
        artist: String,
        mediaSession: MediaSessionCompat
    ): Notification {
        return quranNotificationRepository.getMediaNotification(
            applicationContext,
            notificationId = notificationId,
            title = title,
            artist = artist,
            currentAudioState = AudioNotificationState.IDLE,
            position = 0,
            duration = 0,
            mediaSession = mediaSession,
        )
    }

    override fun onUpdateAudioStateNotification(
        notificationId: Int,
        title: String,
        artist: String,
        position: Long,
        duration: Long,
        musicPlayerState: MusicPlayerState
    ) {
        if (mediaSession != null) {
            when (musicPlayerState) {
                MusicPlayerState.PLAYING -> {
                    quranNotificationRepository.updateMediaNotification(
                        applicationContext,
                        notificationId = notificationId,
                        currentAudioState = AudioNotificationState.PLAYING,
                        title = title,
                        artist = artist,
                        position = position,
                        duration = duration,
                        mediaSession = mediaSession!!
                    )
                }

                MusicPlayerState.PAUSED -> {
                    quranNotificationRepository.updateMediaNotification(
                        applicationContext,
                        notificationId = notificationId,
                        currentAudioState = AudioNotificationState.PAUSED,
                        title = title,
                        artist = artist,
                        position = position,
                        duration = duration,
                        mediaSession = mediaSession!!
                    )
                }

                MusicPlayerState.ENDED -> {
                    quranNotificationRepository.updateMediaNotification(
                        applicationContext,
                        notificationId = notificationId,
                        currentAudioState = AudioNotificationState.ENDED,
                        title = title,
                        artist = artist,
                        position = position,
                        duration = duration,
                        mediaSession = mediaSession!!
                    )
                }

                else -> {

                }
            }
        }
    }
}