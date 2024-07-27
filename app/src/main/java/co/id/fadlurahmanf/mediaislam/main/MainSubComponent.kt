package co.id.fadlurahmanf.mediaislam.main

import co.id.fadlurahmanf.mediaislam.main.presentation.MainActivity
import co.id.fadlurahmanf.mediaislam.main.presentation.alarm.AlarmActivity
import co.id.fadlurahmanf.mediaislam.main.presentation.landing.LandingActivity
import co.id.fadlurahmanf.mediaislam.main.presentation.landing.SplashActivity
import dagger.Subcomponent

@Subcomponent
interface MainSubComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): MainSubComponent
    }

    fun inject(activity: SplashActivity)
    fun inject(activity: LandingActivity)
    fun inject(activity: MainActivity)
    fun inject(activity: AlarmActivity)
}