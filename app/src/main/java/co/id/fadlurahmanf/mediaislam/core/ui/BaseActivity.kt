package co.id.fadlurahmanf.mediaislam.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding

typealias InflateActivity<T> = (LayoutInflater) -> T

abstract class BaseActivity<VB : ViewBinding>(
    private val inflater: InflateActivity<VB>
) : AppCompatActivity() {

    private lateinit var _binding: VB
    val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
            v.setPadding(view.paddingLeft, systemBars.top, view.paddingRight, systemBars.bottom)
            insets
        }
    }
}