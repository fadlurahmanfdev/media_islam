package co.id.fadlurahmanf.mediaislam.core.di.components

import android.content.Context
import co.id.fadlurahmanf.mediaislam.core.di.modules.NetworkModule
import co.id.fadlurahmanf.mediaislam.quran.QuranModule
import co.id.fadlurahmanf.mediaislam.quran.QuranSubComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        QuranModule::class,
    ]
)
interface ApplicationComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }

    fun quranSubComponentFactory(): QuranSubComponent.Factory
}