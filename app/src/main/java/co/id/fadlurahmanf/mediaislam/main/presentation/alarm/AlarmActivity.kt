package co.id.fadlurahmanf.mediaislam.main.presentation.alarm

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import co.id.fadlurahmanf.mediaislam.R
import co.id.fadlurahmanf.mediaislam.core.state.AladhanNetworkState
import co.id.fadlurahmanf.mediaislam.databinding.ActivityAlarmBinding
import co.id.fadlurahmanf.mediaislam.main.BaseMainActivity
import co.id.fadlurahmanf.mediaislam.main.data.dto.model.PrayersTimeModel
import co.id.fadlurahmanf.mediaislam.main.domain.receiver.AlarmReceiver
import co.id.fadlurahmanfdev.kotlin_feature_alarm.domain.receiver.FeatureAlarmReceiver
import java.util.Calendar
import javax.inject.Inject

class AlarmActivity : BaseMainActivity<ActivityAlarmBinding>(ActivityAlarmBinding::inflate) {
    @Inject
    lateinit var viewModel: AlarmViewModel

    override fun onBaseMainInjectActivity() {
        component.inject(this)
    }

    override fun onBaseMainCreate(savedInstanceState: Bundle?) {
        setAppearanceLightStatusBar(false)
        setOnApplyWindowInsetsListener(binding.main)

        binding.toolbar.ivLeading.setOnClickListener {
            finish()
        }

        binding.toolbar.tvTitle.text = "Alarm"
        binding.toolbar.tvTitle.visibility = View.VISIBLE

        initObserver()
        initAction()

        viewModel.getTodayPrayerTime()
    }

    private fun initObserver() {
        viewModel.prayersTimeLive.observe(this) { state ->
            when (state) {
                is AladhanNetworkState.LOADING -> {
                    binding.layoutLoading.root.visibility = View.VISIBLE
                    binding.llSuccess.visibility = View.GONE
                }

                is AladhanNetworkState.SUCCESS -> {
                    binding.layoutLoading.root.visibility = View.GONE
                    binding.llSuccess.visibility = View.VISIBLE

                    setPrayerTime(state.data)
                }

                else -> {

                }
            }
        }
    }

    private lateinit var alarmManager: AlarmManager

    private fun initAction() {
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        binding.itemDhuhr.switchAlarm.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                scheduleAlarm()
            }
        }
    }

    private fun scheduleAlarm() {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.SECOND, 10)
        }

        val calendar2 = Calendar.getInstance().apply {
            add(Calendar.SECOND, 20)
        }

        val pendingIntent =
            FeatureAlarmReceiver.getPendingIntentSetAlarm(this, 0, AlarmReceiver::class.java)
        val pendingIntent2 =
            FeatureAlarmReceiver.getPendingIntentSetAlarm(this, 1, AlarmReceiver::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent
                    )
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP, calendar2.timeInMillis, pendingIntent2
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent
                )
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, calendar2.timeInMillis, pendingIntent2
                )
            }
        }
    }

    private fun setPrayerTime(data: PrayersTimeModel) {
        binding.itemFajr.ivIconPrayer.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.fajr
            )
        )
        binding.itemFajr.tvPrayerTimeType.text = "Subuh"
        binding.itemFajr.tvPrayerTime.text = data.timing.fajr

        binding.itemDhuhr.ivIconPrayer.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.mipmap.dhuhr
            )
        )
        binding.itemDhuhr.tvPrayerTimeType.text = "Zuhur"
        binding.itemDhuhr.tvPrayerTime.text = data.timing.dhuhr

        binding.itemAsr.ivIconPrayer.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.mipmap.asr
            )
        )
        binding.itemAsr.tvPrayerTimeType.text = "Ashar"
        binding.itemAsr.tvPrayerTime.text = data.timing.asr

        binding.itemMaghrib.ivIconPrayer.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.maghrib
            )
        )
        binding.itemMaghrib.tvPrayerTimeType.text = "Maghrib"
        binding.itemMaghrib.tvPrayerTime.text = data.timing.maghrib

        binding.itemIsha.ivIconPrayer.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.isha
            )
        )
        binding.itemIsha.tvPrayerTimeType.text = "Isya"
        binding.itemIsha.tvPrayerTime.text = data.timing.isha
    }

}