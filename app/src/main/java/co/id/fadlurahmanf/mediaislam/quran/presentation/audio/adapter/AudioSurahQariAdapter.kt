package co.id.fadlurahmanf.mediaislam.quran.presentation.audio.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import co.id.fadlurahmanf.mediaislam.R
import co.id.fadlurahmanf.mediaislam.core.other.GlideUrlCachedKey
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.AudioQariModel
import com.bumptech.glide.Glide

class AudioSurahQariAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var context: Context
    private var audios: ArrayList<AudioQariModel> = arrayListOf()
    private var currentQariIdPlaying: String? = null
    private var callBack: CallBack? = null

    fun setCallBack(callBack: CallBack) {
        this.callBack = callBack
    }

    fun setList(list: ArrayList<AudioQariModel>) {
        val oldSize = audios.size
        if (oldSize > 0) {
            audios.clear()
            notifyItemRangeRemoved(0, oldSize)
        }
        audios.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    fun setWhichQariIsPlaying(qariId: String) {
        if (qariId == currentQariIdPlaying) return
        repeat(audios.size) { index ->
            val audioByQari = audios[index]
            if (audioByQari.isPlaying && audioByQari.qariId != qariId) {
                audios[index].isPlaying = false
                notifyItemChanged(index)
            } else if (!audioByQari.isPlaying && audioByQari.qariId == qariId) {
                audios[index].isPlaying = true
                currentQariIdPlaying = qariId
                notifyItemChanged(index)
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val main: LinearLayout = view.findViewById(R.id.ll_main)
        val qariName: TextView = view.findViewById(R.id.tv_now_playing_qari_name)
        val qariImageView: ImageView = view.findViewById(R.id.iv_now_playing_qari_image)

        init {
            view.setOnClickListener {
                callBack?.onClicked(audios[absoluteAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_audio_qari_model, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val audio: AudioQariModel = audios[position]
        val mHolder = holder as ViewHolder

        mHolder.qariName.text = audio.qariName
        if (audio.qariImage != null) {
            Glide.with(mHolder.qariImageView)
                .load(GlideUrlCachedKey(audio.qariImage, audio.qariImageKey))
                .centerCrop()
                .into(mHolder.qariImageView)
        }

        if (audio.isPlaying) {
            mHolder.main.background =
                ContextCompat.getDrawable(context, R.drawable.solid_primary_corner_15)
            mHolder.qariName.setTextColor(ContextCompat.getColor(context, R.color.white))
        } else {
            mHolder.main.background =
                ContextCompat.getDrawable(context, R.drawable.solid_white_corner_5)
            mHolder.qariName.setTextColor(ContextCompat.getColor(context, R.color.black))
        }
    }

    override fun getItemCount(): Int {
        return audios.size
    }

    interface CallBack {
        fun onClicked(audio: AudioQariModel)
    }
}