package co.id.fadlurahmanf.mediaislam.alarm

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import co.id.fadlurahmanf.mediaislam.BaseApp
import co.id.fadlurahmanf.mediaislam.core.ui.BaseActivity
import co.id.fadlurahmanf.mediaislam.core.ui.InflateActivity
import co.id.fadlurahmanf.mediaislam.core.ui.bottomsheet.InfoBottomsheet

abstract class BaseAlarmActivity<VB : ViewBinding>(inflater: InflateActivity<VB>) :
    BaseActivity<VB>(inflater) {
    lateinit var component: AlarmSubComponent

    override fun onBaseCreateSubComponent() {
        component =
            (applicationContext as BaseApp).applicationComponent.alarmSubComponentFactory()
                .create()
        onBaseAlarmInjectActivity()
    }

    abstract fun onBaseAlarmInjectActivity()

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        onBaseAlarmCreate(savedInstanceState)
    }

    abstract fun onBaseAlarmCreate(savedInstanceState: Bundle?)

    private var isBottomsheetOpen: Boolean = false
    private var infoBottomsheet: InfoBottomsheet? = null
//    open fun showFailedBebasBottomsheet(
//        exception: EQuranException,
//        isCancelable: Boolean = true,
//        callback: InfoBottomsheet.Callback? = null,
//    ) {
//        if (isBottomsheetOpen) {
//            dismissFailedBottomsheet()
//        }
//        isBottomsheetOpen = true
//        val model = exception.toBottomsheetModel()
//        val bundle = Bundle()
//        bundle.apply {
//            putString(
//                InfoBottomsheet.INFO_ID,
//                model.infoId
//            )
//            putString(
//                InfoBottomsheet.TITLE_TEXT,
//                model.title
//            )
//            putString(
//                InfoBottomsheet.MESSAGE_TEXT,
//                model.message
//            )
//            putString(
//                InfoBottomsheet.BUTTON_TEXT,
//                model.buttonText
//            )
//            putBoolean(InfoBottomsheet.IS_DIALOG_CANCELABLE, isCancelable)
//        }
//        infoBottomsheet = InfoBottomsheet()
//        infoBottomsheet?.arguments = bundle
//        if (callback != null) {
//            infoBottomsheet?.setCallback(callback)
//        }
//        infoBottomsheet?.show(supportFragmentManager, InfoBottomsheet::class.java.simpleName)
//    }
//
//    open fun showFailedBebasBottomsheet(
//        model: BottomsheetModel,
//        callback: InfoBottomsheet.Callback? = null,
//    ) {
//        if (isBottomsheetOpen) {
//            dismissFailedBottomsheet()
//        }
//        isBottomsheetOpen = true
//        val bundle = Bundle()
//        bundle.apply {
//            putString(
//                InfoBottomsheet.INFO_ID,
//                model.infoId
//            )
//            putString(
//                InfoBottomsheet.TITLE_TEXT,
//                model.title
//            )
//            putString(
//                InfoBottomsheet.MESSAGE_TEXT,
//                model.message
//            )
//            putString(
//                InfoBottomsheet.BUTTON_TEXT,
//                model.buttonText
//            )
//            putBoolean(InfoBottomsheet.IS_DIALOG_CANCELABLE, model.isCancelable)
//        }
//        infoBottomsheet = InfoBottomsheet()
//        infoBottomsheet?.arguments = bundle
//        if (callback != null) {
//            infoBottomsheet?.setCallback(callback)
//        }
//        infoBottomsheet?.show(supportFragmentManager, InfoBottomsheet::class.java.simpleName)
//    }
//
//    open fun dismissFailedBottomsheet() {
//        infoBottomsheet?.dismiss()
//        infoBottomsheet = null
//        isBottomsheetOpen = false
//    }
}