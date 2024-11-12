// AnimalListComponent.kt

package com.um.mascotas.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.um.mascotas.model.Pet
import com.um.mascotas.viewmodel.PetsViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PetList(
    navController: NavController,
    petsViewModel: PetsViewModel = hiltViewModel()
) {
    val pets by petsViewModel.pets.collectAsState()
    val isLoading by petsViewModel.isLoading.collectAsState()

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {

        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .width(180.dp) // Ancho fijo para el panel de filtros
                    .fillMaxHeight()
                    .padding(8.dp)
            ) {
                FilterPanel(petsViewModel)
            }

            if (pets.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay mascotas para mostrar.")
                }
            } else {
                // Usar LazyVerticalGrid para organizar los elementos en una cuadrícula
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3), // Número de columnas
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 8.dp)
                ) {
                    items(pets) { pet ->
                        PetCard(pet = pet, navController = navController)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetCard(
    pet: Pet,
    navController: NavController
) {
    Card(
        onClick = {
            navController.navigate("petDetail/${pet.id}")
        },
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.9f) // Para mantener las tarjetas cuadradas
            .padding(4.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),

            ) {

            AsyncImage(
                model = pet.photoUrl,
                contentDescription = pet.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp) // Imagen más grande
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = pet.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1

                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Edad: ${pet.age}",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1

                )
            }
        }
    }
}
