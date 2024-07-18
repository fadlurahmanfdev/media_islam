package co.id.fadlurahmanf.mediaislam.article

import co.id.fadlurahmanf.mediaislam.core.di.modules.SharedModule
import dagger.Module

@Module(subcomponents = [ArticleSubComponent::class], includes = [SharedModule::class])
class ArticleModule {}