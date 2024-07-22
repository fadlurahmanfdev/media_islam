package co.id.fadlurahmanf.mediaislam.main.data.dto.model

import androidx.annotation.DrawableRes

data class ItemMainMenuModel(
    val id: String,
    val title: String,
    @DrawableRes val icon: Int,
    val active: Boolean = true,
)