package co.id.fadlurahmanf.mediaislam.storage

import android.content.Context
import co.id.fadlurahmanf.mediaislam.storage.data.dao.AdhanAlarmEntityDao
import co.id.fadlurahmanf.mediaislam.storage.data.dao.AppEntityDao
import co.id.fadlurahmanf.mediaislam.storage.data.datasource.AppLocalDatasource
import co.id.fadlurahmanf.mediaislam.storage.data.datasource.AppLocalDatasourceImpl
import co.id.fadlurahmanf.mediaislam.storage.domain.AppDatabase
import dagger.Module
import dagger.Provides

@Module
class StorageModule {
    @Provides
    fun provideAppEntityDao(context: Context): AppEntityDao {
        return AppDatabase.getDatabase(context).appEntityDao()
    }

    @Provides
    fun provideAdhanAlarmEntityDao(context: Context): AdhanAlarmEntityDao {
        return AppDatabase.getDatabase(context).adhanAlarmEntityDao()
    }

    @Provides
    fun provideAppLocalDatasource(
        appEntityDao: AppEntityDao
    ): AppLocalDatasource {
        return AppLocalDatasourceImpl(appEntityDao)
    }
}