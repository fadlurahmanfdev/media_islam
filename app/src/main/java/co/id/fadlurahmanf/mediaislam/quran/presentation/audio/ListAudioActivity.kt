package co.id.fadlurahmanf.mediaislam.quran.presentation.audio

import android.os.Bundle
import android.view.View
import co.id.fadlurahmanf.mediaislam.core.state.EQuranNetworkState
import co.id.fadlurahmanf.mediaislam.databinding.ActivityListAudioBinding
import co.id.fadlurahmanf.mediaislam.quran.BaseQuranActivity
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.AudioPerSurahModel
import co.id.fadlurahmanf.mediaislam.quran.presentation.audio.adapter.AudioPerSurahAdapter
import javax.inject.Inject

class ListAudioActivity :
    BaseQuranActivity<ActivityListAudioBinding>(ActivityListAudioBinding::inflate) {
    @Inject
    lateinit var viewModel: AudioQuranViewModel

    override fun onBaseQuranInjectActivity() {
        component.inject(this)
    }

    override fun onBaseQuranCreate(savedInstanceState: Bundle?) {
        setAppearanceLightStatusBar(false)
        setOnApplyWindowInsetsListener(binding.main)

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

        binding.rv.adapter = adapter
    }

}