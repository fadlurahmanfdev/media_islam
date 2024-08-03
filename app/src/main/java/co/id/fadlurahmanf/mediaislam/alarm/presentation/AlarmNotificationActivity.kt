package co.id.fadlurahmanf.mediaislam.alarm.presentation

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import co.id.fadlurahmanf.mediaislam.R
import co.id.fadlurahmanf.mediaislam.alarm.domain.receiver.AlarmReceiver
import co.id.fadlurahmanfdev.kotlin_feature_alarm.domain.receiver.FeatureAlarmReceiver
import com.github.fadlurahmanfdev.kotlin_core_notification.presentation.BaseFullScreenIntentActivity
import com.ncorti.slidetoact.SlideToActView

class AlarmNotificationActivity : BaseFullScreenIntentActivity() {
    lateinit var constraintLayout: ConstraintLayout
    lateinit var slider: SlideToActView
    lateinit var tvTime: TextView
    lateinit var tvDesc: TextView

    lateinit var time: String
    lateinit var location: String

    companion object {
        const val PARAM_TEXT_TIME = "PARAM_TEXT_TIME"
        const val PARAM_DESCRIPTION = "PARAM_DESCRIPTION"
    }

    override fun onCreateBaseFullScreenIntentActivity(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_alarm_notification)
        enableEdgeToEdge()

        constraintLayout = findViewById(R.id.main)
        slider = findViewById(R.id.slider)
        tvTime = findViewById(R.id.tv_alarm_time)
        tvDesc = findViewById(R.id.tv_desc)

        time = intent.getStringExtra(PARAM_TEXT_TIME) ?: ""
        location = intent.getStringExtra(PARAM_DESCRIPTION) ?: ""

        tvTime.text = time
        tvDesc.text = location

        setAppearanceLightStatusBar(false)
        setOnApplyWindowInsetsListener(constraintLayout)

        slider.onSlideCompleteListener = object : SlideToActView.OnSlideCompleteListener {
            override fun onSlideComplete(view: SlideToActView) {
                FeatureAlarmReceiver.sendBroadcastSendAlarm(
                    this@AlarmNotificationActivity,
                    AlarmReceiver::class.java
                )
                finish()
            }
        }
    }

    override fun onDestroyBaseFullScreenIntentActivity() {}

    fun setOnApplyWindowInsetsListener(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(view.paddingLeft, 0, view.paddingRight, systemBars.bottom)
            insets
        }
    }

    fun setAppearanceLightStatusBar(isLight: Boolean) {
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars =
            isLight
    }

}