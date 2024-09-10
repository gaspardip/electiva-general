package com.um.mascotas.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.um.mascotas.R
import com.um.mascotas.databinding.ActivityMainBinding
import com.um.mascotas.view.adapters.ListItemAdapter
import com.um.mascotas.viewmodel.PetViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val petViewModel: PetViewModel by viewModels()

    // Usamos registerForActivityResult para recibir los datos del AddPet
    private val addPetLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                // Extraer los datos del Intent devuelto por AddPet
                val petName = result.data?.getStringExtra("NEW_PET_NAME") ?: ""
                val petDescription = result.data?.getStringExtra("NEW_PET_DESCRIPTION") ?: ""
                val petAge = result.data?.getStringExtra("NEW_PET_AGE") ?: ""
                val petGiver = result.data?.getStringExtra("NEW_PET_GIVER") ?: ""

                // Asegurarse de que todos los campos estén completos
                if (petName.isNotEmpty() && petDescription.isNotEmpty()) {
                    // Agregar la nueva mascota al ViewModel
                    petViewModel.addPetFromView(petName, petDescription, petAge, petGiver)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Observa los datos de la lista de mascotas desde el ViewModel
        petViewModel.petModel.observe(this, Observer { currentPets ->
            val adapter = ListItemAdapter(currentPets) { selectedPet ->
                // Aquí se inicia la actividad de detalles con los datos de la mascota seleccionada
                val intent = Intent(this@MainActivity, DetailPet::class.java).apply {
                    putExtra("PET_NAME", selectedPet.name)
                    putExtra("PET_DESCRIPTION", selectedPet.description)
                    putExtra("PET_AGE", selectedPet.age)
                    putExtra("PET_GIVER", selectedPet.giverId)
                }
                startActivity(intent)
            }
            recyclerView.adapter = adapter
        })

        // Acción al presionar el FloatingActionButton para agregar una nueva mascota
        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddPet::class.java)
            addPetLauncher.launch(intent)
        }
    }
}
