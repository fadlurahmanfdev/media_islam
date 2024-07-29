package co.id.fadlurahmanf.mediaislam.quran.data.repository

import android.app.Notification
import android.content.Context
import android.support.v4.media.session.MediaSessionCompat
import co.id.fadlurahmanf.mediaislam.R
import co.id.fadlurahmanf.mediaislam.quran.domain.receiver.AudioQuranReceiver
import co.id.fadlurahmanfdev.kotlin_feature_media_player.data.dto.model.MediaNotificationActionModel
import co.id.fadlurahmanfdev.kotlin_feature_media_player.data.repository.BaseMediaNotificationRepository
import co.id.fadlurahmanfdev.kotlin_feature_media_player.data.state.AudioNotificationState
import co.id.fadlurahmanfdev.kotlin_feature_media_player.domain.manager.FeatureMusicPlayerManager

class QuranNotificationRepositoryImpl(context: Context) : BaseMediaNotificationRepository(context),
    QuranNotificationRepository {

    companion object {
        const val QURAN_AUDIO_CHANNEL_ID = "QURAN_AUDIO"
    }

    override fun createAudioQuranChannel() {
        createMediaChannel(
            channelId = QURAN_AUDIO_CHANNEL_ID,
            channelName = "Audio Quran",
            channelDescription = "Audio Quran",
        )
    }

    override fun getMediaNotification(
        context: Context,
        notificationId: Int,
        currentAudioState: AudioNotificationState,
        title: String,
        artist: String,
        position: Long,
        duration: Long,
        mediaSession: MediaSessionCompat,
    ): Notification {
        val actions = arrayListOf<MediaNotificationActionModel>().apply {
            if (currentAudioState != AudioNotificationState.IDLE) {
                add(
                    MediaNotificationActionModel(
                        icon = R.drawable.round_skip_previous_24,
                        title = "Previous",
                        pendingIntent = FeatureMusicPlayerManager.getPreviousPendingIntent(
                            context,
                            notificationId = notificationId,
                            AudioQuranReceiver::class.java
                        )
                    )
                )
            }

            if (currentAudioState == AudioNotificationState.PLAYING) {
                add(
                    MediaNotificationActionModel(
                        icon = co.id.fadlurahmanfdev.kotlin_feature_media_player.R.drawable.round_pause_24,
                        title = "Pause",
                        pendingIntent = FeatureMusicPlayerManager.getPausePendingIntent(
                            context,
                            notificationId = notificationId,
                            AudioQuranReceiver::class.java
                        )
                    )
                )
            } else if (currentAudioState == AudioNotificationState.PAUSED) {
                add(
                    MediaNotificationActionModel(
                        icon = co.id.fadlurahmanfdev.kotlin_feature_media_player.R.drawable.round_play_arrow_24,
                        title = "Resume",
                        pendingIntent = FeatureMusicPlayerManager.getResumePendingIntent(
                            context,
                            notificationId = notificationId,
                            AudioQuranReceiver::class.java
                        )
                    )
                )
            } else if (currentAudioState == AudioNotificationState.ENDED) {
                add(
                    MediaNotificationActionModel(
                        icon = co.id.fadlurahmanfdev.kotlin_feature_media_player.R.drawable.round_play_arrow_24,
                        title = "Replay",
                        pendingIntent = FeatureMusicPlayerManager.getResumePendingIntent(
                            context,
                            notificationId = notificationId,
                            AudioQuranReceiver::class.java
                        )
                    )
                )
            }

            if (currentAudioState != AudioNotificationState.IDLE) {
                add(
                    MediaNotificationActionModel(
                        icon = R.drawable.round_skip_next_24,
                        title = "Next",
                        pendingIntent = FeatureMusicPlayerManager.getNextPendingIntent(
                            context,
                            notificationId = notificationId,
                            AudioQuranReceiver::class.java
                        )
                    )
                )
            }
        }
        return getMediaNotification(
            smallIcon = R.drawable.il_headphone,
            channelId = QURAN_AUDIO_CHANNEL_ID,
            currentAudioState = currentAudioState,
            artist = artist,
            title = title,
            position = position,
            duration = duration,
            onSeekToPosition = { positionSeekTo ->
                FeatureMusicPlayerManager.sendBroadcastSeekToPosition(
                    context,
                    position = positionSeekTo,
                    AudioQuranReceiver::class.java
                )
            },
            actions = actions,
            mediaSession = mediaSession,
        )
    }

    override fun updateMediaNotification(
        context: Context,
        notificationId: Int,
        currentAudioState: AudioNotificationState,
        title: String,
        artist: String,
        position: Long,
        duration: Long,
        mediaSession: MediaSessionCompat
    ) {
        val notification = getMediaNotification(
            context,
            notificationId = notificationId,
            currentAudioState = currentAudioState,
            title = title,
            artist = artist,
            position = position,
            duration = duration,
            mediaSession = mediaSession
        )
        showNotification(
            notificationId = notificationId,
            notification = notification,
        )
    }
}