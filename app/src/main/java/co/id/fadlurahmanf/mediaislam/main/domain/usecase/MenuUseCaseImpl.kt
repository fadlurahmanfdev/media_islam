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
        return when (menuId.uppercase()) {
            "SURAH" -> R.drawable.il_iqra
            "ARTICLE" -> R.drawable.il_books
            "AUDIO" -> R.drawable.il_headphone
            "ADHAN" -> R.drawable.adzan
            else -> R.drawable.il_media_islam
        }
    }

    override fun getMainMenus(): Observable<List<ItemMainMenuModel>> {
        return Observable.create<List<ItemMainMenuModel>> { emitter ->
            firebaseRemoteConfig.reset().addOnSuccessListener {
                println("INIT SUCCESS RESET")
                firebaseRemoteConfig.fetchAndActivate().addOnSuccessListener {
                    val mainMenuString = firebaseRemoteConfig.getString("MAIN_MENU")
                    println("MASUK mainMenuString: $mainMenuString")
                    val rawResponseMainMenus =
                        Gson().fromJson(mainMenuString, mutableListOf<Map<String, Any>>().javaClass)
                    val mainMenus = arrayListOf<ItemMainMenuModel>()
                    println("MASUK rawResponseMainMenus: $rawResponseMainMenus")
                    rawResponseMainMenus.filter { element ->
                        println("MASUK ELEMENT: $element")
                        val itemMenuResponse = Gson().fromJson<ItemMenuResponse>(Gson().toJsonTree(element), ItemMenuResponse::class.java)
                        itemMenuResponse.visible
                    }.forEach { element ->
                        val itemMenuResponse = Gson().fromJson<ItemMenuResponse>(Gson().toJsonTree(element), ItemMenuResponse::class.java)
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
                    println("SUCCESS ON NEXT FETCH ACTIVATE")
                }.addOnFailureListener {
                    println("ERROR FETCH ACTIVATE")
//                    emitter.onError(it)
                }.addOnCompleteListener {
                    println("COMPLETE FETCH ACTIVATE")
//                    emitter.onComplete()
                }
            }.addOnFailureListener {
                println("ERROR RESET")
//                emitter.onError(it)
            }.addOnCompleteListener {
                println("COMPLETE RESET")
//                emitter.onComplete()
            }
        }
    }
}