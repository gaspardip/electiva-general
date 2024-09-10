package com.um.mascotas.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.um.mascotas.R
import com.um.mascotas.databinding.ActivityMainBinding
import com.um.mascotas.view.adapters.ListItemAdapter
import com.um.mascotas.viewmodel.PetViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding;

    private val petViewModel: PetViewModel by viewModels();

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val petName = result.data?.getStringExtra("NEW_PET_NAME")!!
                val petDescription = result.data?.getStringExtra("NEW_PET_DESCRIPTION")!!
                val petAge = result.data?.getStringExtra("NEW_PET_AGE")!!
                val petGiver = result.data?.getStringExtra("NEW_PET_GIVER")!!

                    petViewModel.addPetFromView(petName, petDescription, petAge, petGiver)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        binding.floatingActionButton.setOnClickListener {
            petViewModel.addRandomPet()

            val intent = Intent(this, AddPet::class.java)
            resultLauncher.launch(intent);
        }

        petViewModel.petModel.observe(this, Observer { currentPets ->
            val adapter = ListItemAdapter(petViewModel, currentPets)
            recyclerView.adapter = adapter
        });
    }

}

// check bottom
/*@Composable
fun MyApp() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        MyMinimalScaffold()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMinimalScaffold() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hello, Material 3") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Handle FAB click */ }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Welcome to Jetpack Compose with Material 3!")

                // Button
                Button(onClick = { /* Handle button click */ }) {
                    Text("Click Me")
                }

                // Outlined Button
                OutlinedButton(onClick = { /* Handle button click */ }) {
                    Text("Outlined Button")
                }

                // Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Card Title", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "This is a sample card content.")
                    }
                }

                // Switch
                var isChecked by remember { mutableStateOf(false) }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it }
                    )
                    Text(text = if (isChecked) "Switch ON" else "Switch OFF")
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MyMinimalScaffoldPreview() {
    MyApp()
}
*/