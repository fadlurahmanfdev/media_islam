package co.id.fadlurahmanf.mediaislam.core.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

typealias InflateBottomsheet<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseBottomsheet<VB : ViewBinding>(
    private val inflate: InflateBottomsheet<VB>
) : BottomSheetDialogFragment() {
    private lateinit var _binding: VB
    val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponent()
    }

    open fun initComponent() {}

    abstract fun setup()
}