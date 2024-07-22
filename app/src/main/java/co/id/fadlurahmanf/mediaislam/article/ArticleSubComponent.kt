package co.id.fadlurahmanf.mediaislam.article

import co.id.fadlurahmanf.mediaislam.article.presentation.ArticleFragment
import co.id.fadlurahmanf.mediaislam.article.presentation.ArticleListActivity
import co.id.fadlurahmanf.mediaislam.quran.presentation.audio.AudioActivity
import co.id.fadlurahmanf.mediaislam.quran.presentation.surah.DetailSurahActivity
import co.id.fadlurahmanf.mediaislam.quran.presentation.surah.ListSurahActivity
import dagger.Subcomponent

@Subcomponent
interface ArticleSubComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): ArticleSubComponent
    }

    fun inject(activity: ArticleListActivity)

    fun inject(fragment: ArticleFragment)
}