package co.id.fadlurahmanf.mediaislam.main.presentation.alarm

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import co.id.fadlurahmanf.mediaislam.R
import co.id.fadlurahmanf.mediaislam.core.state.AladhanNetworkState
import co.id.fadlurahmanf.mediaislam.databinding.ActivityAlarmBinding
import co.id.fadlurahmanf.mediaislam.main.BaseMainActivity
import co.id.fadlurahmanf.mediaislam.main.data.dto.model.PrayersTimeModel
import javax.inject.Inject

class AlarmActivity : BaseMainActivity<ActivityAlarmBinding>(ActivityAlarmBinding::inflate) {
    @Inject
    lateinit var viewModel: AlarmViewModel

    override fun onBaseMainInjectActivity() {
        component.inject(this)
    }

    override fun onBaseMainCreate(savedInstanceState: Bundle?) {
        setAppearanceLightStatusBar(false)
        setOnApplyWindowInsetsListener(binding.main)

        binding.toolbar.ivLeading.setOnClickListener {
            finish()
        }

        binding.toolbar.tvTitle.text = "Alarm"
        binding.toolbar.tvTitle.visibility = View.VISIBLE

        initObserver()

        viewModel.getTodayPrayerTime()
    }

    private fun initObserver() {
        viewModel.prayersTimeLive.observe(this) { state ->
            when (state) {
                is AladhanNetworkState.LOADING -> {
                    binding.layoutLoading.root.visibility = View.VISIBLE
                    binding.llSuccess.visibility = View.GONE
                }

                is AladhanNetworkState.SUCCESS -> {
                    binding.layoutLoading.root.visibility = View.GONE
                    binding.llSuccess.visibility = View.VISIBLE

                    setPrayerTime(state.data)
                }

                else -> {

                }
            }
        }
    }

    private fun setPrayerTime(data: PrayersTimeModel) {
        binding.itemFajr.ivIconPrayer.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.fajr
            )
        )
        binding.itemFajr.tvPrayerTime.text = data.timing.fajr

        binding.itemDhuhr.ivIconPrayer.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.mipmap.dhuhr
            )
        )
        binding.itemDhuhr.tvPrayerTime.text = data.timing.dhuhr

        binding.itemAsr.ivIconPrayer.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.mipmap.asr
            )
        )
        binding.itemAsr.tvPrayerTime.text = data.timing.asr

        binding.itemMaghrib.ivIconPrayer.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.maghrib
            )
        )
        binding.itemMaghrib.tvPrayerTime.text = data.timing.maghrib

        binding.itemIsha.ivIconPrayer.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.isha
            )
        )
        binding.itemIsha.tvPrayerTime.text = data.timing.isha
    }

}