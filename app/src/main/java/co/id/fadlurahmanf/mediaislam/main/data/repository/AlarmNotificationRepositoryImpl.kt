package co.id.fadlurahmanf.mediaislam.main.data.repository

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import androidx.annotation.DrawableRes
import co.id.fadlurahmanf.mediaislam.BuildConfig
import co.id.fadlurahmanf.mediaislam.R
import co.id.fadlurahmanfdev.kotlin_feature_alarm.data.dto.FeatureAlarmNotificationAction
import co.id.fadlurahmanfdev.kotlin_feature_alarm.data.repository.BaseAlarmNotificationRepository

class AlarmNotificationRepositoryImpl : BaseAlarmNotificationRepository(),
    AlarmNotificationRepository {
    companion object {
        const val ALARM_NOTIFICATION_CHANNEL_ID = "ALARM_NOTIFICATION"
        const val ALARM_NOTIFICATION_CHANNEL_NAME = "Alarm"
        const val ALARM_NOTIFICATION_DESCRIPTION = "Alarm"

        @DrawableRes
        val ICON_NOTIFICATION = R.drawable.il_media_islam
    }

    override fun getNotification(
        context: Context,
        title: String,
        text: String,
        fullScreenIntent: PendingIntent,
        dismissIntent: PendingIntent
    ): Notification {
        createNotificationChannel(
            context = context,
            id = ALARM_NOTIFICATION_CHANNEL_ID,
            description = ALARM_NOTIFICATION_DESCRIPTION,
            name = ALARM_NOTIFICATION_CHANNEL_NAME,
            sound = null
        )

        return getFullScreenNotification(
            context,
            channelId = ALARM_NOTIFICATION_CHANNEL_ID,
            icon = ICON_NOTIFICATION,
            text = text,
            title = title,
            fullScreenIntent = fullScreenIntent,
            actions = listOf(
                FeatureAlarmNotificationAction(
                    icon = R.drawable.round_alarm_off_24,
                    packageName = BuildConfig.APPLICATION_ID,
                    textAction = "Matikan",
                    action = dismissIntent
                )
            ),
        )

    }
}