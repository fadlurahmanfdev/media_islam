package co.id.fadlurahmanf.mediaislam.quran.domain.usecase

import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.AudioQariModel
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.DetailSurahModel
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.AudioPerSurahModel
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.SurahModel
import io.reactivex.rxjava3.core.Observable

interface QuranUseCase {
    fun getListAudio(): Observable<List<AudioPerSurahModel>>
    fun createAudioMediaChannel()
    fun getListSurah(): Observable<List<SurahModel>>
    fun getDetailSurah(surahNo: Int): Observable<DetailSurahModel>
    fun getAudioSurahFullFromQari(audioFull: List<DetailSurahModel.Audio>): Observable<List<AudioQariModel>>
}