package co.id.fadlurahmanf.mediaislam.main.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import co.id.fadlurahmanf.mediaislam.core.state.EQuranNetworkState
import co.id.fadlurahmanf.mediaislam.core.ui.bottomsheet.InfoBottomsheet
import co.id.fadlurahmanf.mediaislam.databinding.ActivityMainBinding
import co.id.fadlurahmanf.mediaislam.main.BaseMainActivity
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.SurahModel
import co.id.fadlurahmanf.mediaislam.quran.presentation.surah.DetailSurahActivity
import co.id.fadlurahmanf.mediaislam.quran.presentation.surah.adapter.ListSurahAdapter
import javax.inject.Inject

class MainActivity :
    BaseMainActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onBaseQuranInjectActivity() {
        component.inject(this)
    }

    override fun onBaseQuranCreate(savedInstanceState: Bundle?) {
        setAppearanceLightStatusBar(false)
        initAppBar()
        initAdapter()
        initObserver()

        viewModel.getListSurah()
    }

    private fun initAppBar() {
        binding.toolbar.ivLeading.visibility = View.INVISIBLE
        binding.toolbar.tvTitle.text = "MediaIslam"
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.ivAction.visibility = View.INVISIBLE
    }

    private lateinit var adapter: ListSurahAdapter
    private var listSurah: ArrayList<SurahModel> = arrayListOf()
    private fun initAdapter() {
        adapter = ListSurahAdapter()
        adapter.setCallBack(object : ListSurahAdapter.CallBack {
            override fun onClicked(surah: SurahModel) {
                val intent = Intent(this@MainActivity, DetailSurahActivity::class.java)
                intent.putExtra(DetailSurahActivity.SURAH_NAME, surah.latin)
                intent.putExtra(DetailSurahActivity.SURAH_NO, surah.surahNo)
                startActivity(intent)
            }
        })
        adapter.setList(listSurah)
        binding.rv.adapter = adapter
    }

    private fun initObserver() {
        viewModel.listSurah.observe(this) {
            when (it) {
                is EQuranNetworkState.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.layoutShimmerSurah.root.visibility = View.VISIBLE
                    binding.tvError.visibility = View.GONE
                    binding.rv.visibility = View.GONE
                }

                is EQuranNetworkState.ERROR -> {
                    binding.progressBar.visibility = View.GONE
//                    binding.tvError.text = it.exception.toProperMessage(this)
                    binding.tvError.visibility = View.VISIBLE
                    binding.rv.visibility = View.GONE
                    showFailedBebasBottomsheet(
                        exception = it.exception,
                        isCancelable = false,
                        callback = object : InfoBottomsheet.Callback {
                            override fun onButtonClicked() {
                                dismissFailedBottomsheet()
                                viewModel.getListSurah()
                            }
                        }
                    )
                }

                is EQuranNetworkState.SUCCESS -> {
                    listSurah.clear()
                    listSurah.addAll(it.data)
                    adapter.setList(listSurah)

                    binding.progressBar.visibility = View.GONE
                    binding.tvError.visibility = View.GONE
                    binding.rv.visibility = View.VISIBLE
                    binding.layoutShimmerSurah.root.visibility = View.GONE
                }

                else -> {

                }
            }
        }
    }

}