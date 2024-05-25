package co.id.fadlurahmanf.mediaislam.main

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import co.id.fadlurahmanf.mediaislam.BaseApp
import co.id.fadlurahmanf.mediaislam.core.network.exception.EQuranException
import co.id.fadlurahmanf.mediaislam.core.ui.BaseActivity
import co.id.fadlurahmanf.mediaislam.core.ui.InflateActivity
import co.id.fadlurahmanf.mediaislam.core.ui.bottomsheet.InfoBottomsheet

abstract class BaseMainActivity<VB : ViewBinding>(inflater: InflateActivity<VB>) :
    BaseActivity<VB>(inflater) {
    lateinit var component: MainSubComponent

    override fun onBaseCreateSubComponent() {
        component =
            (applicationContext as BaseApp).applicationComponent.mainSubComponentFactory()
                .create()
        onBaseQuranInjectActivity()
    }

    abstract fun onBaseQuranInjectActivity()

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        onBaseQuranCreate(savedInstanceState)
    }

    abstract fun onBaseQuranCreate(savedInstanceState: Bundle?)

    private var isBottomsheetOpen: Boolean = false
    private var infoBottomsheet: InfoBottomsheet? = null
    open fun showFailedBebasBottomsheet(
        exception: EQuranException,
        isCancelable: Boolean = true,
        callback: InfoBottomsheet.Callback? = null,
    ) {
        if (isBottomsheetOpen) {
            dismissFailedBottomsheet()
        }
        isBottomsheetOpen = true
        val bundle = Bundle()
        bundle.apply {
            putString(
                InfoBottomsheet.TITLE_TEXT,
                "Oops..."
            )
            putString(
                InfoBottomsheet.MESSAGE_TEXT,
                "Terjadi kesalahan (${exception.httpCode ?: exception.enumCode}). Silahkan coba beberapa saat lagi"
            )
            putString(
                InfoBottomsheet.BUTTON_TEXT,
                "Oke"
            )
            putBoolean(InfoBottomsheet.IS_DIALOG_CANCELABLE, isCancelable)
        }
        infoBottomsheet = InfoBottomsheet()
        infoBottomsheet?.arguments = bundle
        if (callback != null) {
            infoBottomsheet?.setCallback(callback)
        }
        infoBottomsheet?.show(supportFragmentManager, InfoBottomsheet::class.java.simpleName)
    }

    open fun dismissFailedBottomsheet() {
        infoBottomsheet?.dismiss()
        infoBottomsheet = null
        isBottomsheetOpen = false
    }
}