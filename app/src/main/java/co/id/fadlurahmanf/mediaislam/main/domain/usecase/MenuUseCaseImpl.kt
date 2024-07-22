package co.id.fadlurahmanf.mediaislam.main.domain.usecase

import androidx.annotation.DrawableRes
import co.id.fadlurahmanf.mediaislam.R
import co.id.fadlurahmanf.mediaislam.main.data.dto.model.ItemMainMenuModel
import co.id.fadlurahmanf.mediaislam.main.data.dto.response.ItemMenuResponse
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class MenuUseCaseImpl @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig
) : MenuUseCase {

    @DrawableRes
    private fun getIconBasedOnId(menuId: String): Int {
        return when (menuId) {
            "SURAH" -> R.drawable.il_iqra
            "ARTICLE" -> R.drawable.il_books
            else -> R.drawable.il_media_islam
        }
    }

    override fun getMainMenus(): Observable<List<ItemMainMenuModel>> {
        return Observable.create<List<ItemMainMenuModel>> { emitter ->
            firebaseRemoteConfig.fetchAndActivate().addOnSuccessListener {
                val mainMenuString = firebaseRemoteConfig.getString("MAIN_MENU")
                val rawResponseMainMenus =
                    Gson().fromJson(mainMenuString, mutableListOf<Map<String, Any>>().javaClass)
                val mainMenus = arrayListOf<ItemMainMenuModel>()
                rawResponseMainMenus.filter { element ->
                    val itemMenuResponse = Gson().fromJson<ItemMenuResponse>(element.toString(), ItemMenuResponse::class.java)
                    itemMenuResponse.visible
                }.forEach { element ->
                    val itemMenuResponse = Gson().fromJson<ItemMenuResponse>(element.toString(), ItemMenuResponse::class.java)
                    mainMenus.add(
                        ItemMainMenuModel(
                            id = itemMenuResponse.id ?: "-",
                            title = itemMenuResponse.title ?: "-",
                            icon = getIconBasedOnId(itemMenuResponse.id ?: "-"),
                            active = itemMenuResponse.active,
                        ),
                    )
                }
                emitter.onNext(mainMenus.toList())
            }.addOnFailureListener {
                emitter.onError(it)
            }.addOnCompleteListener {
                emitter.onComplete()
            }
        }
    }
}