package co.id.fadlurahmanf.mediaislam.article.domain.usecase

import co.id.fadlurahmanf.mediaislam.article.data.datasources.ArticleDatasourceRepository
import co.id.fadlurahmanf.mediaislam.core.network.dto.response.article.ArticleItemResponse
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class ArticleUseCaseImpl @Inject constructor(
    private val articleDatasourceRepository: ArticleDatasourceRepository
) : ArticleUseCase {
    override fun getTop3Article(): Observable<List<ArticleItemResponse>> {
        return articleDatasourceRepository.getIslamArticle(source = "ks", page = 1).map {
            if (it.content.size > 3) {
                it.content.take(3)
            } else {
                it.content
            }
        }
    }
}