package co.id.fadlurahmanf.mediaislam.quran.presentation.surah

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.util.UnstableApi
import co.id.fadlurahmanf.mediaislam.R
import co.id.fadlurahmanf.mediaislam.core.state.EQuranNetworkState
import co.id.fadlurahmanf.mediaislam.databinding.ActivityAudioBinding
import co.id.fadlurahmanf.mediaislam.quran.BaseQuranActivity
import co.id.fadlurahmanfdev.kotlin_feature_media_player.domain.common.BaseMusicPlayer
import co.id.fadlurahmanfdev.kotlin_feature_media_player.domain.plugin.FeatureMusicPlayer
import co.id.fadlurahmanfdev.kotlin_feature_media_player.others.utilities.MusicPlayerUtilities
import javax.inject.Inject

class AudioActivity : BaseQuranActivity<ActivityAudioBinding>(ActivityAudioBinding::inflate),
    BaseMusicPlayer.Callback {
    lateinit var featureMusicPlayer: FeatureMusicPlayer

    @Inject
    lateinit var viewModel: SurahViewModel

    var surahNo: Int = -1

    companion object {
        const val SURAH_NO = "SURAH_NO"
    }

    override fun onBaseQuranInjectActivity() {
        component.inject(this)
    }

    @UnstableApi
    override fun onBaseQuranCreate(savedInstanceState: Bundle?) {
        setAppearanceLightStatusBar(false)
        surahNo = intent.getIntExtra(SURAH_NO, -1)
        featureMusicPlayer = FeatureMusicPlayer(this)
        featureMusicPlayer.setCallback(this)
        featureMusicPlayer.initialize()
        initObserver()
        viewModel.getDetailSurah(surahNo)
    }

    @UnstableApi
    private fun initObserver() {
        viewModel.detailSurahLive.observe(this) { state ->
            when (state) {
                is EQuranNetworkState.SUCCESS -> {
                    binding.shimmerNowPlayingVideo.visibility = View.GONE
                    binding.llAudioController.visibility = View.VISIBLE

                    if (state.data.audioFull.isNotEmpty()) {
                        featureMusicPlayer.playRemoteAudio(state.data.audioFull.first().url)
                    }
                }

                else -> {
                    binding.llAudioController.visibility = View.GONE
                    binding.shimmerNowPlayingVideo.visibility = View.VISIBLE
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

}