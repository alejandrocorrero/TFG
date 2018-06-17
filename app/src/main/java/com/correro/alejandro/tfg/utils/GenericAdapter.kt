package com.correro.alejandro.tfg.utils

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.correro.alejandro.tfg.databinding.ProgressLayoutBinding


class GenericAdapter<T>(private val modelBR: Int, private val idBinding: Int, private val click: ((T, ViewDataBinding?) -> Unit)? = null, private val clickLong: ((T) -> Boolean)? = null, var items: ArrayList<T?> = ArrayList(), var empty: View? = null) : RecyclerView.Adapter<BaseViewHolder<T>>() {
    private val ITEM_VIEW_TYPE_BASIC = 0
    private val ITEM_VIEW_TYPE_FOOTER = 1
    private var value: Boolean = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {

        if (viewType == ITEM_VIEW_TYPE_BASIC) {
            return ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), idBinding, parent, false))
        } else {
            return ProgressViewHolder(ProgressLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        }

    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        if (holder is ViewHolder<T>)
            holder.bind(items[position]!!, click, clickLong, modelBR)
        else if (holder is ProgressViewHolder<T>) {
            holder.bind()
            if (!value) {
                holder.binding.progressBar7.visibility = View.VISIBLE
                holder.binding.progressBar7.isIndeterminate = true
            } else
                holder.binding.progressBar7.visibility = View.INVISIBLE
        }
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
        items = newitems as ArrayList<T?>
        notifyDataSetChanged()

    }

    fun lastitems(value: Boolean, newitems: ArrayList<T>) {
        this.value = value
        items.addAll(newitems)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] != null) ITEM_VIEW_TYPE_BASIC else ITEM_VIEW_TYPE_FOOTER
    }

}


class ViewHolder<T>(var binding: ViewDataBinding) : BaseViewHolder<T>(binding) {

    fun bind(item: T, click: ((T, ViewDataBinding?) -> Unit)?, clickLong: ((T) -> Boolean)?, modelBR: Int) = with(binding.root) {
        binding.setVariable(modelBR, item)
        binding.executePendingBindings()
        if (click != null) {
            setOnClickListener { click(item, binding) }
        }
        if (clickLong != null) {
            setOnLongClickListener { clickLong.invoke(item) }
        }

    }
}

class ProgressViewHolder<T>(var binding: ProgressLayoutBinding) : BaseViewHolder<T>(binding) {
    fun bind() = with(binding.root) {


        binding.executePendingBindings()


    }
}

open class BaseViewHolder<T>(var binding2: ViewDataBinding) : RecyclerView.ViewHolder(binding2.root)




