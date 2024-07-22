package co.id.fadlurahmanf.mediaislam.main.domain.usecase

import co.id.fadlurahmanf.mediaislam.main.data.dto.model.ItemMainMenuModel
import io.reactivex.rxjava3.core.Observable

interface MenuUseCase {
    fun getMainMenus(): Observable<List<ItemMainMenuModel>>
}