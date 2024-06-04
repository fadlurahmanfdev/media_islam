package co.id.fadlurahmanf.mediaislam

import android.app.Application
import co.id.fadlurahmanf.mediaislam.core.di.components.ApplicationComponent
import co.id.fadlurahmanf.mediaislam.core.di.components.DaggerApplicationComponent
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class BaseApp : Application() {
    lateinit var applicationComponent: ApplicationComponent
    lateinit var firebaseAnalytics: FirebaseAnalytics
    lateinit var firebaseRemoteConfig: FirebaseRemoteConfig

    //    lateinit var firebaseCrashlytics: FirebaseCrashlytics
    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(applicationContext)
        firebaseAnalytics = FirebaseAnalytics.getInstance(applicationContext)
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
//        firebaseCrashlytics = FirebaseCrashlytics.getInstance()
    }
}