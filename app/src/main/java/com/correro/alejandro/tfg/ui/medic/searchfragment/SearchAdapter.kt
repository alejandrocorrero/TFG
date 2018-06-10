package com.correro.alejandro.tfg.ui.medic.searchfragment

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.correro.alejandro.tfg.BR
import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.medicusersresponse.MedicUser
import com.correro.alejandro.tfg.databinding.FragmentSearchItemBinding
import com.correro.alejandro.tfg.databinding.ProgressLayoutBinding
import com.correro.alejandro.tfg.utils.Constants
import com.squareup.picasso.Picasso


class SearchAdapter(var items: ArrayList<MedicUser?>, val listener: (MedicUser) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_VIEW_TYPE_BASIC = 0
    private val ITEM_VIEW_TYPE_PROGRESS = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ITEM_VIEW_TYPE_BASIC) {
            return ViewHolder2(FragmentSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            return ProgressViewHolder(ProgressLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder2)
            holder.bind(items[position]!!, listener)
        else (holder as? ProgressViewHolder)?.bind()
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return if (items[position] != null) ITEM_VIEW_TYPE_BASIC else ITEM_VIEW_TYPE_PROGRESS
    }

    fun newItems(newitems: ArrayList<MedicUser?>) {
        items = newitems
        notifyDataSetChanged()

    }

    fun lastitems(newitems: ArrayList<MedicUser>) {
        items.addAll(newitems)
        notifyDataSetChanged()
    }

}

class ViewHolder2(var binding: FragmentSearchItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: MedicUser, listener: (MedicUser) -> Unit) = with(binding.root) {
        Picasso.get().load(Constants.ADDRESS2+item.foto).into(binding.imgPhoto);
        binding.setVariable(BR.medicuser, item)
        binding.executePendingBindings()
        setOnClickListener { listener(item) }

    }
}

class ProgressViewHolder(var binding: ProgressLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind() = with(binding.root) {


        binding.executePendingBindings()


    }
}



