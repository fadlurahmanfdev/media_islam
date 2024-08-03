package co.id.fadlurahmanf.mediaislam.alarm.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.id.fadlurahmanf.mediaislam.alarm.data.exception.AlarmPackageException
import co.id.fadlurahmanf.mediaislam.alarm.data.state.AlarmActivityState
import co.id.fadlurahmanf.mediaislam.alarm.domain.usecase.AlarmUseCase
import co.id.fadlurahmanf.mediaislam.core.ui.BaseViewModel
import co.id.fadlurahmanf.mediaislam.main.data.dto.model.AlarmPrayerTimeModel
import co.id.fadlurahmanf.mediaislam.storage.data.dto.model.AlarmPrayerTimeEntityModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class AlarmViewModel @Inject constructor(
    private val alarmUseCase: AlarmUseCase
) : BaseViewModel() {

    private val _alarmModelLive =
        MutableLiveData<AlarmActivityState<AlarmPrayerTimeModel>>(AlarmActivityState.IDLE)
    val alarmModelLive: LiveData<AlarmActivityState<AlarmPrayerTimeModel>> = _alarmModelLive

    fun getAlarmPrayerTime() {
        if (_alarmModelLive.value == AlarmActivityState.IDLE) {
            _alarmModelLive.value = AlarmActivityState.LOADING
        }
        baseDisposable.add(alarmUseCase.getAlarmPrayerTime()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _alarmModelLive.value = AlarmActivityState.SUCCESS(data = it)
                },
                {
                    _alarmModelLive.value =
                        AlarmActivityState.ERROR(AlarmPackageException(message = "00"))
                },
                {}
            ))
    }

    private val _prayerTimeEntityModelLive =
        MutableLiveData<AlarmActivityState<AlarmPrayerTimeEntityModel>>(AlarmActivityState.IDLE)
    val prayerTimeEntityModelLive: LiveData<AlarmActivityState<AlarmPrayerTimeEntityModel>> =
        _prayerTimeEntityModelLive

    fun saveAlarmPrayerTime(
        isFajrAdhanActive: Boolean?,
        isDhuhrAdhanActive: Boolean?,
        isAsrAdhanActive: Boolean?,
        isMaghribAdhanActive: Boolean?,
        isIshaAdhanActive: Boolean?,
    ) {
        _prayerTimeEntityModelLive.value = AlarmActivityState.LOADING
        baseDisposable.add(alarmUseCase.saveAlarmPrayerTime(
            isFajrAdhanActive = isFajrAdhanActive,
            isDhuhrAdhanActive = isDhuhrAdhanActive,
            isAsrAdhanActive = isAsrAdhanActive,
            isMaghribAdhanActive = isMaghribAdhanActive,
            isIshaAdhanActive = isIshaAdhanActive
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _prayerTimeEntityModelLive.value = AlarmActivityState.SUCCESS(it)
                },
                {
                    _prayerTimeEntityModelLive.value =
                        AlarmActivityState.ERROR(AlarmPackageException(message = "01"))
                },
                {}
            ))
    }
}