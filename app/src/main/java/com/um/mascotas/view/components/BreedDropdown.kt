package com.um.mascotas.view.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.um.mascotas.R
import com.um.mascotas.model.enums.Species
import com.um.mascotas.viewmodel.AddPetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedDropdown(
    selectedSpecies: Species,
    viewModel: AddPetViewModel,
    selectedBreed: String,
    onBreedSelected: (String) -> Unit
) {
    val breeds by viewModel.breeds.collectAsState()
    var expanded by remember { mutableStateOf(false) }


    LaunchedEffect(selectedSpecies) {
        viewModel.fetchBreeds(selectedSpecies)
    }

    ExposedDropdownMenuBox(expanded = expanded,
        onExpandedChange = { expanded = !expanded }) {


        OutlinedTextField(
            value = selectedBreed,
            onValueChange = {},
            label = { Text(stringResource(id = R.string.breed)) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.exposedDropdownSize()
        ) {
            breeds.forEach { breed ->
                DropdownMenuItem(text = { Text(breed) }, onClick = {
                    onBreedSelected(breed)
                    expanded = false
                })
            }
        }
    }
}
