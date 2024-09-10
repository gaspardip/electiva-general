package com.um.mascotas.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.um.mascotas.databinding.ListItemBinding
import com.um.mascotas.model.PetModel

// Adapter con un clickListener para manejar la selección de un ítem
class ListItemAdapter(
    private val petList: List<PetModel>,
    private val clickListener: (PetModel) -> Unit
) : RecyclerView.Adapter<ListItemAdapter.PetViewHolder>() {

    // ViewHolder para manejar las vistas de cada elemento
    class PetViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pet: PetModel, clickListener: (PetModel) -> Unit) {
            // Asigna los datos de la mascota a las vistas
            binding.name.text = pet.name
            binding.description.text = pet.description

            // Añadir listener para cuando se haga clic en un ítem
            itemView.setOnClickListener {
                clickListener(pet)
            }
        }
    }

    // Crea las vistas para cada elemento en el RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PetViewHolder(binding)
    }

    // Vincula los datos de la mascota a las vistas
    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        holder.bind(petList[position], clickListener)
    }

    // Retorna la cantidad de ítems en la lista
    override fun getItemCount(): Int = petList.size
}
