package co.id.fadlurahmanf.mediaislam

import android.app.Application
import co.id.fadlurahmanf.mediaislam.core.di.components.ApplicationComponent
import co.id.fadlurahmanf.mediaislam.core.di.components.DaggerApplicationComponent
import com.google.firebase.analytics.FirebaseAnalytics

class BaseApp : Application() {
    lateinit var applicationComponent: ApplicationComponent
    lateinit var firebaseAnalytics: FirebaseAnalytics

    //    lateinit var firebaseCrashlytics: FirebaseCrashlytics
    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(applicationContext)
        firebaseAnalytics = FirebaseAnalytics.getInstance(applicationContext)
//        firebaseCrashlytics = FirebaseCrashlytics.getInstance()
    }
}