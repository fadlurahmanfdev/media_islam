package co.id.fadlurahmanf.mediaislam

import android.app.Application
import co.id.fadlurahmanf.mediaislam.core.di.components.ApplicationComponent
import co.id.fadlurahmanf.mediaislam.core.di.components.DaggerApplicationComponent

class BaseApp:Application() {
    lateinit var applicationComponent: ApplicationComponent
//    lateinit var firebaseCrashlytics: FirebaseCrashlytics
    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(applicationContext)
//        firebaseCrashlytics = FirebaseCrashlytics.getInstance()
    }
}