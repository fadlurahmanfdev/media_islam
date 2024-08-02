package co.id.fadlurahmanf.mediaislam.alarm.presentation

import android.app.AlarmManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import co.id.fadlurahmanf.mediaislam.R
import co.id.fadlurahmanf.mediaislam.alarm.BaseAlarmActivity
import co.id.fadlurahmanf.mediaislam.alarm.data.state.AlarmActivityState
import co.id.fadlurahmanf.mediaislam.databinding.ActivityAlarmBinding
import co.id.fadlurahmanf.mediaislam.alarm.domain.worker.ScheduleAlarmWorker
import co.id.fadlurahmanf.mediaislam.core.enums.PrayerTimeType
import co.id.fadlurahmanf.mediaislam.main.data.dto.model.AlarmPrayerTimeModel
import javax.inject.Inject

class AlarmActivity : BaseAlarmActivity<ActivityAlarmBinding>(ActivityAlarmBinding::inflate) {
    @Inject
    lateinit var viewModel: AlarmViewModel

    lateinit var alarmPrayerTimeModel: AlarmPrayerTimeModel

    override fun onBaseAlarmInjectActivity() {
        component.inject(this)
    }

    override fun onBaseAlarmCreate(savedInstanceState: Bundle?) {
        setAppearanceLightStatusBar(false)
        setOnApplyWindowInsetsListener(binding.main)

        binding.toolbar.ivLeading.setOnClickListener {
            finish()
        }

        binding.toolbar.tvTitle.text = "Alarm"
        binding.toolbar.tvTitle.visibility = View.VISIBLE

        initObserver()
        initAction()

        viewModel.getAlarmPrayerTime()
    }

    private fun initObserver() {
        viewModel.alarmModelLive.observe(this) { state ->
            when (state) {
                is AlarmActivityState.LOADING -> {
                    binding.layoutLoading.root.visibility = View.VISIBLE
                    binding.llSuccess.visibility = View.GONE
                }

                is AlarmActivityState.SUCCESS -> {
                    binding.layoutLoading.root.visibility = View.GONE
                    binding.llSuccess.visibility = View.VISIBLE

                    alarmPrayerTimeModel = state.data
                    setPrayerTime(state.data)
                }

                else -> {

                }
            }
        }

        viewModel.prayerTimeEntityModelLive.observe(this) { state ->
            when (state) {
                is AlarmActivityState.SUCCESS -> {
//                    scheduleAlarm()
                }

                else -> {

                }
            }
        }
    }

    private lateinit var alarmManager: AlarmManager

    private fun initAction() {
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        binding.itemFajr.switchAlarm.setOnCheckedChangeListener { _, checked ->
            viewModel.saveAlarmPrayerTime(
                isFajrAdhanActive = checked,
                isDhuhrAdhanActive = null,
                isAsrAdhanActive = null,
                isMaghribAdhanActive = null,
                isIshaAdhanActive = null
            )
        }

        binding.itemDhuhr.switchAlarm.setOnCheckedChangeListener { _, checked ->
            viewModel.saveAlarmPrayerTime(
                isFajrAdhanActive = null,
                isDhuhrAdhanActive = checked,
                isAsrAdhanActive = null,
                isMaghribAdhanActive = null,
                isIshaAdhanActive = null
            )
        }

        binding.itemAsr.switchAlarm.setOnCheckedChangeListener { _, checked ->
            viewModel.saveAlarmPrayerTime(
                isFajrAdhanActive = null,
                isDhuhrAdhanActive = null,
                isAsrAdhanActive = checked,
                isMaghribAdhanActive = null,
                isIshaAdhanActive = null
            )
        }

        binding.itemMaghrib.switchAlarm.setOnCheckedChangeListener { _, checked ->
            viewModel.saveAlarmPrayerTime(
                isFajrAdhanActive = null,
                isDhuhrAdhanActive = null,
                isAsrAdhanActive = null,
                isMaghribAdhanActive = checked,
                isIshaAdhanActive = null
            )

            if (checked) {
                scheduleAlarmByType(
                    PrayerTimeType.MAGHRIB,
                    prayerDate = alarmPrayerTimeModel.maghrib.date,
                    prayerTime = alarmPrayerTimeModel.maghrib.time,
                    prayerDateTime = alarmPrayerTimeModel.maghrib.dateTime,
                )
            } else {
                cancelWorkByPrayerTime(PrayerTimeType.MAGHRIB)
            }
        }

        binding.itemIsha.switchAlarm.setOnCheckedChangeListener { _, checked ->
            viewModel.saveAlarmPrayerTime(
                isFajrAdhanActive = null,
                isDhuhrAdhanActive = null,
                isAsrAdhanActive = null,
                isMaghribAdhanActive = null,
                isIshaAdhanActive = checked
            )
        }
    }

