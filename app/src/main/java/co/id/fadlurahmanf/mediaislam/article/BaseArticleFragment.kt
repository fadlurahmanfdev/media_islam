package co.id.fadlurahmanf.mediaislam.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import co.id.fadlurahmanf.mediaislam.BaseApp
import co.id.fadlurahmanf.mediaislam.core.di.components.ApplicationComponent

typealias InflateFragment<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseArticleFragment<VB : ViewBinding>(
    var inflate: InflateFragment<VB>
) : Fragment() {

    private lateinit var _appComponent: ApplicationComponent
    val articleComponent get() = _appComponent.articleSubComponentFactory().create()

    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        initAppComponent()
        inject()
        super.onCreate(savedInstanceState)
    }

    private fun initAppComponent() {
        _appComponent = (activity?.applicationContext as BaseApp).applicationComponent
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup(savedInstanceState)
    }

    abstract fun setup(savedInstanceState: Bundle?)

    abstract fun inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }
}