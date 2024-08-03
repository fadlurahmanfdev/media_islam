package co.id.fadlurahmanf.mediaislam.core.di.components

import android.content.Context
import co.id.fadlurahmanf.mediaislam.alarm.AlarmModule
import co.id.fadlurahmanf.mediaislam.alarm.AlarmSubComponent
import co.id.fadlurahmanf.mediaislam.article.ArticleModule
import co.id.fadlurahmanf.mediaislam.article.ArticleSubComponent
import co.id.fadlurahmanf.mediaislam.core.di.modules.NetworkModule
import co.id.fadlurahmanf.mediaislam.core.di.modules.SharedDatasourceModule
import co.id.fadlurahmanf.mediaislam.core.di.modules.SharedModule
import co.id.fadlurahmanf.mediaislam.core.di.modules.SharedUseCaseModule
import co.id.fadlurahmanf.mediaislam.main.MainModule
import co.id.fadlurahmanf.mediaislam.main.MainSubComponent
import co.id.fadlurahmanf.mediaislam.quran.QuranModule
import co.id.fadlurahmanf.mediaislam.quran.QuranSubComponent
import co.id.fadlurahmanf.mediaislam.storage.StorageModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        SharedModule::class,
        SharedDatasourceModule::class,
        SharedUseCaseModule::class,
        StorageModule::class,
        NetworkModule::class,
        QuranModule::class,
        MainModule::class,
        ArticleModule::class,
        AlarmModule::class,
    ]
)
interface ApplicationComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }

    fun quranSubComponentFactory(): QuranSubComponent.Factory
    fun mainSubComponentFactory(): MainSubComponent.Factory
    fun alarmSubComponentFactory(): AlarmSubComponent.Factory
    fun articleSubComponentFactory(): ArticleSubComponent.Factory
}