package co.id.fadlurahmanf.mediaislam.quran.presentation.surah

import android.content.Intent
import android.os.Bundle
import android.view.View
import co.id.fadlurahmanf.mediaislam.core.state.EQuranNetworkState
import co.id.fadlurahmanf.mediaislam.core.ui.bottomsheet.InfoBottomsheet
import co.id.fadlurahmanf.mediaislam.databinding.ActivityListSurahBinding
import co.id.fadlurahmanf.mediaislam.quran.BaseQuranActivity
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.SurahModel
import co.id.fadlurahmanf.mediaislam.quran.presentation.surah.adapter.ListSurahAdapter
import com.google.firebase.analytics.FirebaseAnalytics.Event
import com.google.firebase.analytics.FirebaseAnalytics.Param
import javax.inject.Inject

class ListSurahActivity :
    BaseQuranActivity<ActivityListSurahBinding>(ActivityListSurahBinding::inflate) {

    @Inject
    lateinit var viewModel: SurahViewModel

    override fun onBaseQuranInjectActivity() {
        component.inject(this)
    }

    override fun onBaseQuranCreate(savedInstanceState: Bundle?) {
        firebaseAnalytics.logEvent(Event.SCREEN_VIEW, Bundle().apply {
            putString(Param.VALUE, ListSurahActivity::class.java.simpleName)
        })
        setAppearanceLightStatusBar(false)
        initAppBar()
        initAdapter()
        initObserver()
        initAction()

        viewModel.getListSurah()
    }

    private fun initAction() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getListSurah()
        }
    }

    private fun initAppBar() {
        binding.toolbar.ivLeading.visibility = View.INVISIBLE
        binding.toolbar.tvTitle.text = "Surah"
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.ivAction.visibility = View.INVISIBLE
    }

    private lateinit var adapter: ListSurahAdapter
    private var listSurah: ArrayList<SurahModel> = arrayListOf()
    private fun initAdapter() {
        adapter = ListSurahAdapter()
        adapter.setCallBack(object : ListSurahAdapter.CallBack {
            override fun onClicked(surah: SurahModel) {
                val intent = Intent(this@ListSurahActivity, DetailSurahActivity::class.java)
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
                    binding.swipeRefresh.isRefreshing = false
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

                    binding.swipeRefresh.isRefreshing = false
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