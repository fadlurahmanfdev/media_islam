package co.id.fadlurahmanf.mediaislam.main.domain.receiver

import android.content.Context
import android.content.Intent
import co.id.fadlurahmanf.mediaislam.main.domain.service.AlarmService
import co.id.fadlurahmanfdev.kotlin_feature_alarm.domain.receiver.FeatureAlarmReceiver
import co.id.fadlurahmanfdev.kotlin_feature_alarm.domain.service.FeatureAlarmService
import java.util.Date

class AlarmReceiver : FeatureAlarmReceiver() {

    override fun onReceiveActionSetAlarm(context: Context, intent: Intent) {
        FeatureAlarmService.startPlayingAlarm(context, 2000, AlarmService::class.java)
    }

    override fun onReceiveActionDismissAlarm(context: Context, intent: Intent) {
        FeatureAlarmService.stopPlayingAlarm(
            context,
            notificationId = 2000,
            AlarmService::class.java
        )
    }

    override fun onReceiveActionSnoozeAlarm(context: Context, intent: Intent, snoozeTime: Date) {
        FeatureAlarmService.stopPlayingAlarm(
            context,
            notificationId = 2000,
            AlarmService::class.java
        )
    }
}