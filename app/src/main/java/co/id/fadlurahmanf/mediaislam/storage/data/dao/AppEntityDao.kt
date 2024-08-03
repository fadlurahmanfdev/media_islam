package co.id.fadlurahmanf.mediaislam.storage.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import co.id.fadlurahmanf.mediaislam.storage.data.entity.AppEntity
import co.id.fadlurahmanf.mediaislam.storage.other.AppDatabaseConstant
import io.reactivex.rxjava3.core.Single

@Dao
interface AppEntityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(value: AppEntity)

    @Update
    fun update(value: AppEntity)

    @Query("SELECT * FROM ${AppDatabaseConstant.APP_ENTITY_TABLE_NAME}")
    fun getAll(): Single<List<AppEntity>>

    @Query("SELECT * FROM ${AppDatabaseConstant.APP_ENTITY_TABLE_NAME}")
    fun getAllV2(): io.reactivex.Single<List<AppEntity>>

    @Query("SELECT isFirstInstall FROM ${AppDatabaseConstant.APP_ENTITY_TABLE_NAME}")
    fun getIsFirstInstall(): Single<List<Boolean>>

    @Query("DELETE FROM ${AppDatabaseConstant.APP_ENTITY_TABLE_NAME}")
    fun delete()
}