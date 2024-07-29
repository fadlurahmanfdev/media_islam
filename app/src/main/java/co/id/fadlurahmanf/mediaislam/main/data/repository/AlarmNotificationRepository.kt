package co.id.fadlurahmanf.mediaislam.main.data.repository

import android.app.Notification
import android.app.PendingIntent
import android.content.Context

interface AlarmNotificationRepository {
    fun getNotification(
        context: Context,
        fullScreenIntent: PendingIntent,
        snoozeIntent: PendingIntent,
        dismissIntent: PendingIntent,
    ): Notification
}