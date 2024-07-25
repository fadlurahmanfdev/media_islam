package co.id.fadlurahmanf.mediaislam.quran.presentation.audio.adapter

import android.app.ActionBar.LayoutParams
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import co.id.fadlurahmanf.mediaislam.R
import co.id.fadlurahmanf.mediaislam.core.other.GlideUrlCachedKey
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.AudioPerSurahModel
import com.bumptech.glide.Glide

class AudioPerSurahAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var context: Context
    private var audios: ArrayList<AudioPerSurahModel> = arrayListOf()

    private var callBack: CallBack? = null

    fun setCallBack(callBack: CallBack) {
        this.callBack = callBack
    }

    fun setList(list: ArrayList<AudioPerSurahModel>) {
        val oldSize = audios.size
        if (oldSize > 0) {
            audios.clear()
            notifyItemRangeRemoved(0, oldSize)
        }
        audios.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    private var expandedItemBySurahNo: Int? = null
    private fun setExpandedItem(surahNo: Int) {
        if (expandedItemBySurahNo != null) {
            audios.forEachIndexed { index, audioPerSurahModel ->
                if (audioPerSurahModel.surah.no == expandedItemBySurahNo) {
                    audios[index] = audioPerSurahModel.copy(isExpanded = false)
                }
            }
            val firstIndex =
                audios.indexOfFirst { element -> element.surah.no == expandedItemBySurahNo }
            val lastIndex =
                audios.indexOfLast { element -> element.surah.no == expandedItemBySurahNo }
            notifyItemRangeChanged(firstIndex, lastIndex)
        }

        if (expandedItemBySurahNo == surahNo) {
            expandedItemBySurahNo = null
            return
        }

        expandedItemBySurahNo = surahNo
        audios.forEachIndexed { index, audioPerSurahModel ->
            if (audioPerSurahModel.surah.no == surahNo) {
                audios[index] = audioPerSurahModel.copy(isExpanded = true)
            }
        }
        val firstIndex = audios.indexOfFirst { element -> element.surah.no == surahNo }
        val lastIndex = audios.indexOfLast { element -> element.surah.no == surahNo }
        notifyItemRangeChanged(firstIndex, lastIndex)
    }

    inner class SurahViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val no: TextView = view.findViewById(R.id.tv_number_surah)
        val latin: TextView = view.findViewById(R.id.tv_latin_title)
        val arabic: TextView = view.findViewById(R.id.tv_arabic_title)
        val indonesian: TextView = view.findViewById(R.id.tv_indonesian_title)
        val expandIcon: ImageView = view.findViewById(R.id.iv_expand_icon)

        init {
            view.setOnClickListener {
                val audio = audios[absoluteAdapterPosition]
                setExpandedItem(audio.surah.no)
            }
        }
    }

    inner class QariViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val picture: ImageView = view.findViewById(R.id.iv_qari)
        val name: TextView = view.findViewById(R.id.tv_qari_name)
        val main: ConstraintLayout = view.findViewById(R.id.main)

//        init {
//            view.setOnClickListener {
//                callBack?.onClicked(audios[absoluteAdapterPosition])
//            }
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        if (viewType == 0) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_audio_type_surah, parent, false)
            return SurahViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_audio_typed_qari, parent, false)
            return QariViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val audio: AudioPerSurahModel = audios[position]
        if (getItemViewType(position) == 0) {
            val mHolder = holder as SurahViewHolder

            mHolder.no.text = audio.surah.no.toString()
            mHolder.arabic.text = audio.surah.arabic
            mHolder.arabic.visibility = View.GONE
            mHolder.indonesian.text = audio.surah.meaning
            mHolder.latin.text = audio.surah.latinName

            @DrawableRes
            val expandIcondrawable: Int = when (audio.isExpanded) {
                true -> {
                    R.drawable.baseline_expand_less_24
                }

                false -> {
                    R.drawable.baseline_expand_more_24
                }
            }
            mHolder.expandIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    expandIcondrawable
                )
            )
        } else {
            val mHolder = holder as QariViewHolder
            mHolder.main.layoutParams.height = LayoutParams.WRAP_CONTENT
            mHolder.main.layoutParams.width = LayoutParams.MATCH_PARENT
            if (audio.isExpanded) {
                mHolder.main.layoutParams.height = LayoutParams.WRAP_CONTENT
                mHolder.main.layoutParams.width = LayoutParams.MATCH_PARENT
            } else {
                mHolder.main.layoutParams.height = 0
                mHolder.main.layoutParams.width = 0
            }
            mHolder.name.text = audio.qari.name
            Glide.with(mHolder.picture)
                .load(GlideUrlCachedKey(audio.qari.image ?: "", customKey = audio.qari.imageKey))
                .into(mHolder.picture)
        }
    }

    override fun getItemCount(): Int = audios.size

    override fun getItemViewType(position: Int): Int = audios[position].type

    interface CallBack {
        fun onClicked(audio: AudioPerSurahModel)
    }
}