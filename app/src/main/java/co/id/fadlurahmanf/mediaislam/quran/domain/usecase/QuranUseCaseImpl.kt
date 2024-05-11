package co.id.fadlurahmanf.mediaislam.quran.domain.usecase

import co.id.fadlurahmanf.mediaislam.quran.data.datasources.EQuranDatasourceRepository
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
                totalVerse = response.totalVerse ?: -1,
                verses = ArrayList((response.verses ?: listOf())).map { verseResp ->
                    DetailSurahModel.Verse(
                        no = verseResp.no ?: -1,
                        latinText = verseResp.latinText ?: "-",
                        indonesianText = verseResp.indonesianText ?: "-",
                        arabicText = verseResp.arabicText ?: "-"
                    )
                }.toList()
            )
        }
    }
}