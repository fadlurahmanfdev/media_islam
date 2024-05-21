package co.id.fadlurahmanf.mediaislam.main

import co.id.fadlurahmanf.mediaislam.main.presentation.MainActivity
import dagger.Subcomponent

@Subcomponent
interface MainSubComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): MainSubComponent
    }

    fun inject(activity: MainActivity)
}