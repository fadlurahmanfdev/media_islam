package co.id.fadlurahmanf.mediaislam.main.presentation.landing

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import co.id.fadlurahmanf.mediaislam.databinding.ActivitySplashBinding
import co.id.fadlurahmanf.mediaislam.main.BaseMainActivity
import co.id.fadlurahmanf.mediaislam.main.presentation.MainActivity
import javax.inject.Inject

class SplashActivity : BaseMainActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    override fun onBaseMainInjectActivity() {
        component.inject(this)
    }

    @Inject
    lateinit var viewModel: SplashViewModel

    override fun onBaseMainCreate(savedInstanceState: Bundle?) {
        setAppearanceLightStatusBar(false)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }, 1500)

    }
}