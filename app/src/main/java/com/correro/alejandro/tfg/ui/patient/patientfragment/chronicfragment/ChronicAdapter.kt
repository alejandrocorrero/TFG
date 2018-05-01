package com.correro.alejandro.tfg.ui.patient.patientfragment.chronicfragment

import android.arch.lifecycle.AndroidViewModel
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.correro.alejandro.tfg.data.api.models.chronicresponse.Chronic
import com.correro.alejandro.tfg.data.api.models.historialresponse.Historical
import com.correro.alejandro.tfg.databinding.FragmentChronicItemBinding
import com.correro.alejandro.tfg.databinding.FragmentHistorialItemBinding
import com.correro.alejandro.tfg.ui.patient.MainActivityPatientViewModel

class ChronicAdapter(val items: ArrayList<Chronic>, val listener: (Chronic) -> Unit) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = FragmentChronicItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position], listener)

    override fun getItemCount() = items.size
}

class ViewHolder(var binding: FragmentChronicItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Chronic, listener: (Chronic) -> Unit) = with(binding.root) {
        binding.chronic = item
        setOnClickListener { listener(item) }
        //setOnLongClickListener { listener2(item) }

    }
}
