package co.id.fadlurahmanf.mediaislam.quran.domain.usecase

import co.id.fadlurahmanf.mediaislam.core.network.dto.response.quran.DetailSurahResponse
import co.id.fadlurahmanf.mediaislam.quran.data.datasources.EQuranDatasourceRepository
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.AudioQariModel
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.DetailSurahModel
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.SurahModel
import io.reactivex.rxjava3.core.Observable

class QuranUseCaseImpl(private val quranRepository: EQuranDatasourceRepository) : QuranUseCase {
    override fun getListSurah(): Observable<List<SurahModel>> {
        return quranRepository.getListSurah().map { response ->
            response.map { surah ->
                SurahModel(
                    surahNo = surah.surahNo ?: -1,
                    arabic = surah.arabic ?: "-",
                    latin = surah.latin ?: "-",
                    meaning = surah.meaning ?: "-",
                    origin = surah.origin ?: "-",
                    totalVerse = surah.totalVerse ?: -1
                )
            }
        }
    }

    override fun getDetailSurah(surahNo: Int): Observable<DetailSurahModel> {
        return quranRepository.getDetailSurah(surahNo).map { response ->
            DetailSurahModel(
                surahNo = response.surahNo ?: -1,
                arabic = response.arabic ?: "-",
                latin = response.latin ?: "-",
                meaning = response.meaning ?: "-",
                origin = response.origin ?: "-",
                desc = response.desc ?: "-",
                totalVerse = response.totalVerse ?: -1,
                audioFull = getAudioFull(response),
                verses = ArrayList((response.verses ?: listOf())).map { verseResp ->
                    DetailSurahModel.Verse(
                        no = verseResp.no ?: -1,
                        latinText = verseResp.latinText ?: "-",
                        indonesianText = verseResp.indonesianText ?: "-",
                        arabicText = verseResp.arabicText ?: "-",
                        audio = getAudioVerse(verseResp),
                    )
                }.toList()
            )
        }
    }

    override fun getAudioSurahFullFromQari(audioFull: List<DetailSurahModel.Audio>): Observable<List<AudioQariModel>> {
        return quranRepository.getQari().map { qaris ->
            val audios: ArrayList<AudioQariModel> = arrayListOf()
            audioFull.forEach { audio ->
                if (qaris.firstOrNull { elementQari -> elementQari.id == audio.qariId } != null) {
                    val qari = qaris.first { elementQari -> elementQari.id == audio.qariId }
                    audios.add(
                        AudioQariModel(
                            qariId = qari.id ?: "-",
                            qariName = qari.name ?: "-",
                            qariImage = qari.image,
                            qariImageKey = qari.imageKey,
                            qariAudio = audio.url
                        )
                    )
                }
            }
            audios
        }
    }

    private fun getAudioFull(response: DetailSurahResponse): List<DetailSurahModel.Audio> {
        val audioFull = arrayListOf<DetailSurahModel.Audio>()
        if (response.audio?.audio1 != null) {
            audioFull.add(
                DetailSurahModel.Audio(
                    url = response.audio.audio1,
                    qariId = "Abdullah-Al-Juhany"
                )
            )
        }

        if (response.audio?.audio2 != null) {
            audioFull.add(
                DetailSurahModel.Audio(
                    url = response.audio.audio2,
                    qariId = "Abdul-Muhsin-Al-Qasim"
                )
            )
        }

        if (response.audio?.audio3 != null) {
            audioFull.add(
                DetailSurahModel.Audio(
                    url = response.audio.audio3,
                    qariId = "Abdurrahman-as-Sudais"
                )
            )
        }

        if (response.audio?.audio4 != null) {
            audioFull.add(
                DetailSurahModel.Audio(
                    url = response.audio.audio4,
                    qariId = "Ibrahim-Al-Dossari"
                )
            )
        }

        if (response.audio?.audio5 != null) {
            audioFull.add(
                DetailSurahModel.Audio(
                    url = response.audio.audio5,
                    qariId = "Misyari-Rasyid-Al-Afasi"
                )
            )
        }
        return audioFull
    }

    private fun getAudioVerse(verseResp: DetailSurahResponse.Verse): List<DetailSurahModel.Audio> {
        val audio = arrayListOf<DetailSurahModel.Audio>()
        if (verseResp.audio?.audio1 != null) {
            audio.add(
                DetailSurahModel.Audio(
                    url = verseResp.audio.audio1,
                    qariId = "Abdullah-Al-Juhany"
                )
            )
        }

        if (verseResp.audio?.audio2 != null) {
            audio.add(
                DetailSurahModel.Audio(
                    url = verseResp.audio.audio2,
                    qariId = "Abdul-Muhsin-Al-Qasim"
                )
            )
        }

        if (verseResp.audio?.audio3 != null) {
            audio.add(
                DetailSurahModel.Audio(
                    url = verseResp.audio.audio3,
                    qariId = "Abdurrahman-as-Sudais"
                )
            )
        }

        if (verseResp.audio?.audio4 != null) {
            audio.add(
                DetailSurahModel.Audio(
                    url = verseResp.audio.audio4,
                    qariId = "Ibrahim-Al-Dossari"
                )
            )
        }

        if (verseResp.audio?.audio5 != null) {
            audio.add(
                DetailSurahModel.Audio(
                    url = verseResp.audio.audio5,
                    qariId = "Misyari-Rasyid-Al-Afasi"
                )
            )
        }
        return audio
    }
}