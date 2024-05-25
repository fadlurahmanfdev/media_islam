package co.id.fadlurahmanf.mediaislam.quran.presentation.surah.adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.id.fadlurahmanf.mediaislam.R
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.DetailSurahModel

class ListVerseAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var verses: ArrayList<DetailSurahModel.Verse> = arrayListOf()
    private var callBack: CallBack? = null

    //    private var fontSize: QuranFontSize = QuranFontSize()
    private lateinit var context: Context

    fun setCallBack(callBack: CallBack) {
        this.callBack = callBack
    }

    fun setList(list: ArrayList<DetailSurahModel.Verse>) {
        verses.clear()
        verses.addAll(list)
        notifyItemRangeInserted(0, verses.size)
        notifyDataSetChanged()
    }

//    fun setFontSize(fontSize: QuranFontSize) {
//        this.fontSize = fontSize
//        notifyDataSetChanged()
//    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val nomorAyah: TextView = view.findViewById(R.id.tv_number_ayah)
        val arabic: TextView = view.findViewById<TextView>(R.id.tv_arabic)
        val latin: TextView = view.findViewById(R.id.tv_latin)
        val indonesian: TextView = view.findViewById(R.id.tv_indonesian)
        val share: ImageView = view.findViewById(R.id.iv_share)
//        val favorite: ImageView = view.findViewById(R.id.iv_favorite)

        init {
            share.setOnClickListener {
                callBack?.onVerseClickedShare(verses[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_verse_v2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val verse = verses[position]

        val mHolder = holder as ViewHolder

//        mHolder.nomorAyah.text = "${verse.no}"
        mHolder.arabic.text = verse.arabicText
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mHolder.latin.text = Html.fromHtml(verse.latinText, Html.FROM_HTML_MODE_COMPACT)
        } else {
            mHolder.latin.text = Html.fromHtml(verse.latinText)
        }
        mHolder.indonesian.text = "${verse.no}. ${verse.indonesianText}"
//        mHolder.favorite.imageTintList =
//            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green))
//        if (verse.isFavorite == true) {
//            mHolder.favorite.setImageDrawable(
//                ContextCompat.getDrawable(
//                    context,
//                    R.drawable.round_favorite_24
//                )
//            )
//        } else {
//            mHolder.favorite.setImageDrawable(
//                ContextCompat.getDrawable(
//                    context,
//                    R.drawable.round_favorite_border_24
//                )
//            )
//        }
//        mHolder.share.setOnClickListener {
//            callBack?.onSharedClicked(verse)
//        }
//        mHolder.favorite.setOnClickListener {
//            callBack?.onFavoriteClicked(verse)
//        }

//        mHolder.arabic.setTextAppearance(if (fontSize.arabicStyle == FontSize.LARGE) R.style.Font_Bold_40 else if (fontSize.arabicStyle == FontSize.SMALL) R.style.Font_Bold_30 else R.style.Font_Bold_35)
//        mHolder.latin.setTextAppearance(if (fontSize.latinSize == FontSize.LARGE) R.style.Font_Regular_20 else if (fontSize.latinSize == FontSize.SMALL) R.style.Font_Regular_12 else R.style.Font_Regular_16)
//        mHolder.indonesian.setTextAppearance(if (fontSize.indonesianStyle == FontSize.LARGE) R.style.Font_Regular_20 else if (fontSize.indonesianStyle == FontSize.SMALL) R.style.Font_Regular_12 else R.style.Font_Regular_16)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mHolder.arabic.setTextAppearance(R.style.Font_Bold_40)
        } else {
            mHolder.arabic.setTextAppearance(context, R.style.Font_Bold_40)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mHolder.latin.setTextAppearance(R.style.Font_Regular_18)
        } else {
            mHolder.latin.setTextAppearance(context, R.style.Font_Regular_18)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mHolder.indonesian.setTextAppearance(R.style.Font_Regular_16)
        } else {
            mHolder.indonesian.setTextAppearance(context, R.style.Font_Bold_16)
        }
    }

    override fun getItemCount(): Int {
        return verses.size
    }

    interface CallBack {
        fun onVerseClickedShare(verse: DetailSurahModel.Verse)
//        fun onFavoriteClicked(ayah: AyahModel)
    }
}