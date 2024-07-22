package co.id.fadlurahmanf.mediaislam.article.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import co.id.fadlurahmanf.mediaislam.R
import co.id.fadlurahmanf.mediaislam.core.network.dto.response.article.ArticleItemResponse
import com.bumptech.glide.Glide

class ArticlePagingAdapter :
    PagingDataAdapter<ArticleItemResponse, ArticlePagingAdapter.ViewHolder>(DiffUtilCallBack()) {
    private lateinit var context: Context
    private var callBack: CallBack? = null
    fun setCallBack(callBack: CallBack) {
        this.callBack = callBack
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tv_title)
        val category: TextView = view.findViewById(R.id.tv_category)
        val date: TextView = view.findViewById(R.id.tv_date)
        val thumbnail: ImageView = view.findViewById(R.id.iv_thumbnail)

        init {
            view.setOnClickListener {
                callBack?.onClicked(getItem(absoluteAdapterPosition)!!)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article: ArticleItemResponse = getItem(position)!!

        if (article.categories?.isNotEmpty() == true) {
            holder.category.text = article.categories.first().name ?: "-"
        } else {
            holder.category.visibility = View.VISIBLE
        }
        holder.title.text = article.title
        holder.date.text = article.date
        Glide.with(holder.thumbnail).load(article.thumbnail ?: "-").into(holder.thumbnail)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticlePagingAdapter.ViewHolder {
        context = parent.context
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_article_vertical, parent, false)
        return ViewHolder(view)
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<ArticleItemResponse>() {
        override fun areItemsTheSame(
            oldItem: ArticleItemResponse,
            newItem: ArticleItemResponse
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ArticleItemResponse,
            newItem: ArticleItemResponse
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    interface CallBack {
        fun onClicked(article: ArticleItemResponse)
    }
}