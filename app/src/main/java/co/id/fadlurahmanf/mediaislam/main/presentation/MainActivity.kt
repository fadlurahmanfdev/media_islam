package co.id.fadlurahmanf.mediaislam.main.presentation

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.IntentSenderRequest
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import co.id.fadlurahmanf.mediaislam.article.presentation.ArticleListActivity
import co.id.fadlurahmanf.mediaislam.article.presentation.ArticleWebViewActivity
import co.id.fadlurahmanf.mediaislam.article.presentation.adapter.ArticleAdapter
import co.id.fadlurahmanf.mediaislam.core.analytics.AnalyticEvent
import co.id.fadlurahmanf.mediaislam.core.analytics.AnalyticParam
import co.id.fadlurahmanf.mediaislam.core.network.dto.response.article.ArticleItemResponse
import co.id.fadlurahmanf.mediaislam.core.network.exception.toSimpleCopyWriting
import co.id.fadlurahmanf.mediaislam.core.state.AladhanNetworkState
import co.id.fadlurahmanf.mediaislam.core.state.AppState
import co.id.fadlurahmanf.mediaislam.core.state.ArticleNetworkState
import co.id.fadlurahmanf.mediaislam.core.state.EQuranNetworkState
import co.id.fadlurahmanf.mediaislam.databinding.ActivityMainBinding
import co.id.fadlurahmanf.mediaislam.main.BaseMainActivity
import co.id.fadlurahmanf.mediaislam.main.data.dto.model.ItemMainMenuModel
import co.id.fadlurahmanf.mediaislam.main.presentation.adapter.MenuAdapter
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.SurahModel
import co.id.fadlurahmanf.mediaislam.quran.presentation.audio.ListAudioActivity
import co.id.fadlurahmanf.mediaislam.quran.presentation.surah.DetailSurahActivity
import co.id.fadlurahmanf.mediaislam.quran.presentation.surah.ListSurahActivity
import co.id.fadlurahmanf.mediaislam.quran.presentation.surah.adapter.ListSurahAdapter
import co.id.fadlurahmanfdev.kotlin_core_platform.domain.plugin.CorePlatformLocationManager
import com.google.firebase.analytics.FirebaseAnalytics.Event
import com.google.firebase.analytics.FirebaseAnalytics.Param
import javax.inject.Inject