    private lateinit var workManager: WorkManager
    private fun scheduleAlarmByType(
        type: PrayerTimeType,
        prayerDate: String,
        prayerTime: String,
        prayerDateTime: String
    ) {
        if (!::workManager.isInitialized) {
            workManager = WorkManager.getInstance(this)
        }
        val uniqueWorkName = getUniqueWorkByPrayerTime(type)
        val constraint = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        val alarmWorkRequest = OneTimeWorkRequestBuilder<ScheduleAlarmWorker>()
            .addTag("adhan")
            .setInputData(
                workDataOf(
                    ScheduleAlarmWorker.PARAM_PRAYER_TIME_TYPE to type.name,
                    ScheduleAlarmWorker.PARAM_CURRENT_PRAYER_DATE to prayerDate,
                    ScheduleAlarmWorker.PARAM_CURRENT_PRAYER_TIME to prayerTime,
                    ScheduleAlarmWorker.PARAM_CURRENT_PRAYER_DATE_TIME to prayerDateTime,
                    ScheduleAlarmWorker.PARAM_LATITUDE to alarmPrayerTimeModel.latitude,
                    ScheduleAlarmWorker.PARAM_LONGITUDE to alarmPrayerTimeModel.longitude,
                )
            )
            .setConstraints(constraint)
            .build()
        workManager.enqueueUniqueWork(
            uniqueWorkName,
            ExistingWorkPolicy.REPLACE,
            alarmWorkRequest
        )

        workManager.getWorkInfosForUniqueWorkLiveData(uniqueWorkName)
            .observe(this) { workInfo ->
                println("MASUK_ LISTEN SIZE -> ${workInfo.size}")
                if (workInfo.isNotEmpty()) {
                    println("MASUK_ LISTEN STATE -> ${workInfo.first().state}")
                    println("MASUK_ LISTEN OUTPUT -> ${workInfo.first().outputData}")
                }
            }

//        val calendar = Calendar.getInstance().apply {
//            add(Calendar.SECOND, 10)
//        }
//
//        val calendar2 = Calendar.getInstance().apply {
//            add(Calendar.SECOND, 20)
//        }
//
//        val pendingIntent =
//            FeatureAlarmReceiver.getPendingIntentSetAlarm(this, 0, AlarmReceiver::class.java)
//        val pendingIntent2 =
//            FeatureAlarmReceiver.getPendingIntentSetAlarm(this, 1, AlarmReceiver::class.java)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                if (alarmManager.canScheduleExactAlarms()) {
//                    alarmManager.setExactAndAllowWhileIdle(
//                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent
//                    )
//                    alarmManager.setExactAndAllowWhileIdle(
//                        AlarmManager.RTC_WAKEUP, calendar2.timeInMillis, pendingIntent2
//                    )
//                }
//            } else {
//                alarmManager.setExactAndAllowWhileIdle(
//                    AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent
//                )
//                alarmManager.setExactAndAllowWhileIdle(
//                    AlarmManager.RTC_WAKEUP, calendar2.timeInMillis, pendingIntent2
//                )
//            }
//        }
    }


    private fun cancelWorkByPrayerTime(type: PrayerTimeType) {
        if (!::workManager.isInitialized) {
            workManager = WorkManager.getInstance(this)
        }
        workManager.cancelUniqueWork(getUniqueWorkByPrayerTime(type))
    }

    private fun getUniqueWorkByPrayerTime(type: PrayerTimeType): String {
        return when (type) {
            PrayerTimeType.FAJR -> "fajrAlarm"
            PrayerTimeType.DHUHR -> "dhuhrAlarm"
            PrayerTimeType.ASR -> "asrAlarm"
            PrayerTimeType.MAGHRIB -> "maghribAlarm"
            PrayerTimeType.ISHA -> "ishaAlarm"
        }
    }


    private fun setPrayerTime(data: AlarmPrayerTimeModel) {
        binding.itemFajr.ivIconPrayer.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.fajr
            )
        )
        binding.itemFajr.tvPrayerTimeType.text = "Subuh"
        binding.itemFajr.tvPrayerTime.text = data.fajr.readableTime
        binding.itemFajr.switchAlarm.isChecked = data.fajr.isAlarmActive

        binding.itemDhuhr.ivIconPrayer.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.mipmap.dhuhr
            )
        )
        binding.itemDhuhr.tvPrayerTimeType.text = "Zuhur"
        binding.itemDhuhr.tvPrayerTime.text = data.dhuhr.readableTime
        binding.itemDhuhr.switchAlarm.isChecked = data.dhuhr.isAlarmActive

        binding.itemAsr.ivIconPrayer.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.mipmap.asr
            )
        )
        binding.itemAsr.tvPrayerTimeType.text = "Ashar"
        binding.itemAsr.tvPrayerTime.text = data.asr.readableTime
        binding.itemAsr.switchAlarm.isChecked = data.asr.isAlarmActive

        binding.itemMaghrib.ivIconPrayer.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.maghrib
            )
        )
        binding.itemMaghrib.tvPrayerTimeType.text = "Maghrib"
        binding.itemMaghrib.tvPrayerTime.text = data.maghrib.readableTime
        binding.itemMaghrib.switchAlarm.isChecked = data.maghrib.isAlarmActive

        binding.itemIsha.ivIconPrayer.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.isha
            )
        )
        binding.itemIsha.tvPrayerTimeType.text = "Isya"
        binding.itemIsha.tvPrayerTime.text = data.isha.readableTime
        binding.itemIsha.switchAlarm.isChecked = data.isha.isAlarmActive
    }

}