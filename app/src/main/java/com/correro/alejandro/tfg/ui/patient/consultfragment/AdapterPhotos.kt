package com.correro.alejandro.tfg.ui.patient.consultfragment

import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.correro.alejandro.tfg.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.imagegrid_item.view.*


class AdapterPhotos(private val click: (ImageItem) -> Boolean, var items: ArrayList<ImageItem> = ArrayList()) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.imagegrid_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position], click)

    override fun getItemCount() = items.size
    fun addItem(item: ArrayList<ImageItem>) {
        items = item
        notifyItemInserted(items.size - 1)
    }
    fun removeItem(item: ArrayList<ImageItem>,position: Int) {
        items = item
        notifyItemRemoved(position)
    }
}


class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: ImageItem, click: (ImageItem) -> Boolean) = with(itemView) {
        Picasso.get().load(item.path).resize(500, 500).into(imgPhoto)
        popup.setOnClickListener { v -> showMenu(v, click, item) }


    }

    private fun showMenu(v: View?, click: (ImageItem) -> Boolean, item: ImageItem) {
        val popup = PopupMenu(v!!.context, v)
        val inflador = popup.menuInflater
        inflador.inflate(R.menu.popupmenu, popup.menu)
        popup.setOnMenuItemClickListener(
                {
                    when (it.itemId) {
                        R.id.mnuEliminar -> click(item)
                    }
                    false
                })
        popup.show()
    }
}


