package co.id.fadlurahmanf.mediaislam.quran

import co.id.fadlurahmanf.mediaislam.core.di.modules.SharedModule
import dagger.Module

@Module(subcomponents = [QuranSubComponent::class], includes = [SharedModule::class])
class QuranModule {}