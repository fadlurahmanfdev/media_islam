package co.id.fadlurahmanf.mediaislam.quran.presentation.surah

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import co.id.fadlurahmanf.mediaislam.R
import co.id.fadlurahmanf.mediaislam.core.state.EQuranNetworkState
import co.id.fadlurahmanf.mediaislam.databinding.ActivityDetailSurahBinding
import co.id.fadlurahmanf.mediaislam.quran.BaseQuranActivity
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.DetailSurahModel
import co.id.fadlurahmanf.mediaislam.quran.presentation.surah.adapter.ListVerseAdapter
import javax.inject.Inject

class DetailSurahActivity :
    BaseQuranActivity<ActivityDetailSurahBinding>(ActivityDetailSurahBinding::inflate) {
    companion object {
        const val SURAH_NAME = "SURAH_NAME"
        const val SURAH_NO = "SURAH_NO"
    }

    @Inject
    lateinit var viewModel: SurahViewModel

    override fun onBaseQuranInjectActivity() {
        component.inject(this)
    }

    override fun onBaseQuranCreate(savedInstanceState: Bundle?) {
        setAppearanceLightStatusBar(false)
        val surahNo = intent.getIntExtra(SURAH_NO, -1)
        val surahName = intent.getStringExtra(SURAH_NAME)
        binding.toolbar.tvTitle.text = surahName
        binding.toolbar.tvTitle.visibility = View.VISIBLE

        initObserver()
        initAdapter()

        viewModel.getDetailSurah(surahNo)
    }

    private lateinit var adapter: ListVerseAdapter
    private val verses: ArrayList<DetailSurahModel.Verse> = arrayListOf()
    private fun initAdapter() {
        adapter = ListVerseAdapter()
        adapter.setList(verses)
        binding.rv.adapter = adapter
    }

    private fun initObserver() {
        viewModel.detailSurahLive.observe(this) { state ->
            when (state) {
                is EQuranNetworkState.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.layoutShimmerVerse.root.visibility = View.VISIBLE
                    binding.rv.visibility = View.GONE
                }

                is EQuranNetworkState.SUCCESS -> {
                    verses.clear()
                    verses.addAll(state.data.verses)
                    adapter.setList(verses)

                    binding.progressBar.visibility = View.GONE
                    binding.layoutShimmerVerse.root.visibility = View.GONE
                    binding.rv.visibility = View.VISIBLE
                }

                is EQuranNetworkState.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.layoutShimmerVerse.root.visibility = View.VISIBLE
                    binding.rv.visibility = View.GONE

                    showFailedBebasBottomsheet(state.exception)
                }

                else -> {

                }
            }
        }
    }

}