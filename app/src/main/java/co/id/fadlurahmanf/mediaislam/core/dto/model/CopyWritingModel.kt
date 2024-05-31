package co.id.fadlurahmanf.mediaislam.core.dto.model

import androidx.annotation.DrawableRes

data class CopyWritingModel(
    val infoId: String? = null,
    @DrawableRes val asset: Int? = null,
    val title: String,
    val message: String,
    val buttonText: String,
)
