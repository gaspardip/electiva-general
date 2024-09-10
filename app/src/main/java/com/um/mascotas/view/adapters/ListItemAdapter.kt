package com.um.mascotas.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.um.mascotas.R
import com.um.mascotas.model.PetModel
import com.um.mascotas.viewmodel.PetViewModel

class ListItemAdapter(private val petViewModel: PetViewModel, private val items: List<PetModel>) :
    RecyclerView.Adapter<ListItemAdapter.CardViewHolder>() {

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val description: TextView = itemView.findViewById(R.id.description)
        val age: TextView = itemView.findViewById(R.id.age)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        view.setOnClickListener{
            petViewModel.addRandomPet()
        }
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = item.name
        holder.description.text = item.description
        holder.age.text = item.age
    }

    override fun getItemCount(): Int = items.size
}