class MainActivity :
    BaseMainActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private lateinit var corePlatformLocationManager: CorePlatformLocationManager

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onBaseMainInjectActivity() {
        component.inject(this)
    }

    override fun onBaseMainCreate(savedInstanceState: Bundle?) {
        setOnApplyWindowInsetsListener(binding.main)
        setAppearanceLightStatusBar(false)

        firebaseAnalytics.logEvent(Event.SCREEN_VIEW, Bundle().apply {
            putString(Param.VALUE, MainActivity::class.java.simpleName)
        })

        corePlatformLocationManager = CorePlatformLocationManager(this)

        initAppBar()
        initMenuAdapter()
        initSurahAdapter()
        initArticleAdapter()
        initObserver()
        initAction()

        viewModel.getMainMenu()
        getPrayerTime()
        viewModel.getFirst10Surah()
        viewModel.getTop3Article()
    }

    private fun getPrayerTime() {
        val isPermissionEnabled = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (isPermissionEnabled) {
            corePlatformLocationManager.requestLocationService(
                this,
                object : CorePlatformLocationManager.RequestLocationServiceCallback {
                    override fun onFailure(exception: Exception) {}

                    override fun onLocationServiceEnabled(enabled: Boolean) {
                        viewModel.setPermissionLocation(enabled)
                        if (enabled) {
                            binding.llInfoLocationPermission.visibility = View.GONE
                            viewModel.getPrayerTime()
                        } else {
                            binding.llInfoLocationPermission.visibility = View.VISIBLE
                        }
                    }

                    override fun onShouldShowPromptServiceDialog(intentSenderRequest: IntentSenderRequest) {

                    }
                })
        } else {
            binding.llInfoLocationPermission.visibility = View.VISIBLE
        }
    }

    private fun initAction() {
        binding.swipeRefresh.setOnRefreshListener {
            getPrayerTime()
            viewModel.getFirst10Surah()
            viewModel.getTop3Article()
        }

        binding.ivAlarm.setOnClickListener {
            val intent = Intent(
                this,
                Class.forName("co.id.fadlurahmanf.mediaislam.alarm.presentation.AlarmActivity")
            )
            startActivity(intent)
        }

        binding.llViewAllSurah.setOnClickListener {
            firebaseAnalytics.logEvent(AnalyticEvent.VIEW_ALL, Bundle().apply {
                putString(AnalyticParam.MENU, "surah")
            })
            val intent = Intent(this@MainActivity, ListSurahActivity::class.java)
            startActivity(intent)
        }

        binding.llViewAllArticle.setOnClickListener {
            firebaseAnalytics.logEvent(AnalyticEvent.VIEW_ALL, Bundle().apply {
                putString(AnalyticParam.MENU, "article")
            })
            val intent = Intent(this@MainActivity, ArticleListActivity::class.java)
            startActivity(intent)
        }

        binding.btnGoToSetting.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.setData(uri)
            startActivity(intent)
        }
    }

    private fun initAppBar() {
        binding.toolbar.ivLeading.visibility = View.INVISIBLE
        binding.toolbar.tvTitle.text = "MediaIslam"
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.ivAction.visibility = View.INVISIBLE
    }

    private lateinit var menuAdapter: MenuAdapter
    private val menus: ArrayList<ItemMainMenuModel> = arrayListOf<ItemMainMenuModel>()

    private fun initMenuAdapter() {
        menuAdapter = MenuAdapter()
        menuAdapter.setCallBack(object : MenuAdapter.CallBack {
            override fun onClicked(menu: ItemMainMenuModel) {
                when (menu.id) {
                    "SURAH" -> {
                        firebaseAnalytics.logEvent(AnalyticEvent.SELECT_MENU, Bundle().apply {
                            putString(AnalyticParam.MENU, "surah")
                            putString(AnalyticParam.FROM, "main_menu")
                        })
                        val intent = Intent(this@MainActivity, ListSurahActivity::class.java)
                        startActivity(intent)
                    }

                    "ARTICLE" -> {
                        firebaseAnalytics.logEvent(AnalyticEvent.SELECT_MENU, Bundle().apply {
                            putString(AnalyticParam.MENU, "article")
                            putString(AnalyticParam.FROM, "main_menu")
                        })
                        val intent = Intent(this@MainActivity, ArticleListActivity::class.java)
                        startActivity(intent)
                    }

                    "AUDIO" -> {
                        firebaseAnalytics.logEvent(AnalyticEvent.SELECT_MENU, Bundle().apply {
                            putString(AnalyticParam.MENU, "audio")
                            putString(AnalyticParam.FROM, "main_menu")
                        })
                        val intent = Intent(this@MainActivity, ListAudioActivity::class.java)
                        startActivity(intent)
                    }

                    "ADHAN" -> {
                        firebaseAnalytics.logEvent(AnalyticEvent.SELECT_MENU, Bundle().apply {
                            putString(AnalyticParam.MENU, "adhan")
                            putString(AnalyticParam.FROM, "main_menu")
                        })
                        val intent = Intent(
                            this@MainActivity,
                            Class.forName("co.id.fadlurahmanf.mediaislam.alarm.presentation.AlarmActivity")
                        )
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

    private lateinit var articleAdapter: ArticleAdapter
    private var articles: ArrayList<ArticleItemResponse> = arrayListOf()
    private fun initArticleAdapter() {
        articleAdapter = ArticleAdapter()
        articleAdapter.setCallBack(object : ArticleAdapter.CallBack {
            override fun onClicked(article: ArticleItemResponse) {
//                firebaseAnalytics.logEvent(Event.SELECT_ITEM, Bundle().apply {
//                    putInt(AnalyticParam.SURAH_NO, surah.surahNo)
//                    putString(AnalyticParam.SURAH_TITLE, surah.latin)
//                })
                val intent = Intent(this@MainActivity, ArticleWebViewActivity::class.java)
                intent.putExtra(ArticleWebViewActivity.URL, article.url)
                startActivity(intent)
            }
        })
        articleAdapter.setList(articles)
        binding.rvTop3Articl.adapter = articleAdapter
    }

    private fun initObserver() {
        viewModel.progressBarVisible.observe(this) { isVisble ->
            binding.progressBar.visibility = if (isVisble) View.VISIBLE else View.GONE
        }

        viewModel.mainMenusLive.observe(this) { state ->
            when (state) {
                is AppState.SUCCESS -> {
                    menus.addAll(state.data)
                    menuAdapter.setList(menus)
                }

                else -> {

                }
            }
        }

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

        viewModel.listSurahLive.observe(this) {
            when (it) {
                is EQuranNetworkState.LOADING -> {
                    binding.layoutShimmerSurah.root.visibility = View.VISIBLE
                    binding.layoutError.root.visibility = View.GONE
                    binding.rv.visibility = View.GONE
                }

                is EQuranNetworkState.ERROR -> {
                    binding.swipeRefresh.isRefreshing = false
                    binding.layoutShimmerSurah.root.visibility = View.GONE
                    binding.rv.visibility = View.GONE

                    val model = it.exception.toSimpleCopyWriting()
                    binding.layoutError.tvTitle.text = model.title
                    binding.layoutError.tvDesc.text = model.message
                    binding.layoutError.root.visibility = View.VISIBLE
                    binding.layoutError.btnRetry.setOnClickListener {
                        viewModel.getFirst10Surah()
                    }
                }

                is EQuranNetworkState.SUCCESS -> {
                    listSurah.clear()
                    listSurah.addAll(it.data)
                    surahAdapter.setList(listSurah)

                    binding.swipeRefresh.isRefreshing = false
                    binding.layoutError.root.visibility = View.GONE
                    binding.rv.visibility = View.VISIBLE
                    binding.layoutShimmerSurah.root.visibility = View.GONE
                }

                else -> {}
            }
        }

        viewModel.articleNetworkLive.observe(this) {
            when (it) {
                is ArticleNetworkState.LOADING -> {
                    binding.layoutShimmerArticle.root.visibility = View.VISIBLE
                    binding.rvTop3Articl.visibility = View.GONE
                }

                is ArticleNetworkState.ERROR -> {

                }

                is ArticleNetworkState.SUCCESS -> {
                    articles.clear()
                    articles.addAll(it.data)
                    articleAdapter.setList(articles)

                    binding.rvTop3Articl.visibility = View.VISIBLE
                    binding.layoutShimmerArticle.root.visibility = View.GONE
                }

                else -> {}
            }
        }
    }

}