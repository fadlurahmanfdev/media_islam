package co.id.fadlurahmanf.mediaislam.main

import co.id.fadlurahmanf.mediaislam.core.di.modules.SharedModule
import dagger.Module

@Module(subcomponents = [MainSubComponent::class], includes = [SharedModule::class])
class MainModule {}