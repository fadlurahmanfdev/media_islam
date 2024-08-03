package co.id.fadlurahmanf.mediaislam.alarm

import co.id.fadlurahmanf.mediaislam.alarm.presentation.AlarmActivity
import dagger.Subcomponent

@Subcomponent
interface AlarmSubComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): AlarmSubComponent
    }

    fun inject(activity: AlarmActivity)
}