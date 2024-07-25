package co.id.fadlurahmanf.mediaislam.quran

import co.id.fadlurahmanf.mediaislam.quran.presentation.audio.AudioActivity
import co.id.fadlurahmanf.mediaislam.quran.presentation.audio.ListAudioActivity
import co.id.fadlurahmanf.mediaislam.quran.presentation.surah.DetailSurahActivity
import co.id.fadlurahmanf.mediaislam.quran.presentation.surah.ListSurahActivity
import dagger.Subcomponent

@Subcomponent
interface QuranSubComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): QuranSubComponent
    }

    fun inject(activity: ListSurahActivity)
    fun inject(activity: DetailSurahActivity)
    fun inject(activity: ListAudioActivity)
    fun inject(activity: AudioActivity)
}