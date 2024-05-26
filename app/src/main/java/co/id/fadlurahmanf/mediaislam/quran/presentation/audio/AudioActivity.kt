package co.id.fadlurahmanf.mediaislam.quran.presentation.audio

import android.os.Bundle
import android.view.View
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.GridLayoutManager
import co.id.fadlurahmanf.mediaislam.core.other.GlideUrlCachedKey
import co.id.fadlurahmanf.mediaislam.core.state.EQuranNetworkState
import co.id.fadlurahmanf.mediaislam.databinding.ActivityAudioBinding
import co.id.fadlurahmanf.mediaislam.quran.BaseQuranActivity
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.AudioQariModel
import co.id.fadlurahmanf.mediaislam.quran.data.state.NowPlayingAudioState
import co.id.fadlurahmanf.mediaislam.quran.presentation.audio.adapter.AudioSurahQariAdapter
import co.id.fadlurahmanfdev.kotlin_feature_media_player.domain.common.BaseMusicPlayer
import co.id.fadlurahmanfdev.kotlin_feature_media_player.domain.plugin.FeatureMusicPlayer
import co.id.fadlurahmanfdev.kotlin_feature_media_player.others.utilities.MusicPlayerUtilities
import com.bumptech.glide.Glide
import javax.inject.Inject

class AudioActivity : BaseQuranActivity<ActivityAudioBinding>(ActivityAudioBinding::inflate),
    BaseMusicPlayer.Callback, AudioSurahQariAdapter.CallBack {
    lateinit var featureMusicPlayer: FeatureMusicPlayer

    @Inject
    lateinit var viewModel: AudioQuranViewModel

    var surahNo: Int = -1

    companion object {
        const val SURAH_NO = "SURAH_NO"
    }

    override fun onBaseQuranInjectActivity() {
        component.inject(this)
    }

    @UnstableApi
    override fun onBaseQuranCreate(savedInstanceState: Bundle?) {
        setOnApplyWindowInsetsListener(binding.main)
        setAppearanceLightStatusBar(false)
        surahNo = intent.getIntExtra(SURAH_NO, -1)
        featureMusicPlayer = FeatureMusicPlayer(this)
        featureMusicPlayer.setCallback(this)
        featureMusicPlayer.initialize()
        initAdapter()
        initObserver()

        viewModel.getDetailSurah(surahNo)
    }

    private lateinit var audioQariAdapter: AudioSurahQariAdapter
    private fun initAdapter() {
        audioQariAdapter = AudioSurahQariAdapter()
        audioQariAdapter.setCallBack(this)
        val gm = GridLayoutManager(this, 2)
        binding.rvListQari.layoutManager = gm
        binding.rvListQari.adapter = audioQariAdapter
    }

    @UnstableApi
    private fun initObserver() {
        viewModel.nowPlayingLive.observe(this) { state ->
            when (state) {
                is NowPlayingAudioState.SUCCESS -> {
                    binding.tvNowPlayingAr.text = state.nowPlayingArTitle
                    binding.tvNowPlayingLatin.text = state.nowPlayingLatinTitle
                    binding.tvNowPlayingIndonesia.text = state.nowPlayingIndonesaTitle
                    binding.tvNowPlayingQariName.text = state.qariName
                    if (state.qariImage != null) {
                        Glide.with(binding.ivNowPlayingQariImage)
                            .load(GlideUrlCachedKey(state.qariImage, state.qariImageKey))
                            .centerCrop()
                            .into(binding.ivNowPlayingQariImage)
                    }

                    binding.shimmerNowPlayingVideo.visibility = View.GONE
                    binding.llNowPlayingAudio.visibility = View.VISIBLE
                    binding.llAudioController.visibility = View.VISIBLE
                }

                else -> {
                    binding.llNowPlayingAudio.visibility = View.GONE
                    binding.llAudioController.visibility = View.GONE
                    binding.shimmerNowPlayingVideo.visibility = View.VISIBLE
                }
            }
        }

        viewModel.audioFullLive.observe(this) { state ->
            when (state) {
                is EQuranNetworkState.SUCCESS -> {
                    val audioQaris = ArrayList(state.data)
                    audioQariAdapter.setList(audioQaris)

                    if (state.data.isNotEmpty()) {
                        val audioFirst = state.data.first()
                        audioQariAdapter.setWhichQariIsPlaying(audioFirst.qariId)
                        viewModel.selectAudio(audioFirst)
                    }

                    binding.llListAudioQari.visibility = View.VISIBLE
                }

                else -> {
                    binding.llListAudioQari.visibility = View.GONE
                }
            }
        }

        viewModel.detailSurahLive.observe(this) { state ->
            when (state) {
                is EQuranNetworkState.SUCCESS -> {
                    viewModel.getAudioFullDetail(state.data.audioFull)
                }

                else -> {

                }
            }

        }
    }

    @UnstableApi
    override fun onDurationFetched(duration: Long) {
        super.onDurationFetched(duration)
        binding.seekbar.max = duration.toInt()
        binding.tvDuration.text = MusicPlayerUtilities.formatToReadableTime(duration)
    }

    @UnstableApi
    override fun onPositionChanged(position: Long) {
        super.onPositionChanged(position)
        binding.seekbar.progress = position.toInt()
        binding.tvPosition.text = MusicPlayerUtilities.formatToReadableTime(position)
    }

    @UnstableApi
    override fun onDestroy() {
        featureMusicPlayer.destroy()
        super.onDestroy()
    }

    override fun onClicked(audio: AudioQariModel) {
        audioQariAdapter.setWhichQariIsPlaying(audio.qariId)
        viewModel.selectAudio(audio)
    }

}