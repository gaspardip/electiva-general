package com.um.mascotas.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.um.mascotas.databinding.LayoutSampleBinding

class LayoutSample: AppCompatActivity() {
    private lateinit var binding: LayoutSampleBinding
    // Don't use: private val petViewModel: PetViewModel by viewModels();
    // To get current pets, use putExtras when intent is created.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inputFromXml = binding.input;
        val buttonFromXml = binding.button;

        val inputText = inputFromXml.text;

        // onClick
        buttonFromXml.setOnClickListener {
            // callback
        }
    }
}