package co.id.fadlurahmanf.mediaislam.quran.data.repository

import android.app.Notification
import android.content.Context
import android.support.v4.media.session.MediaSessionCompat
import co.id.fadlurahmanfdev.kotlin_feature_media_player.data.state.AudioNotificationState

interface QuranNotificationRepository {
    fun createAudioQuranChannel()
    fun getMediaNotification(
        context: Context,
        notificationId: Int,
        currentAudioState: AudioNotificationState,
        title: String,
        artist: String,
        position: Long,
        duration: Long,
        mediaSession: MediaSessionCompat,
    ): Notification

    fun updateMediaNotification(
        context: Context,
        notificationId: Int,
        currentAudioState: AudioNotificationState,
        title: String,
        artist: String,
        position: Long,
        duration: Long,
        mediaSession: MediaSessionCompat,
    )
}