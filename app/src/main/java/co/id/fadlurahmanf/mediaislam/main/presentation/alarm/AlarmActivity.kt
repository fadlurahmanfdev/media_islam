package co.id.fadlurahmanf.mediaislam.main.presentation.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import co.id.fadlurahmanf.mediaislam.R
import co.id.fadlurahmanf.mediaislam.core.state.AladhanNetworkState
import co.id.fadlurahmanf.mediaislam.databinding.ActivityAlarmBinding
import co.id.fadlurahmanf.mediaislam.main.BaseMainActivity
import co.id.fadlurahmanf.mediaislam.main.data.dto.model.PrayersTimeModel
import co.id.fadlurahmanf.mediaislam.main.domain.receiver.AlarmReceiver
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
            println("MASUK_ CHECKED: $checked")
            if (checked) {
                scheduleAlarm()
            }
        }
    }

    private fun scheduleAlarm() {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.SECOND, 10)
        }

        val pendingIntent = AlarmReceiver.getPendingIntent(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                println("MASUK_ CAN SCHEDULE EXACT ALARM: ${alarmManager.canScheduleExactAlarms()}")
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExact(
                        AlarmManager.RTC, calendar.timeInMillis, pendingIntent
                    )
                }
            } else {
                println("MASUK_ SCHEDULE EXACT ALARM BELOW API 31")
                alarmManager.setExact(
                    AlarmManager.RTC, calendar.timeInMillis, pendingIntent
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