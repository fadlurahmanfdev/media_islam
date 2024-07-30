package co.id.fadlurahmanf.mediaislam.alarm.data.exception

data class AlarmPackageException(
    override val message: String,
    val httpCode: Int? = null,
    val enumCode: String? = null,
) : Throwable() {}