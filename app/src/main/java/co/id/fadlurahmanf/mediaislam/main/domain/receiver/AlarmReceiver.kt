package co.id.fadlurahmanf.mediaislam.main.domain.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        fun getPendingIntent(context: Context): PendingIntent {
            val intent = Intent(context, AlarmReceiver::class.java).apply {
                action = "co.id.fadlurahmanfdev.kotlin_feature_alarm.ACTION_SET_ALARM"
            }
            return PendingIntent.getBroadcast(context, 0, intent, getFlagPendingIntent())
        }

        private fun getFlagPendingIntent(): Int {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {

    }
}