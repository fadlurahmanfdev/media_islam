package co.id.fadlurahmanf.mediaislam.main.presentation.landing

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import co.id.fadlurahmanf.mediaislam.core.state.AppState
import co.id.fadlurahmanf.mediaislam.databinding.ActivitySplashBinding
import co.id.fadlurahmanf.mediaislam.main.BaseMainActivity
import co.id.fadlurahmanf.mediaislam.main.presentation.MainActivity
import javax.inject.Inject

class SplashActivity : BaseMainActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    override fun onBaseQuranInjectActivity() {
        component.inject(this)
    }

    @Inject
    lateinit var viewModel: SplashViewModel

    override fun onBaseQuranCreate(savedInstanceState: Bundle?) {
        setAppearanceLightStatusBar(false)

        initObserver()

//        locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_COARSE_LOCATION)

        viewModel.canGoToMainPage()

    }

    private fun initObserver() {
        viewModel.canGoToMainPageLive.observe(this) { state ->
            when (state) {
                is AppState.SUCCESS -> {
                    if (state.data) {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(this, LandingActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                }

                else -> {}
            }
        }
    }

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            PackageManager.PERMISSION_GRANTED
        }
}