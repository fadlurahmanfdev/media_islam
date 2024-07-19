package co.id.fadlurahmanf.mediaislam.article

import co.id.fadlurahmanf.mediaislam.article.data.datasources.ArticleDatasourceRepository
import co.id.fadlurahmanf.mediaislam.article.domain.usecase.ArticleUseCase
import co.id.fadlurahmanf.mediaislam.article.domain.usecase.ArticleUseCaseImpl
import co.id.fadlurahmanf.mediaislam.core.di.modules.SharedModule
import dagger.Module
import dagger.Provides

@Module(subcomponents = [ArticleSubComponent::class], includes = [SharedModule::class])
class ArticleModule {
    @Provides
    fun provideArticleUseCase(
        articleDatasourceRepository: ArticleDatasourceRepository,
    ): ArticleUseCase {
        return ArticleUseCaseImpl(
            articleDatasourceRepository = articleDatasourceRepository
        )
    }
}