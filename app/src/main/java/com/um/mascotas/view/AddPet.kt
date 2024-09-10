package com.um.mascotas.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.um.mascotas.databinding.AddPetBinding

class AddPet : AppCompatActivity() {
    private lateinit var binding: AddPetBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AddPetBinding.inflate(layoutInflater);
        setContentView(binding.root);

        binding.addPetButton.setOnClickListener {
            val petName = binding.inputName.text.toString();
            val petDescription = binding.inputDescription.text.toString();
            val petAge = binding.inputAge.text.toString();
            val petGiver = binding.inputGiver.text.toString();

            if (petName.isNotEmpty()) {
                val resultIntent = Intent()
                resultIntent.putExtra("NEW_PET_NAME", petName)
                resultIntent.putExtra("NEW_PET_DESCRIPTION", petDescription)
                resultIntent.putExtra("NEW_PET_AGE", petAge)
                resultIntent.putExtra("NEW_PET_GIVER", petGiver)
                setResult(Activity.RESULT_OK, resultIntent)
                finish() // Close the current activity and back to MainActivity
            }
        }
    }
}