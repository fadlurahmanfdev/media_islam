package co.id.fadlurahmanf.mediaislam.core.ui.button

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.Button
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import co.id.fadlurahmanf.mediaislam.R

class ButtonCustomFilled(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

    private var attributes: TypedArray
    private var button: Button

    private var text: String
    private var buttonBackground: Drawable
    private var active: Boolean

    init {
        inflate(context, R.layout.button_custom_filled, this)
        attributes = context.obtainStyledAttributes(attributeSet, R.styleable.ButtonCustomFilled)

        button = findViewById(R.id.btn)

        text = attributes.getString(R.styleable.ButtonCustomFilled_text) ?: ""
        buttonBackground = attributes.getDrawable(R.styleable.ButtonCustomFilled_buttonBackground)
            ?: ContextCompat.getDrawable(context, R.drawable.rounded_primary_button_background)!!
        active = attributes.getBoolean(R.styleable.ButtonCustomFilled_active, true)

        setup()
    }

    private fun setup() {
        setButtonText(text)
        setButtonColor(background = buttonBackground, active = active)
    }

    private fun setButtonColor(background: Drawable, active: Boolean) {
        this.active = active
        if (active) {
            button.background = background
            button.setOnClickListener(listener)
            button.setTextColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )
            )
        } else {
            button.background =
                ContextCompat.getDrawable(context, R.drawable.rounded_grey_button_background)
            button.setOnClickListener(null)
            button.setTextColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )
            )
        }
    }

    fun setButtonText(text: String) {
        button.text = text
    }

    private var listener: OnClickListener? = null

    override fun setOnClickListener(l: OnClickListener?) {
        if (active) {
            listener = l
            button.setOnClickListener(l)
        }
    }
}