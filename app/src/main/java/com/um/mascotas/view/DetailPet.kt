package com.um.mascotas.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.um.mascotas.databinding.DetailPetBinding

class DetailPet : AppCompatActivity() {
    private lateinit var binding: DetailPetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializar ViewBinding
        binding = DetailPetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recuperar los detalles de la mascota desde el Intent
        val petName = intent.getStringExtra("PET_NAME")
        val petDescription = intent.getStringExtra("PET_DESCRIPTION")
        val petAge = intent.getStringExtra("PET_AGE")
        val petGiver = intent.getStringExtra("PET_GIVER")

        // Mostrar los datos en las vistas correspondientes
        binding.textName.text = petName
        binding.textDescription.text = petDescription
        binding.textAge.text = petAge
        binding.textGiver.text = petGiver
    }
}
