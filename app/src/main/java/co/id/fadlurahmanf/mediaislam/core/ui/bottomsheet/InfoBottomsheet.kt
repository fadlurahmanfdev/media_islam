package co.id.fadlurahmanf.mediaislam.core.ui.bottomsheet

import android.graphics.drawable.Drawable
import co.id.fadlurahmanf.mediaislam.databinding.BottomsheetInfoBinding

class InfoBottomsheet : BaseBottomsheet<BottomsheetInfoBinding>(
    BottomsheetInfoBinding::inflate
) {

    private var callback: Callback? = null
    private var failedImageDrawble: Drawable? = null
    private var infoId: String? = null

    companion object {
        const val IS_DIALOG_CANCELABLE = "IS_DIALOG_CANCELABLE"
        const val TITLE_TEXT = "TITLE_TEXT"
        const val MESSAGE_TEXT = "MESSAGE_TEXT"
        const val BUTTON_TEXT = "BUTTON_TEXT"
        const val INFO_ID = "INFO_ID"
    }

    override fun setup() {
        infoId = arguments?.getString(INFO_ID)
        isCancelable = arguments?.getBoolean(IS_DIALOG_CANCELABLE, true) ?: true

        if (failedImageDrawble != null) {
            binding.ivAsset.setImageDrawable(failedImageDrawble)
        }

        binding.tvTitle.text = arguments?.getString(TITLE_TEXT)
        binding.tvDesc.text = arguments?.getString(MESSAGE_TEXT)
        binding.btnBottomsheet.setButtonText(arguments?.getString(BUTTON_TEXT) ?: "-")

        dialog?.setCanceledOnTouchOutside(false)

        binding.btnBottomsheet.setOnClickListener {
            if (callback != null) {
                callback?.onButtonClicked(infoId = infoId)
            } else if (isCancelable) {
                dismiss()
            }
        }
    }

    fun setImageDrawable(drawable: Drawable?) {
        if (drawable != null) {
            this.failedImageDrawble = drawable
        }
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    interface Callback {
        fun onButtonClicked(infoId: String?)
    }
}