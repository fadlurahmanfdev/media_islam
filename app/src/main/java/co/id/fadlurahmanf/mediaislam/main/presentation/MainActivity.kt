package co.id.fadlurahmanf.mediaislam.main.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import co.id.fadlurahmanf.mediaislam.R
import co.id.fadlurahmanf.mediaislam.core.analytics.AnalyticEvent
import co.id.fadlurahmanf.mediaislam.core.analytics.AnalyticParam
import co.id.fadlurahmanf.mediaislam.core.network.exception.toSimpleCopyWriting
import co.id.fadlurahmanf.mediaislam.core.state.AladhanNetworkState
import co.id.fadlurahmanf.mediaislam.core.state.EQuranNetworkState
import co.id.fadlurahmanf.mediaislam.core.ui.bottomsheet.InfoBottomsheet
import co.id.fadlurahmanf.mediaislam.databinding.ActivityMainBinding
import co.id.fadlurahmanf.mediaislam.main.BaseMainActivity
import co.id.fadlurahmanf.mediaislam.main.data.dto.model.ItemMainMenuModel
import co.id.fadlurahmanf.mediaislam.main.presentation.adapter.MenuAdapter
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.SurahModel
import co.id.fadlurahmanf.mediaislam.quran.presentation.surah.DetailSurahActivity
import co.id.fadlurahmanf.mediaislam.quran.presentation.surah.ListSurahActivity
import co.id.fadlurahmanf.mediaislam.quran.presentation.surah.adapter.ListSurahAdapter
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject
import com.google.firebase.analytics.FirebaseAnalytics.Event
import com.google.firebase.analytics.FirebaseAnalytics.Param

class MainActivity :
    BaseMainActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onBaseQuranInjectActivity() {
        component.inject(this)
    }

    override fun onBaseQuranCreate(savedInstanceState: Bundle?) {
        firebaseAnalytics.logEvent(Event.SCREEN_VIEW, Bundle().apply {
            putString(Param.VALUE, MainActivity::class.java.simpleName)
        })
        setOnApplyWindowInsetsListener(binding.main)
        setAppearanceLightStatusBar(false)
        initAppBar()
        initMenuAdapter()
        initSurahAdapter()
        initObserver()
        initAction()

        viewModel.getPrayerTime(this)
        viewModel.getListSurah()
    }

    private fun initAction() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getPrayerTime(this)
            viewModel.getListSurah()
        }
    }

    private fun initAppBar() {
        binding.toolbar.ivLeading.visibility = View.INVISIBLE
        binding.toolbar.tvTitle.text = "MediaIslam"
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.ivAction.visibility = View.INVISIBLE
    }

    private lateinit var menuAdapter: MenuAdapter
    val menus = arrayListOf(
        ItemMainMenuModel(
            id = "SURAH",
            title = "Surat",
            icon = R.drawable.il_iqra,
        )
    )

    private fun initMenuAdapter() {
        menuAdapter = MenuAdapter()
        menuAdapter.setCallBack(object : MenuAdapter.CallBack {
            override fun onClicked(menu: ItemMainMenuModel) {
                when (menu.id) {
                    "SURAH" -> {
                        val intent = Intent(this@MainActivity, ListSurahActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        })
        menuAdapter.setList(menus)
        val lm = GridLayoutManager(this, 4)
        binding.rvMainMenu.adapter = menuAdapter
        binding.rvMainMenu.layoutManager = lm
    }

    private lateinit var surahAdapter: ListSurahAdapter
    private var listSurah: ArrayList<SurahModel> = arrayListOf()
    private fun initSurahAdapter() {
        surahAdapter = ListSurahAdapter()
        surahAdapter.setCallBack(object : ListSurahAdapter.CallBack {
            override fun onClicked(surah: SurahModel) {
                firebaseAnalytics.logEvent(Event.SELECT_ITEM, Bundle().apply {
                    putInt(AnalyticParam.SURAH_NO, surah.surahNo)
                    putString(AnalyticParam.SURAH_TITLE, surah.latin)
                })
                val intent = Intent(this@MainActivity, DetailSurahActivity::class.java)
                intent.putExtra(DetailSurahActivity.SURAH_NAME, surah.latin)
                intent.putExtra(DetailSurahActivity.SURAH_NO, surah.surahNo)
                startActivity(intent)
            }
        })
        surahAdapter.setList(listSurah)
        binding.rv.adapter = surahAdapter
    }

    private fun initObserver() {
        viewModel.prayersTimeLive.observe(this) { state ->
            when (state) {
                is AladhanNetworkState.SUCCESS -> {
                    binding.tvHijriDate.text = state.data.readableDateInHijr
                    binding.tvDate.text = state.data.readableDate
                    binding.tvLocation.text = state.data.location

                    binding.prayerTiming.tvFajrTime.text = state.data.timing.fajr
                    binding.prayerTiming.tvDhuhrTime.text = state.data.timing.dhuhr
                    binding.prayerTiming.tvAsrTime.text = state.data.timing.asr
                    binding.prayerTiming.tvMaghribTime.text = state.data.timing.maghrib
                    binding.prayerTiming.tvIshaTime.text = state.data.timing.isha

                    binding.llPrayerTime.visibility = View.VISIBLE
                }

                else -> {
                    binding.llPrayerTime.visibility = View.GONE
                }
            }
        }

        viewModel.listSurah.observe(this) {
            when (it) {
                is EQuranNetworkState.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.layoutShimmerSurah.root.visibility = View.VISIBLE
                    binding.layoutError.root.visibility = View.GONE
                    binding.rv.visibility = View.GONE
                }

                is EQuranNetworkState.ERROR -> {

                    binding.swipeRefresh.isRefreshing = false
                    binding.progressBar.visibility = View.GONE
                    binding.layoutShimmerSurah.root.visibility = View.GONE
                    binding.rv.visibility = View.GONE

                    val model = it.exception.toSimpleCopyWriting()
                    binding.layoutError.tvTitle.text = model.title
                    binding.layoutError.tvDesc.text = model.message
                    binding.layoutError.root.visibility = View.VISIBLE
                    showFailedBebasBottomsheet(
                        exception = it.exception,
                        isCancelable = false,
                        callback = object : InfoBottomsheet.Callback {
                            override fun onButtonClicked(infoId: String?) {
                                dismissFailedBottomsheet()
                                viewModel.getListSurah()
                            }
                        }
                    )
                }

                is EQuranNetworkState.SUCCESS -> {
                    listSurah.clear()
                    listSurah.addAll(it.data)
                    surahAdapter.setList(listSurah)

                    binding.swipeRefresh.isRefreshing = false
                    binding.progressBar.visibility = View.GONE
                    binding.layoutError.root.visibility = View.GONE
                    binding.rv.visibility = View.VISIBLE
                    binding.layoutShimmerSurah.root.visibility = View.GONE
                }

                else -> {}
            }
        }
    }

}