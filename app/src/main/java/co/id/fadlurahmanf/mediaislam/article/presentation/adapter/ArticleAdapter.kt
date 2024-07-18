package co.id.fadlurahmanf.mediaislam.article.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.id.fadlurahmanf.mediaislam.R
import co.id.fadlurahmanf.mediaislam.core.network.dto.response.article.ArticleItemResponse
import com.bumptech.glide.Glide

class ArticleAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var articles: ArrayList<ArticleItemResponse> = arrayListOf()
    private lateinit var callBack: CallBack

    fun setCallBack(callBack: CallBack) {
        this.callBack = callBack
    }

    fun setList(list: ArrayList<ArticleItemResponse>) {
        val oldSize = articles.size
        if (oldSize > 0) {
            articles.clear()
            notifyItemRangeRemoved(0, oldSize)
        }
        articles.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tv_title)
        val category: TextView = view.findViewById(R.id.tv_category)
        val thumbnail: ImageView = view.findViewById(R.id.iv_thumbnail)

        init {
            view.setOnClickListener {
                callBack.onClicked(articles[absoluteAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_article_horizontal, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val article: ArticleItemResponse = articles[position]
        val mHolder = holder as ViewHolder

        if (article.categories?.isNotEmpty() == true) {
            mHolder.category.text = article.categories.first().name ?: "-"
        } else {
            mHolder.category.visibility = View.VISIBLE
        }
        mHolder.title.text = article.title
        Glide.with(mHolder.thumbnail).load(article.thumbnail ?: "-").into(mHolder.thumbnail)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    interface CallBack {
        fun onClicked(article: ArticleItemResponse)
    }
}