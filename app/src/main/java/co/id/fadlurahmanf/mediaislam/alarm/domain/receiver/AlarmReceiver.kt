package co.id.fadlurahmanf.mediaislam.alarm.domain.receiver

import android.content.Context
import android.content.Intent
import android.os.Bundle
import co.id.fadlurahmanf.mediaislam.alarm.domain.service.AlarmService
import co.id.fadlurahmanf.mediaislam.core.constant.AppConstant
import co.id.fadlurahmanfdev.kotlin_feature_alarm.domain.receiver.FeatureAlarmReceiver
import co.id.fadlurahmanfdev.kotlin_feature_alarm.domain.service.FeatureAlarmService
import java.util.Date

class AlarmReceiver : FeatureAlarmReceiver() {

    companion object {
        const val PARAM_PRAYER_TIME_TYPE = "PARAM_PRAYER_TIME_TYPE"
    }

    override fun onReceiveActionSetAlarm(context: Context, intent: Intent, extras: Bundle?) {
        FeatureAlarmService.startPlayingAlarm(context, AppConstant.NOTIFICATION_ID_ALARM, extras, AlarmService::class.java)
    }

    override fun onReceiveActionDismissAlarm(context: Context, intent: Intent) {
        FeatureAlarmService.stopPlayingAlarm(
            context,
            AlarmService::class.java
        )
    }

    override fun onReceiveActionSnoozeAlarm(context: Context, intent: Intent, snoozeTime: Date) {}
}