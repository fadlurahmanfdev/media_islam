package co.id.fadlurahmanf.mediaislam.main.domain.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import co.id.fadlurahmanf.mediaislam.BuildConfig
import co.id.fadlurahmanf.mediaislam.R
import co.id.fadlurahmanf.mediaislam.main.data.repository.AlarmNotificationRepository
import co.id.fadlurahmanf.mediaislam.main.data.repository.AlarmNotificationRepositoryImpl
import co.id.fadlurahmanf.mediaislam.main.domain.receiver.AlarmReceiver
import co.id.fadlurahmanf.mediaislam.main.presentation.alarm.AlarmNotificationActivity
import co.id.fadlurahmanfdev.kotlin_feature_alarm.domain.receiver.FeatureAlarmReceiver
import co.id.fadlurahmanfdev.kotlin_feature_alarm.domain.service.FeatureAlarmService
import java.util.Calendar

class AlarmService : FeatureAlarmService() {
    private lateinit var alarmNotificationRepository: AlarmNotificationRepository

    override var defaultUriRingtone: Uri =
        Uri.parse("android.resource://${BuildConfig.APPLICATION_ID}/" + R.raw.adhan)

    override fun onFeatureAlarmServiceCreate() {
        alarmNotificationRepository = AlarmNotificationRepositoryImpl()
    }

    override fun onAlarmNotification(context: Context): Notification {
        val fullScreenIntent = Intent(this, AlarmNotificationActivity::class.java)
        val fullScreenPendingIntent =
            PendingIntent.getActivity(context, 0, fullScreenIntent, getFlagPendingIntent())
        val snoozeTime = Calendar.getInstance().apply {
            add(Calendar.SECOND, 10)
        }.time
        val snoozePendingIntent = FeatureAlarmReceiver.getPendingIntentSnoozeAlarm(
            context,
            requestCode = 1,
            date = snoozeTime,
            clazz = AlarmReceiver::class.java
        )
        val dismissPendingIntent =
            FeatureAlarmReceiver.getPendingIntentDismissAlarm(context, 1, AlarmReceiver::class.java)
        return alarmNotificationRepository.getNotification(
            context = context,
            fullScreenIntent = fullScreenPendingIntent,
            snoozeIntent = snoozePendingIntent,
            dismissIntent = dismissPendingIntent
        )
    }

    override fun onStartForegroundService(notificationId: Int) {
        super.onStartForegroundService(notificationId)
    }

    private fun getFlagPendingIntent(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
    }
}