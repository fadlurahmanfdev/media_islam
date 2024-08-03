package co.id.fadlurahmanf.mediaislam.alarm.domain.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import co.id.fadlurahmanf.mediaislam.BuildConfig
import co.id.fadlurahmanf.mediaislam.R
import co.id.fadlurahmanf.mediaislam.alarm.domain.receiver.AlarmReceiver
import co.id.fadlurahmanf.mediaislam.alarm.presentation.AlarmNotificationActivity
import co.id.fadlurahmanf.mediaislam.core.enums.PrayerTimeType
import co.id.fadlurahmanf.mediaislam.main.data.repository.AlarmNotificationRepository
import co.id.fadlurahmanf.mediaislam.main.data.repository.AlarmNotificationRepositoryImpl
import co.id.fadlurahmanfdev.kotlin_feature_alarm.domain.receiver.FeatureAlarmReceiver
import co.id.fadlurahmanfdev.kotlin_feature_alarm.domain.service.FeatureAlarmService

class AlarmService : FeatureAlarmService() {
    private lateinit var alarmNotificationRepository: AlarmNotificationRepository

    override var defaultUriRingtone: Uri =
        Uri.parse("android.resource://${BuildConfig.APPLICATION_ID}/" + R.raw.adhan)

    override fun onFeatureAlarmServiceCreate() {
        alarmNotificationRepository = AlarmNotificationRepositoryImpl()
    }

    override fun onAlarmNotification(context: Context, bundle: Bundle?): Notification {
        val prayerTimeTypeString = bundle?.getString(AlarmReceiver.PARAM_PRAYER_TIME_TYPE)
        val prayerTimeType: PrayerTimeType = try {
            PrayerTimeType.valueOf(prayerTimeTypeString ?: "")
        } catch (e: Throwable) {
            PrayerTimeType.FAJR
        }

        val titleNotification = when (prayerTimeType) {
            PrayerTimeType.FAJR -> "Waktu Sholat Subuh"
            PrayerTimeType.DHUHR -> "Waktu Sholat Dzuhur"
            PrayerTimeType.ASR -> "Waktu Sholat Ashar"
            PrayerTimeType.MAGHRIB -> "Waktu Sholat Maghrib"
            PrayerTimeType.ISHA -> "Waktu Sholat Isya"
        }

        val textNotification = when (prayerTimeType) {
            PrayerTimeType.FAJR -> "Sudah waktunya sholat Subuh. Mulailah hari Anda dengan berkah."
            PrayerTimeType.DHUHR -> "Sudah waktunya sholat Dzuhur. Berhenti sejenak dan berdoa kepada Allah."
            PrayerTimeType.ASR -> "Sudah waktunya sholat Ashar. Luangkan waktu untuk berdoa."
            PrayerTimeType.MAGHRIB -> "Sudah waktunya sholat Maghrib. Akhiri hari Anda dengan rasa syukur."
            PrayerTimeType.ISHA -> "Sudah waktunya sholat Isya. Selesaikan malam Anda dengan kedamaian."
        }

        val fullScreenIntent = Intent(this, AlarmNotificationActivity::class.java)
        val fullScreenPendingIntent =
            PendingIntent.getActivity(context, 0, fullScreenIntent, getFlagPendingIntent())
        val dismissPendingIntent =
            FeatureAlarmReceiver.getPendingIntentDismissAlarm(context, 1, AlarmReceiver::class.java)
        return alarmNotificationRepository.getNotification(
            context = context,
            title = titleNotification,
            text = textNotification,
            fullScreenIntent = fullScreenPendingIntent,
            dismissIntent = dismissPendingIntent
        )
    }

    override fun getRingtone(bundle: Bundle?): Uri {
        val prayerTimeTypeString = bundle?.getString(AlarmReceiver.PARAM_PRAYER_TIME_TYPE)
        val prayerTimeType: PrayerTimeType = try {
            PrayerTimeType.valueOf(prayerTimeTypeString ?: "")
        } catch (e: Throwable) {
            PrayerTimeType.FAJR
        }

        return when (prayerTimeType) {
            PrayerTimeType.FAJR -> {
                Uri.parse("android.resource://${BuildConfig.APPLICATION_ID}/" + R.raw.adhan_fajr)
            }

            else -> {
                Uri.parse("android.resource://${BuildConfig.APPLICATION_ID}/" + R.raw.adhan)
            }
        }
    }

    private fun getFlagPendingIntent(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
    }
}