package co.id.fadlurahmanf.mediaislam.storage.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import co.id.fadlurahmanf.mediaislam.storage.data.entity.AdhanAlarmEntity
import co.id.fadlurahmanf.mediaislam.storage.other.AppDatabaseConstant

@Dao
interface AdhanAlarmEntityDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(value: AdhanAlarmEntity): io.reactivex.Single<Unit>

    @Update
    fun update(value: AdhanAlarmEntity)

    @Query("SELECT * FROM ${AppDatabaseConstant.ADHAN_ALARM_ENTITY}")
    fun getAll(): io.reactivex.Single<List<AdhanAlarmEntity>>

    @Query("SELECT * FROM ${AppDatabaseConstant.ADHAN_ALARM_ENTITY} WHERE type = :type")
    fun getAllByType(type: String): io.reactivex.Single<List<AdhanAlarmEntity>>

    @Query("SELECT * FROM ${AppDatabaseConstant.ADHAN_ALARM_ENTITY} WHERE type = :type AND triggerDate = :date")
    fun getAllByTypeAndDate(type: String, date: String): io.reactivex.Single<List<AdhanAlarmEntity>>

    @Query("DELETE FROM ${AppDatabaseConstant.ADHAN_ALARM_ENTITY} WHERE type = :type")
    fun deleteByPrayerType(type: String): io.reactivex.Single<Int>
}