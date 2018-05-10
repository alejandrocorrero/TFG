package com.correro.alejandro.tfg.ui.patient.patientfragment.attachmentsfragment

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.correro.alejandro.tfg.data.api.models.attachmentsresponse.Attachment
import com.correro.alejandro.tfg.databinding.FragmentAttachmentsItemBinding

class AttachmentlAdapter(var items: ArrayList<Attachment>, val listener: (Attachment) -> Unit) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = FragmentAttachmentsItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position], listener)

    override fun getItemCount() = items.size
    fun newItems(newitems: ArrayList<Attachment>) {
        items = newitems
        notifyDataSetChanged()
    }
}


class ViewHolder(var binding: FragmentAttachmentsItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Attachment, listener: (Attachment) -> Unit) = with(binding.root) {
        binding.attachment = item
        setOnClickListener { listener(item) }
        //setOnLongClickListener { listener2(item) }

    }
}
