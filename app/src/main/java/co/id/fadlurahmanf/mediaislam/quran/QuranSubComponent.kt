package co.id.fadlurahmanf.mediaislam.quran

import co.id.fadlurahmanf.mediaislam.quran.presentation.surah.DetailSurahActivity
import co.id.fadlurahmanf.mediaislam.quran.presentation.surah.HomeActivity
import dagger.Subcomponent

@Subcomponent
interface QuranSubComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): QuranSubComponent
    }

    fun inject(activity: HomeActivity)
    fun inject(activity: DetailSurahActivity)
}