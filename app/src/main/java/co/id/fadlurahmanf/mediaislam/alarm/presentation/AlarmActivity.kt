package co.id.fadlurahmanf.mediaislam.alarm.presentation

import android.app.AlarmManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import co.id.fadlurahmanf.mediaislam.R
import co.id.fadlurahmanf.mediaislam.alarm.BaseAlarmActivity
import co.id.fadlurahmanf.mediaislam.alarm.data.state.AlarmActivityState
import co.id.fadlurahmanf.mediaislam.alarm.domain.worker.CancelAlarmWorker
import co.id.fadlurahmanf.mediaislam.databinding.ActivityAlarmBinding
import co.id.fadlurahmanf.mediaislam.alarm.domain.worker.ScheduleAlarmWorker
import co.id.fadlurahmanf.mediaislam.core.enums.PrayerTimeType
import co.id.fadlurahmanf.mediaislam.main.data.dto.model.AlarmPrayerTimeModel
import java.util.concurrent.TimeUnit
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

        binding.itemFajr.switchAlarm.setOnCheckedChangeListener { buttonCompound, checked ->
            if (buttonCompound.isPressed) {
                viewModel.saveAlarmPrayerTime(
                    isFajrAdhanActive = checked,
                    isDhuhrAdhanActive = null,
                    isAsrAdhanActive = null,
                    isMaghribAdhanActive = null,
                    isIshaAdhanActive = null
                )

                if (checked) {
                    scheduleAlarmByPrayerTimeType(
                        PrayerTimeType.FAJR,
                        prayerDate = alarmPrayerTimeModel.fajr.date,
                        prayerTime = alarmPrayerTimeModel.fajr.time,
                        prayerDateTime = alarmPrayerTimeModel.fajr.dateTime,
                    )
                } else {
                    cancelAlarmByPrayerTimeType(PrayerTimeType.FAJR)
                }
            }
        }

        binding.itemDhuhr.switchAlarm.setOnCheckedChangeListener { buttonCompound, checked ->
            if (buttonCompound.isPressed) {
                viewModel.saveAlarmPrayerTime(
                    isFajrAdhanActive = null,
                    isDhuhrAdhanActive = checked,
                    isAsrAdhanActive = null,
                    isMaghribAdhanActive = null,
                    isIshaAdhanActive = null
                )

                if (checked) {
                    scheduleAlarmByPrayerTimeType(
                        PrayerTimeType.DHUHR,
                        prayerDate = alarmPrayerTimeModel.dhuhr.date,
                        prayerTime = alarmPrayerTimeModel.dhuhr.time,
                        prayerDateTime = alarmPrayerTimeModel.dhuhr.dateTime,
                    )
                } else {
                    cancelAlarmByPrayerTimeType(PrayerTimeType.DHUHR)
                }
            }
        }

        binding.itemAsr.switchAlarm.setOnCheckedChangeListener { buttonCompound, checked ->
            if (buttonCompound.isPressed) {
                viewModel.saveAlarmPrayerTime(
                    isFajrAdhanActive = null,
                    isDhuhrAdhanActive = null,
                    isAsrAdhanActive = checked,
                    isMaghribAdhanActive = null,
                    isIshaAdhanActive = null
                )

                if (checked) {
                    scheduleAlarmByPrayerTimeType(
                        PrayerTimeType.ASR,
                        prayerDate = alarmPrayerTimeModel.asr.date,
                        prayerTime = alarmPrayerTimeModel.asr.time,
                        prayerDateTime = alarmPrayerTimeModel.asr.dateTime,
                    )
                } else {
                    cancelAlarmByPrayerTimeType(PrayerTimeType.ASR)
                }
            }
        }

        binding.itemMaghrib.switchAlarm.setOnCheckedChangeListener { buttonCompound, checked ->
            if (buttonCompound.isPressed) {
                viewModel.saveAlarmPrayerTime(
                    isFajrAdhanActive = null,
                    isDhuhrAdhanActive = null,
                    isAsrAdhanActive = null,
                    isMaghribAdhanActive = checked,
                    isIshaAdhanActive = null
                )

                if (checked) {
                    scheduleAlarmByPrayerTimeType(
                        PrayerTimeType.MAGHRIB,
                        prayerDate = alarmPrayerTimeModel.maghrib.date,
                        prayerTime = alarmPrayerTimeModel.maghrib.time,
                        prayerDateTime = alarmPrayerTimeModel.maghrib.dateTime,
                    )
                } else {
                    cancelAlarmByPrayerTimeType(PrayerTimeType.MAGHRIB)
                }
            }
        }

        binding.itemIsha.switchAlarm.setOnCheckedChangeListener { buttonCompound, checked ->
            if (buttonCompound.isPressed) {
                viewModel.saveAlarmPrayerTime(
                    isFajrAdhanActive = null,
                    isDhuhrAdhanActive = null,
                    isAsrAdhanActive = null,
                    isMaghribAdhanActive = null,
                    isIshaAdhanActive = checked
                )

                if (checked) {
                    scheduleAlarmByPrayerTimeType(
                        PrayerTimeType.ISHA,
                        prayerDate = alarmPrayerTimeModel.isha.date,
                        prayerTime = alarmPrayerTimeModel.isha.time,
                        prayerDateTime = alarmPrayerTimeModel.isha.dateTime,
                    )
                } else {
                    cancelAlarmByPrayerTimeType(PrayerTimeType.ISHA)
                }
            }
        }
    }

    private lateinit var workManager: WorkManager
    private fun scheduleAlarmByPrayerTimeType(
        type: PrayerTimeType,
        prayerDate: String,
        prayerTime: String,
        prayerDateTime: String
    ) {
        if (!::workManager.isInitialized) {
            workManager = WorkManager.getInstance(this)
        }
        val uniqueWorkName = getUniqueWorkByPrayerTimeType(type)
        val constraint = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        val alarmWorkRequest = PeriodicWorkRequestBuilder<ScheduleAlarmWorker>(1, TimeUnit.HOURS)
            .addTag("scheduleAlarm")
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
        workManager.enqueueUniquePeriodicWork(
            uniqueWorkName,
            ExistingPeriodicWorkPolicy.UPDATE,
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
    }


    private fun cancelAlarmByPrayerTimeType(type: PrayerTimeType) {
        if (!::workManager.isInitialized) {
            workManager = WorkManager.getInstance(this)
        }

        try {
            val cancelWork = OneTimeWorkRequestBuilder<CancelAlarmWorker>()
                .addTag("cancelScheduleAlarm")
                .setInputData(
                    workDataOf(
                        ScheduleAlarmWorker.PARAM_PRAYER_TIME_TYPE to type.name,
                    )
                )
                .build()
            workManager.enqueueUniqueWork(
                getUniqueWorkCancelPrayerTimeByPrayerTimeType(type),
                ExistingWorkPolicy.REPLACE,
                cancelWork
            )
        } catch (e: Throwable) {
            Log.e(AlarmActivity::class.java.simpleName, "cannot cancel alarm by prayer time type")
        }

        try {
            workManager.cancelUniqueWork(getUniqueWorkByPrayerTimeType(type))
        } catch (e: Throwable) {
            Log.e(AlarmActivity::class.java.simpleName, "cannot cancel work $type by prayerTime")
        }
    }

    private fun getUniqueWorkCancelPrayerTimeByPrayerTimeType(type: PrayerTimeType): String {
        return when (type) {
            PrayerTimeType.FAJR -> "cancelFajrAlarm"
            PrayerTimeType.DHUHR -> "cancelDhuhrAlarm"
            PrayerTimeType.ASR -> "cancelAsrAlarm"
            PrayerTimeType.MAGHRIB -> "cancelMaghribAlarm"
            PrayerTimeType.ISHA -> "cancelIshaAlarm"
        }
    }

    private fun getUniqueWorkByPrayerTimeType(type: PrayerTimeType): String {
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