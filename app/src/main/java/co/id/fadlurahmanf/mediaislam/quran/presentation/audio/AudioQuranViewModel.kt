package co.id.fadlurahmanf.mediaislam.quran.presentation.audio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.id.fadlurahmanf.mediaislam.core.network.exception.fromEQuranException
import co.id.fadlurahmanf.mediaislam.core.state.EQuranNetworkState
import co.id.fadlurahmanf.mediaislam.core.ui.BaseViewModel
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.AudioQariModel
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.DetailSurahModel
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.AudioPerSurahModel
import co.id.fadlurahmanf.mediaislam.quran.data.state.NowPlayingAudioState
import co.id.fadlurahmanf.mediaislam.quran.domain.usecase.QuranUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class AudioQuranViewModel @Inject constructor(
    private val quranUseCase: QuranUseCase
) : BaseViewModel() {

    private val _listAudioLive = MutableLiveData<EQuranNetworkState<List<AudioPerSurahModel>>>()
    val listAudioLive: LiveData<EQuranNetworkState<List<AudioPerSurahModel>>> = _listAudioLive

    fun getListAudio() {
        _listAudioLive.value = EQuranNetworkState.LOADING
        baseDisposable.add(
            quranUseCase.getListAudio().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _listAudioLive.value = EQuranNetworkState.SUCCESS(it)
                    },
                    {
                        _listAudioLive.value = EQuranNetworkState.ERROR(it.fromEQuranException())
                    },
                    {},
                )
        )
    }

    fun createChannel() {
        quranUseCase.createAudioMediaChannel()
    }

    private val _nowPlayingLive = MutableLiveData<NowPlayingAudioState>(NowPlayingAudioState.IDLE)
    val nowPlayingLive: LiveData<NowPlayingAudioState> = _nowPlayingLive

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
                    _detailSurahLive.value = EQuranNetworkState.SUCCESS(data)
                },
                { error ->

                },
                {}
            ))
    }

    private val _audioFullLive = MutableLiveData<EQuranNetworkState<List<AudioQariModel>>>()
    val audioFullLive: LiveData<EQuranNetworkState<List<AudioQariModel>>> = _audioFullLive
    fun getAudioFullDetail(audioFull: List<DetailSurahModel.Audio>) {
        _audioFullLive.value = EQuranNetworkState.LOADING
        baseDisposable.add(quranUseCase.getAudioSurahFullFromQari(audioFull)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { data ->
                    _audioFullLive.value = EQuranNetworkState.SUCCESS(data = data)
                },
                { error ->
                    _audioFullLive.value = EQuranNetworkState.ERROR(error.fromEQuranException())
                },
                {}
            ))
    }

    fun selectAudio(audio: AudioQariModel) {
        _nowPlayingLive.value = NowPlayingAudioState.SUCCESS(
            nowPlayingArTitle = detailSurahModel.arabic,
            nowPlayingLatinTitle = detailSurahModel.latin,
            nowPlayingIndonesaTitle = detailSurahModel.meaning,
            qariId = audio.qariId,
            url = audio.qariAudio,
            qariName = audio.qariName,
            qariImage = audio.qariImage,
            qariImageKey = audio.qariImageKey,
        )
    }
}