package co.id.fadlurahmanf.mediaislam.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding
import co.id.fadlurahmanf.mediaislam.BaseApp
import com.google.firebase.analytics.FirebaseAnalytics

typealias InflateActivity<T> = (LayoutInflater) -> T

abstract class BaseActivity<VB : ViewBinding>(
    private val inflater: InflateActivity<VB>
) : AppCompatActivity() {

    lateinit var firebaseAnalytics: FirebaseAnalytics

    private lateinit var _binding: VB
    val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        firebaseAnalytics = (application as BaseApp).firebaseAnalytics
        onBaseBindingActivity()
        onBaseCreateSubComponent()
        onBaseCreate(savedInstanceState)
    }

    abstract fun onBaseCreateSubComponent()

    open fun onBaseBindingActivity() {
        _binding = inflater.invoke(layoutInflater)
        setContentView(_binding.root)
    }

    abstract fun onBaseCreate(savedInstanceState: Bundle?)

    open fun setOnApplyWindowInsetsListener(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, 0, 0, systemBars.bottom)
            insets
        }
    }

    fun setAppearanceLightStatusBar(isLight: Boolean) {
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars =
            isLight
    }
}