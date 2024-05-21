package co.id.fadlurahmanf.mediaislam.main.presentation.adapter

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
import co.id.fadlurahmanf.mediaislam.main.data.dto.model.ItemMainMenuModel

class MenuAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var menus: ArrayList<ItemMainMenuModel> = arrayListOf()
    private lateinit var callBack: CallBack
    private lateinit var context: Context

    fun setCallBack(callBack: CallBack) {
        this.callBack = callBack
    }

    fun setList(list: ArrayList<ItemMainMenuModel>) {
        menus.clear()
        menus.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.iv_menu_icon)
        val title: TextView = view.findViewById(R.id.tv_menu_title)
        val main: LinearLayout = view.findViewById(R.id.ll_main)

        init {
            main.setOnClickListener {
                callBack.onClicked(menus[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_main_menu, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val menu: ItemMainMenuModel = menus[position]
        val mHolder = holder as ViewHolder

        mHolder.title.text = menu.title
        mHolder.icon.setImageDrawable(ContextCompat.getDrawable(context, menu.icon))
    }

    override fun getItemCount(): Int {
        return menus.size
    }

    interface CallBack {
        fun onClicked(menu: ItemMainMenuModel)
    }
}