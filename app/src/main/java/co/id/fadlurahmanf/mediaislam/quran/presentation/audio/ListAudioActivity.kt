package co.id.fadlurahmanf.mediaislam.quran.presentation.audio

import android.os.Bundle
import android.view.View
import co.id.fadlurahmanf.mediaislam.core.state.EQuranNetworkState
import co.id.fadlurahmanf.mediaislam.databinding.ActivityListAudioBinding
import co.id.fadlurahmanf.mediaislam.quran.BaseQuranActivity
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.AudioPerSurahModel
import co.id.fadlurahmanf.mediaislam.quran.domain.service.AudioQuranService
import co.id.fadlurahmanf.mediaislam.quran.presentation.audio.adapter.AudioPerSurahAdapter
import co.id.fadlurahmanfdev.kotlin_feature_media_player.data.state.MusicPlayerState
import co.id.fadlurahmanfdev.kotlin_feature_media_player.data.state.MusicPlayerState.*
import co.id.fadlurahmanfdev.kotlin_feature_media_player.domain.manager.FeatureMusicPlayerManager
import co.id.fadlurahmanfdev.kotlin_feature_media_player.domain.manager.FeatureMusicPlayerReceiverManager
import javax.inject.Inject

class ListAudioActivity :
    BaseQuranActivity<ActivityListAudioBinding>(ActivityListAudioBinding::inflate),
    AudioPerSurahAdapter.CallBack, FeatureMusicPlayerReceiverManager.CallBack {
    @Inject
    lateinit var viewModel: AudioQuranViewModel

    override fun onBaseQuranInjectActivity() {
        component.inject(this)
    }

    private lateinit var featureMusicPlayerReceiverManager: FeatureMusicPlayerReceiverManager


    override fun onBaseQuranCreate(savedInstanceState: Bundle?) {
        setAppearanceLightStatusBar(false)
        setOnApplyWindowInsetsListener(binding.main)

        featureMusicPlayerReceiverManager = FeatureMusicPlayerReceiverManager(this)
        featureMusicPlayerReceiverManager.setCallBack(this)
        featureMusicPlayerReceiverManager.registerReceiver(this)

        binding.toolbar.tvTitle.text = "Pilih Surat"
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.ivLeading.setOnClickListener {
            finish()
        }

        initAdapter()
        initObserver()

        viewModel.getListAudio()
    }

    private fun initObserver() {
        viewModel.listAudioLive.observe(this) { state ->
            when (state) {
                is EQuranNetworkState.LOADING -> {
                    binding.rv.visibility = View.GONE
                    binding.layoutShimmer.root.visibility = View.VISIBLE
                }

                is EQuranNetworkState.ERROR -> {

                }

                is EQuranNetworkState.SUCCESS -> {
                    audioListPerSurah.clear()
                    audioListPerSurah.addAll(state.data)
                    adapter.setList(audioListPerSurah)

                    binding.rv.visibility = View.VISIBLE
                    binding.layoutShimmer.root.visibility = View.GONE
                }

                else -> {

                }
            }
        }
    }

    private lateinit var adapter: AudioPerSurahAdapter
    private val audioListPerSurah = arrayListOf<AudioPerSurahModel>()
    private fun initAdapter() {
        adapter = AudioPerSurahAdapter()
        adapter.setList(audioListPerSurah)
        adapter.setCallBack(this)

        binding.rv.adapter = adapter
    }

    private var currentPlayingSurahNo: Int? = null
    private var currentPlayingSurah: AudioPerSurahModel? = null
    override fun onPlayClicked(audio: AudioPerSurahModel) {
        currentPlayingSurahNo = audio.surah.no
        currentPlayingSurah = audio
        FeatureMusicPlayerManager.playRemoteAudio(
            this,
            notificationId = 1,
            title = audio.surah.arabic,
            artist = audio.surah.latinName,
            urls = listOf(
                audio.url
            ),
            clazz = AudioQuranService::class.java,
        )
    }

    override fun onPauseClicked(audio: AudioPerSurahModel) {
        FeatureMusicPlayerManager.pause(this, AudioQuranService::class.java)
    }

    override fun onSendInfo(position: Long, duration: Long, state: MusicPlayerState) {
        println("MASUK_ SINI STATE: $state")
        if (currentPlayingSurahNo == null || currentPlayingSurah == null) {
            return
        }
        println("MASUK_ SINI 2 STATE: $state")
        when (state) {
            PLAYING, RESUME -> {
                adapter.setItemPlaying(currentPlayingSurahNo!!)
            }

            PAUSED, STOPPED, ENDED -> {
                adapter.resetIconItemIsPlayingIcon()
            }

            else -> {}
        }
    }

}