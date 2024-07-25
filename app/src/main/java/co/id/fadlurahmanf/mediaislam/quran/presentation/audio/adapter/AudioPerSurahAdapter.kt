package co.id.fadlurahmanf.mediaislam.quran.presentation.audio.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.id.fadlurahmanf.mediaislam.R
import co.id.fadlurahmanf.mediaislam.core.other.GlideUrlCachedKey
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.AudioPerSurahModel
import com.bumptech.glide.Glide

class AudioPerSurahAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var context: Context
    private var audios: ArrayList<AudioPerSurahModel> = arrayListOf()

    //    private var currentQariIdPlaying: String? = null
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

//    fun setWhichQariIsPlaying(qariId: String) {
//        if (qariId == currentQariIdPlaying) return
//        repeat(audios.size) { index ->
//            val audioByQari = audios[index]
//            if (audioByQari.isPlaying && audioByQari.qariId != qariId) {
//                audios[index].isPlaying = false
//                notifyItemChanged(index)
//            } else if (!audioByQari.isPlaying && audioByQari.qariId == qariId) {
//                audios[index].isPlaying = true
//                currentQariIdPlaying = qariId
//                notifyItemChanged(index)
//            }
//        }
//    }

    inner class SurahViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val no: TextView = view.findViewById(R.id.tv_number_surah)
        val latin: TextView = view.findViewById(R.id.tv_latin_title)
        val arabic: TextView = view.findViewById(R.id.tv_arabic_title)
        val indonesian: TextView = view.findViewById(R.id.tv_indonesian_title)

//        init {
//            view.setOnClickListener {
//                callBack?.onClicked(audios[absoluteAdapterPosition])
//            }
//        }
    }

    inner class QariViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val picture: ImageView = view.findViewById(R.id.iv_qari)
        val name: TextView = view.findViewById(R.id.tv_qari_name)

        init {
            view.setOnClickListener {
                callBack?.onClicked(audios[absoluteAdapterPosition])
            }
        }
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
            mHolder.indonesian.text = audio.surah.meaning
            mHolder.latin.text = audio.surah.latinName

            val param = mHolder.itemView.layoutParams as ViewGroup.MarginLayoutParams
            if (position > 0) {
                param.setMargins(0, 50, 0, 0)
            } else {
                param.setMargins(0, 0, 0, 0)
            }
            mHolder.itemView.layoutParams = param
        } else {
            val mHolder = holder as QariViewHolder
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