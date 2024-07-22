package co.id.fadlurahmanf.mediaislam.article.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import co.id.fadlurahmanf.mediaislam.article.BaseArticleFragment
import co.id.fadlurahmanf.mediaislam.article.presentation.adapter.ArticlePagingAdapter
import co.id.fadlurahmanf.mediaislam.core.network.dto.response.article.ArticleItemResponse
import co.id.fadlurahmanf.mediaislam.databinding.FragmentArticleBinding
import javax.inject.Inject

class ArticleFragment :
    BaseArticleFragment<FragmentArticleBinding>(FragmentArticleBinding::inflate),
    ArticlePagingAdapter.CallBack {
    @Inject
    lateinit var viewModel: ArticleViewModel

    private lateinit var pagingAdapter: ArticlePagingAdapter
    override fun setup(savedInstanceState: Bundle?) {
        initAdapter()
        initObserver()
        viewModel.requestPagingArticle()
    }

    private fun initAdapter() {
        pagingAdapter = ArticlePagingAdapter()
        pagingAdapter.setCallBack(this)

        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rv.layoutManager = layoutManager
        binding.rv.adapter = pagingAdapter

        pagingAdapter.addLoadStateListener {
            if(it.refresh is LoadState.Loading){
                binding.rv.visibility = View.GONE
                binding.layoutLoading.root.visibility = View.VISIBLE
            }

            if(it.prepend.endOfPaginationReached){
                binding.rv.visibility = View.VISIBLE
                binding.layoutLoading.root.visibility = View.GONE
            }


        }
    }

    private fun initObserver() {
        viewModel.pagingData.observe(this) {
            pagingAdapter.submitData(lifecycle, it)
        }
    }

    override fun inject() {
        articleComponent.inject(this)
    }

    override fun onClicked(article: ArticleItemResponse) {
        val intent = Intent(requireActivity(), ArticleWebViewActivity::class.java)
        intent.putExtra(ArticleWebViewActivity.URL, article.url)
        startActivity(intent)
    }
}