package co.id.fadlurahmanf.mediaislam.quran.presentation.surah

import android.content.Intent
import android.os.Bundle
import android.view.View
import co.id.fadlurahmanf.mediaislam.core.state.EQuranNetworkState
import co.id.fadlurahmanf.mediaislam.databinding.ActivityDetailSurahBinding
import co.id.fadlurahmanf.mediaislam.quran.BaseQuranActivity
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.DetailSurahModel
import co.id.fadlurahmanf.mediaislam.quran.presentation.audio.AudioActivity
import co.id.fadlurahmanf.mediaislam.quran.presentation.surah.adapter.ListVerseAdapter
import javax.inject.Inject

class DetailSurahActivity :
    BaseQuranActivity<ActivityDetailSurahBinding>(ActivityDetailSurahBinding::inflate),
    ListVerseAdapter.CallBack {
    companion object {
        const val SURAH_NAME = "SURAH_NAME"
        const val SURAH_NO = "SURAH_NO"
    }

    @Inject
    lateinit var viewModel: SurahViewModel

    override fun onBaseQuranInjectActivity() {
        component.inject(this)
    }

    var surahNo: Int = -1
    lateinit var surahName: String
    override fun onBaseQuranCreate(savedInstanceState: Bundle?) {
        setOnApplyWindowInsetsListener(binding.main)
        setAppearanceLightStatusBar(false)
        surahNo = intent.getIntExtra(SURAH_NO, -1)
        surahName = intent.getStringExtra(SURAH_NAME) ?: "-"

        initAppBar()
        initAdapter()
        initObserver()
        initAction()

        viewModel.getDetailSurah(surahNo)
    }

    private fun initAppBar() {
        binding.toolbar.tvTitle.text = surahName
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.ivLeading.setOnClickListener {
            finish()
        }
    }

    private fun initAction() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getDetailSurah(surahNo)
        }

        binding.llAudio.setOnClickListener {
            val intent = Intent(this, AudioActivity::class.java)
            intent.apply {
                putExtra(AudioActivity.SURAH_NO, viewModel.detailSurahModel.surahNo)
            }
            startActivity(intent)
        }
    }

    private lateinit var adapter: ListVerseAdapter
    private val verses: ArrayList<DetailSurahModel.Verse> = arrayListOf()
    private fun initAdapter() {
        adapter = ListVerseAdapter()
        adapter.setCallBack(this)
        adapter.setList(verses)
        binding.rv.adapter = adapter
    }

    private fun initObserver() {
        viewModel.detailSurahLive.observe(this) { state ->
            when (state) {
                is EQuranNetworkState.LOADING -> {
                    binding.llMainLayout.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                    binding.layoutShimmerVerse.root.visibility = View.VISIBLE
                }

                is EQuranNetworkState.SUCCESS -> {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        binding.tvSurahMeaning.text = Html.fromHtml(
//                            "Arti: <b>${state.data.meaning}</b>",
//                            Html.FROM_HTML_MODE_COMPACT
//                        )
//                        binding.tvSurahExplanation.text = Html.fromHtml(
//                            state.data.desc,
//                            Html.FROM_HTML_MODE_COMPACT
//                        )
//                    } else {
//                        binding.tvSurahMeaning.text =
//                            Html.fromHtml("Arti: <b>${state.data.meaning}</b>")
//                        binding.tvSurahExplanation.text =
//                            Html.fromHtml(state.data.desc)
//                    }

                    verses.clear()
                    verses.addAll(state.data.verses)
                    adapter.setList(verses)

                    binding.swipeRefresh.isRefreshing = false
                    binding.progressBar.visibility = View.GONE
                    binding.layoutShimmerVerse.root.visibility = View.GONE
                    binding.llMainLayout.visibility = View.VISIBLE
                }

                is EQuranNetworkState.ERROR -> {
                    binding.swipeRefresh.isRefreshing = false
                    binding.progressBar.visibility = View.GONE
                    binding.llMainLayout.visibility = View.GONE
                    binding.layoutShimmerVerse.root.visibility = View.VISIBLE

                    showFailedBebasBottomsheet(state.exception)
                }

                else -> {}
            }
        }
    }

    override fun onVerseClickedShare(verse: DetailSurahModel.Verse) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "\n${surahName} - Ayat ke ${verse.no}\n\n${verse.arabicText}\n\n${verse.indonesianText}\n\nSelengkapnya baca di\nhttps://quran.kemenag.go.id/quran/per-ayat/surah/$surahNo?from=1"
        )
        intent.type = "text/plain"
        startActivity(Intent.createChooser(intent, "Bagikan kepada:"))
    }

}