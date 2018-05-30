package com.correro.alejandro.tfg.ui.patient.consultfragment

import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.ui.patient.patientfragment.attachmentsfragment.FileWithType
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.imagegrid_item.view.*


class ConsultDetailPhotoAdapter(private val click: (FileWithType) -> Unit, var items: ArrayList<FileWithType> = ArrayList()) : RecyclerView.Adapter<ViewHolder2>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder2(LayoutInflater.from(parent.context).inflate(R.layout.imagegrid_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder2, position: Int) = holder.bind(items[position], click)

    override fun getItemCount() = items.size
    fun addItem(it: FileWithType?) {
        items.add(it!!)
        notifyItemInserted(items.size - 1)
    }
}


class ViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: FileWithType, click: (FileWithType) -> Unit) = with(itemView) {
        this.popup.visibility = View.INVISIBLE
        Picasso.get().load(item.file).placeholder(R.color.backgroundProfile).resize(500, 500).into(imgPhoto)
        setOnClickListener { click(item) }


    }

}


