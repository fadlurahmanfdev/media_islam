package co.id.fadlurahmanf.mediaislam.quran.presentation.surah.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.id.fadlurahmanf.mediaislam.R
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.SurahModel

class ListSurahAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var surahs: ArrayList<SurahModel> = arrayListOf()
    private lateinit var callBack: CallBack

    fun setCallBack(callBack: CallBack) {
        this.callBack = callBack
    }

    fun setList(list: ArrayList<SurahModel>) {
        val oldSize = surahs.size
        if (oldSize > 0) {
            surahs.clear()
            notifyItemRangeRemoved(0, oldSize)
        }
        surahs.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val number: TextView = view.findViewById(R.id.tv_number_surah)
        val latin: TextView = view.findViewById(R.id.tv_latin_title)
        val location: TextView = view.findViewById(R.id.tv_location_surah)
        val indonesian: TextView = view.findViewById(R.id.tv_indonesian_title)
        val arabic: TextView = view.findViewById(R.id.tv_arabic_title)
        val totalAyah: TextView = view.findViewById(R.id.tv_total_ayah)

        init {
            view.setOnClickListener {
                callBack.onClicked(surahs[absoluteAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_surah, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val surah: SurahModel = surahs[position]
        val mHolder = holder as ViewHolder

        mHolder.number.text = "${surah.surahNo}"
        mHolder.latin.text = surah.latin
//        mHolder.location.text = surah.origin.changeFirstWordToUpperCase()
        mHolder.location.text = surah.origin
        mHolder.arabic.text = surah.arabic
        mHolder.indonesian.text = surah.meaning
        mHolder.totalAyah.text = "${surah.totalVerse} Ayat"
    }

    override fun getItemCount(): Int {
        return surahs.size
    }

    interface CallBack {
        fun onClicked(surah: SurahModel)
    }
}