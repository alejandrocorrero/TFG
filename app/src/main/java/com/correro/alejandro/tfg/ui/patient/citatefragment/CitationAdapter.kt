package com.correro.alejandro.tfg.ui.patient.citatefragment

import android.arch.lifecycle.AndroidViewModel
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.correro.alejandro.tfg.data.api.models.chronicresponse.Chronic
import com.correro.alejandro.tfg.data.api.models.citationresponse.Citation
import com.correro.alejandro.tfg.data.api.models.historialresponse.Historical
import com.correro.alejandro.tfg.databinding.FragmentChronicItemBinding
import com.correro.alejandro.tfg.databinding.FragmentCitationItemBinding
import com.correro.alejandro.tfg.databinding.FragmentHistorialItemBinding
import com.correro.alejandro.tfg.ui.patient.MainActivityPatientViewModel

class CitationAdapter(val items: ArrayList<Citation>) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = FragmentCitationItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size
}

class ViewHolder(var binding: FragmentCitationItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Citation) = with(binding.root) {
        binding.citation = item
        // setOnClickListener { listener(item) }
        //setOnLongClickListener { listener2(item) }

    }
}
