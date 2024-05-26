package co.id.fadlurahmanf.mediaislam.quran.presentation.audio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.id.fadlurahmanf.mediaislam.core.network.exception.fromEQuranException
import co.id.fadlurahmanf.mediaislam.core.state.EQuranNetworkState
import co.id.fadlurahmanf.mediaislam.core.ui.BaseViewModel
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.AudioQariModel
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.DetailSurahModel
import co.id.fadlurahmanf.mediaislam.quran.data.state.NowPlayingAudioState
import co.id.fadlurahmanf.mediaislam.quran.domain.usecase.QuranUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class AudioQuranViewModel @Inject constructor(
    private val quranUseCase: QuranUseCase
) : BaseViewModel() {
    private val _detailSurahLive = MutableLiveData<EQuranNetworkState<DetailSurahModel>>()
    val detailSurahLive: LiveData<EQuranNetworkState<DetailSurahModel>> = _detailSurahLive
    lateinit var detailSurahModel: DetailSurahModel

    fun getDetailSurah(surahNo: Int) {
        _detailSurahLive.value = EQuranNetworkState.LOADING
        baseDisposable.add(quranUseCase.getDetailSurah(surahNo).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { data ->
                    detailSurahModel = data
                    _detailSurahLive.value = EQuranNetworkState.SUCCESS(data = data)
                    getAudioFull(data.audioFull)
                },
                { error ->
                    _detailSurahLive.value = EQuranNetworkState.ERROR(error.fromEQuranException())
                },
                {}
            ))
    }

    private val _audioFullLive = MutableLiveData<EQuranNetworkState<List<AudioQariModel>>>()
    val audioFullLive: LiveData<EQuranNetworkState<List<AudioQariModel>>> = _audioFullLive

    private val _nowPlayingLive = MutableLiveData<NowPlayingAudioState>()
    val nowPlayingLive: LiveData<NowPlayingAudioState> = _nowPlayingLive

    fun getAudioFull(audioFull: List<DetailSurahModel.Audio>) {
        _audioFullLive.value = EQuranNetworkState.LOADING
        baseDisposable.add(quranUseCase.getAudioSurahFullFromQari(audioFull)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { data ->
                    _audioFullLive.value = EQuranNetworkState.SUCCESS(data = data)

                    if (data.isNotEmpty()) {
                        val audioFirst = data.first()
                        _nowPlayingLive.value = NowPlayingAudioState.SUCCESS(
                            qariId = audioFirst.qariId,
                            url = audioFirst.qariAudio,
                            qariName = audioFirst.qariName,
                            qariImage = audioFirst.qariImage,
                            qariImageKey = audioFirst.qariImageKey,
                        )
                    }
                },
                { error ->
                    _audioFullLive.value = EQuranNetworkState.ERROR(error.fromEQuranException())
                },
                {}
            ))
    }
}