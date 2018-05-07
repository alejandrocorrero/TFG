package com.correro.alejandro.tfg.ui.patient.recipefragment

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.correro.alejandro.tfg.data.api.models.historialresponse.Historical
import com.correro.alejandro.tfg.data.api.models.reciperesponse.Recipe
import com.correro.alejandro.tfg.databinding.FragmentHistorialItemBinding
import com.correro.alejandro.tfg.databinding.FragmentRecipesItemBinding
public class RecipeAdapter(val items: ArrayList<Recipe>) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = FragmentRecipesItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size
}

class ViewHolder(var binding: FragmentRecipesItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Recipe) = with(binding.root) {
        binding.recipe = item
        //setOnClickListener { listener(item) }
        //setOnLongClickListener { listener2(item) }

    }
}
