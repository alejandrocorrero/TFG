package com.correro.alejandro.tfg.ui.patient.citatefragment

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.correro.alejandro.tfg.R
import kotlinx.android.synthetic.main.acitivty_citation_time_item.view.*

class TimeAdapter(var items: ArrayList<String>, val listener: (Int) -> Unit) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.acitivty_citation_time_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position], listener, position, upDateValues())

    override fun getItemCount() = items.size

    fun updateItems(its:ArrayList<String>){
        items=its
        notifyDataSetChanged()
    }
    fun upDateValues(): (Int, Int) -> Unit {
        return { position: Int, selected: Int ->
            if (selected != -1)
                notifyItemChanged(selected)
            notifyItemChanged(position)
        }
    }


}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        var selectedItem: Int = -1
    }

    fun bind(item: String, listener: (Int) -> Unit, position: Int, upDateValues: (Int, Int) -> Unit) = with(itemView) {
        lblTime.text = item
        if (position == selectedItem)
            itemView.setBackgroundResource(R.color.colorCitations)
        else {
            itemView.setBackgroundResource(R.color.background_login)
        }
        setOnClickListener {
            var lastSelected = selectedItem
            if (selectedItem == position)
                selectedItem = -1
            else
                selectedItem = position
            upDateValues(position, lastSelected)
            listener(selectedItem)

        }


    }
}