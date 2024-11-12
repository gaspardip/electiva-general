package com.um.mascotas.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.um.mascotas.viewmodel.PetDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetailScreen(
    navController: NavController,
    petId: String,
    petDetailViewModel: PetDetailViewModel = hiltViewModel()
) {
    var scrollState = rememberScrollState();
    val animalState by petDetailViewModel.pet.collectAsState()
    val donorState by petDetailViewModel.donor.collectAsState()
    val isLoading by petDetailViewModel.isLoading.collectAsState()

    LaunchedEffect(petId) {
        petDetailViewModel.fetchPetDetails(petId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalles de la mascota") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (animalState != null && donorState != null) {
            val animal = animalState!!
            val donor = donorState!!

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(scrollState),
            ) {
                // Animal Image
                if (animal.photoUrl.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(animal.photoUrl),
                        contentDescription = animal.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Animal Details
                Text(animal.name, style = MaterialTheme.typography.headlineMedium)
                Text(
                    "Especie: ${stringResource(animal.species.displayNameResId)}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text("Raza: ${animal.breed}", style = MaterialTheme.typography.bodyLarge)
                Text("Edad: ${animal.age}", style = MaterialTheme.typography.bodyLarge)
                Text(
                    "Género: ${stringResource(animal.gender.displayNameResId)}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Descripción:", style = MaterialTheme.typography.titleMedium)
                Text(animal.description, style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(16.dp))

                // Donor Details
                Text("Contacto del Donante:", style = MaterialTheme.typography.titleMedium)
                Text("Nombre: ${donor.displayName}", style = MaterialTheme.typography.bodyLarge)
                donor.organizationName?.let {
                    Text("Organización: $it", style = MaterialTheme.typography.bodyLarge)
                }
                donor.phoneNumber?.let {
                    Text("Teléfono: $it", style = MaterialTheme.typography.bodyLarge)
                }
                donor.email?.let {
                    Text("Email: $it", style = MaterialTheme.typography.bodyLarge)
                }
            }
        } else {
            // Error state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No se pudo cargar la información del animal.")
            }
        }
    }
}
