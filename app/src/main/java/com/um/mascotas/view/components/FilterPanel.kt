// FilterPanel.kt
package com.um.mascotas.view.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.um.mascotas.R
import com.um.mascotas.model.enums.*
import com.um.mascotas.model.isDonor
import com.um.mascotas.viewmodel.PetsViewModel
import com.um.mascotas.viewmodel.SessionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterPanel(
    petsViewModel: PetsViewModel,
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    val user by sessionViewModel.currentUser.collectAsState()

    val speciesOptions = Species.values().toList()
    val selectedSpecies by petsViewModel.selectedSpecies.collectAsState()

    val genderOptions = Gender.values().toList()
    val selectedGender by petsViewModel.selectedGender.collectAsState()

    val hairOptions = Hair.values().toList()
    val selectedHair by petsViewModel.selectedHair.collectAsState()

    val selectedBreed by petsViewModel.selectedBreed.collectAsState()
    var breedInput by remember { mutableStateOf(selectedBreed ?: "") }

    val selectedAgeRange by petsViewModel.selectedAgeRange.collectAsState()
    var ageRange by remember {
        mutableStateOf(selectedAgeRange?.let { it.start.toFloat()..it.endInclusive.toFloat() }
            ?: 0f..20f)
    }

    val onlyOwnPets by petsViewModel.onlyOwnPets.collectAsState()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(8.dp)
    ) {
        if (user?.isDonor() == true) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = onlyOwnPets,
                    onCheckedChange = { checked ->
                        petsViewModel.setOnlyOwnPets(checked)
                    }
                )
                Text(
                    text = stringResource(id = R.string.filter_only_own_pets),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }


        // Species Dropdown
        EnumDropdown(
            label = stringResource(id = R.string.filter_species),
            options = speciesOptions,
            selectedOption = selectedSpecies,
            onOptionSelected = { species ->
                petsViewModel.setSpecies(species)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Gender Dropdown
        EnumDropdown(
            label = stringResource(id = R.string.filter_gender),
            options = genderOptions,
            selectedOption = selectedGender,
            onOptionSelected = { gender ->
                petsViewModel.setGender(gender)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Hair Length Dropdown
        EnumDropdown(
            label = stringResource(id = R.string.filter_hair),
            options = hairOptions,
            selectedOption = selectedHair,
            onOptionSelected = { hair ->
                petsViewModel.setHair(hair)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Breed TextField
        OutlinedTextField(
            value = breedInput,
            onValueChange = { value ->
                breedInput = value
            },
            label = { Text(stringResource(id = R.string.filter_breed)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                if (breedInput.isNotEmpty()) {
                    IconButton(onClick = {
                        breedInput = ""
                        petsViewModel.setBreed(null)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.clear_text)
                        )
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Update the breed filter when the user finishes input
        LaunchedEffect(breedInput) {
            petsViewModel.setBreed(if (breedInput.isNotBlank()) breedInput else null)
        }

        // Age Range Slider
        Text(
            text = stringResource(id = R.string.filter_age_range),
            style = MaterialTheme.typography.bodyMedium
        )
        RangeSlider(
            value = ageRange,
            onValueChange = { range ->
                ageRange = range
            },
            valueRange = 0f..20f,
            steps = 19,
            onValueChangeFinished = {
                val intRange = ageRange.start.toInt()..ageRange.endInclusive.toInt()
                petsViewModel.setAgeRange(intRange)
            },
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "${ageRange.start.toInt()} - ${ageRange.endInclusive.toInt()} ${stringResource(id = R.string.years)}",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Clear Filters Button
        OutlinedButton(
            onClick = {
                breedInput = ""
                ageRange = 0f..20f
                petsViewModel.clearFilters()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.filter_clear))
        }
    }
}
