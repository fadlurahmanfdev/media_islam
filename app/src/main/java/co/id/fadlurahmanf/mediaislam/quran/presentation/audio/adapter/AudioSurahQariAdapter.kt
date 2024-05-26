package co.id.fadlurahmanf.mediaislam.quran.presentation.audio.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.id.fadlurahmanf.mediaislam.R
import co.id.fadlurahmanf.mediaislam.core.other.GlideUrlCachedKey
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.AudioQariModel
import com.bumptech.glide.Glide

class AudioSurahQariAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var audios: ArrayList<AudioQariModel> = arrayListOf()
    private lateinit var callBack: CallBack

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

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val qariName: TextView = view.findViewById(R.id.tv_now_playing_qari_name)
        val qariImageView: ImageView = view.findViewById(R.id.iv_now_playing_qari_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_audio_qari_model, parent, false)
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
    }

    override fun getItemCount(): Int {
        return audios.size
    }

    interface CallBack {
        fun onClicked(audio: AudioQariModel)
    }
}