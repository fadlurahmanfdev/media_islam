package co.id.fadlurahmanf.mediaislam.core.dto.model

import androidx.annotation.DrawableRes

data class BottomsheetModel(
    val infoId: String? = null,
    @DrawableRes val asset: Int? = null,
    val title: String,
    val message: String,
    val buttonText: String,
    val isCancelable: Boolean = true,
)
