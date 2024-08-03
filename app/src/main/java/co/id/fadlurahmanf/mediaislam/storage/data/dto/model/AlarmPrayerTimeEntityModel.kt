package co.id.fadlurahmanf.mediaislam.storage.data.dto.model

data class AlarmPrayerTimeEntityModel(
    var isFajrAdhanAlarmActive: Boolean = false,
    var isDhuhrAdhanAlarmActive: Boolean = false,
    var isAsrAdhanAlarmActive: Boolean = false,
    var isMaghribAdhanAlarmActive: Boolean = false,
    var isIshaAdhanAlarmActive: Boolean = false,
)
