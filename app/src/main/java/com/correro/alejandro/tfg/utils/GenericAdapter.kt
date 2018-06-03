package com.correro.alejandro.tfg.utils

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup





class GenericAdapter<T>(private val modelBR: Int, private val idBinding: Int, private val click: ((T,ViewDataBinding?) -> Unit)? = null, private val clickLong: ((T) -> Boolean)? = null, var items: ArrayList<T> = ArrayList(), var empty: View? = null) : RecyclerView.Adapter<ViewHolder<T>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {

            return ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), idBinding, parent, false))

    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        holder.bind(items[position], click, clickLong, modelBR)
    }

    override fun getItemCount(): Int {
        if (empty != null) {
            if (items.size == 0) {
                empty!!.visibility = View.VISIBLE
            } else {
                empty!!.visibility = View.INVISIBLE

            }
        }
        return items.size
    }

    fun newItems(newitems: ArrayList<T>) {
        items = newitems
        notifyDataSetChanged()

    }

    fun lastitems(newitems: ArrayList<T>) {
        items.addAll(newitems)
        notifyDataSetChanged()
    }


}


class ViewHolder<T>(var binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: T, click: ((T,ViewDataBinding?) -> Unit)?, clickLong: ((T) -> Boolean)?, modelBR: Int) = with(binding.root) {
        binding.setVariable(modelBR, item)
        binding.executePendingBindings()
        if (click != null) {
            setOnClickListener { click(item,binding) }
        }
        if (clickLong != null) {
            setOnLongClickListener { clickLong.invoke(item) }
        }

    }
}




