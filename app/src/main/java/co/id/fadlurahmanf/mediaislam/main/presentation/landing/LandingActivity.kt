package co.id.fadlurahmanf.mediaislam.main.presentation.landing

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import co.id.fadlurahmanf.mediaislam.databinding.ActivityLandingBinding
import co.id.fadlurahmanf.mediaislam.main.BaseMainActivity
import co.id.fadlurahmanf.mediaislam.main.presentation.MainActivity
import co.id.fadlurahmanfdev.kotlin_core_platform.domain.plugin.CorePlatformLocationManager
import javax.inject.Inject

class LandingActivity : BaseMainActivity<ActivityLandingBinding>(ActivityLandingBinding::inflate),
    CorePlatformLocationManager.RequestLocationServiceCallback {
    lateinit var corePlatformLocationManager: CorePlatformLocationManager

    @Inject
    lateinit var viewModel: SplashViewModel
    override fun onBaseMainInjectActivity() {
        component.inject(this)
    }

    private val launcherLocationService =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            goToMainPage()
        }

    private fun goToMainPage() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private val launcherPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                corePlatformLocationManager.requestLocationService(this, this)
            }
        }

    override fun onBaseMainCreate(savedInstanceState: Bundle?) {
        setOnApplyWindowInsetsListener(binding.main)

        corePlatformLocationManager = CorePlatformLocationManager(this)

        binding.btnActivateLocation.setOnClickListener {
            launcherPermission.launch(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    override fun onFailure(exception: Exception) {

    }

    override fun onLocationServiceEnabled(enabled: Boolean) {
        goToMainPage()
    }

    override fun onShouldShowPromptServiceDialog(intentSenderRequest: IntentSenderRequest) {
        launcherLocationService.launch(intentSenderRequest)
    }

}