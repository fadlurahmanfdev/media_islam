package co.id.fadlurahmanf.mediaislam.alarm.data.exception

data class AlarmWorkerException(
    val reason: String
) : Throwable() {}