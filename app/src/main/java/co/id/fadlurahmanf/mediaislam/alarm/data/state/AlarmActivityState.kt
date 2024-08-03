package co.id.fadlurahmanf.mediaislam.alarm.data.state

import co.id.fadlurahmanf.mediaislam.alarm.data.exception.AlarmPackageException

sealed class AlarmActivityState<out T : Any> {
    data object IDLE : AlarmActivityState<Nothing>()
    data object LOADING : AlarmActivityState<Nothing>()
    data class SUCCESS<out T : Any>(val data: T) : AlarmActivityState<T>()
    data class ERROR(
        val exception: AlarmPackageException
    ) : AlarmActivityState<Nothing>()
}