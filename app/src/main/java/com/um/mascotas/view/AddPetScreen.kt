// AddPetScreen.kt
package com.um.mascotas.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.um.mascotas.R
import com.um.mascotas.model.enums.Gender
import com.um.mascotas.model.enums.Hair
import com.um.mascotas.model.enums.Species
import com.um.mascotas.view.components.BreedDropdown
import com.um.mascotas.view.components.EnumDropdown
import com.um.mascotas.viewmodel.AddPetViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetScreen(
    onNavigateBack: () -> Unit,
    addPetViewModel: AddPetViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    // Estado del formulario y de carga desde el ViewModel
    val formState by addPetViewModel.formState.collectAsState()
    val isFormValid by addPetViewModel.isFormValid.collectAsState()
    val isUploading by addPetViewModel.isUploading.collectAsState()

    // Launcher para seleccionar foto
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            addPetViewModel.updatePhotoUri(uri)
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.add_pet_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                // Campo de Nombre
                OutlinedTextField(
                    value = formState.name,
                    onValueChange = { addPetViewModel.updateName(it) },
                    label = { Text(stringResource(id = R.string.name)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text
                    ),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Dropdown de Especie
                EnumDropdown(
                    label = stringResource(id = R.string.species),
                    options = Species.entries,
                    selectedOption = formState.species,
                    onOptionSelected = { addPetViewModel.updateSpecies(it) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo de Raza
                BreedDropdown(
                    selectedSpecies = formState.species,
                    viewModel = addPetViewModel,
                    selectedBreed = formState.breed,
                    onBreedSelected = { addPetViewModel.updateBreed(it) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo de Edad
                OutlinedTextField(
                    value = formState.age,
                    onValueChange = { addPetViewModel.updateAge(it) },
                    label = { Text(stringResource(id = R.string.age)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Dropdown de Género
                EnumDropdown(
                    label = stringResource(id = R.string.gender),
                    options = Gender.entries,
                    selectedOption = formState.gender,
                    onOptionSelected = { addPetViewModel.updateGender(it) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Dropdown de Pelo
                EnumDropdown(
                    label = stringResource(id = R.string.hair),
                    options = Hair.entries,
                    selectedOption = formState.hair,
                    onOptionSelected = { addPetViewModel.updateHair(it) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo de Descripción
                OutlinedTextField(
                    value = formState.description,
                    onValueChange = { addPetViewModel.updateDescription(it) },
                    label = { Text(stringResource(id = R.string.description)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text
                    ),
                    maxLines = 5
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para seleccionar foto
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        launcher.launch("image/*")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = stringResource(id = R.string.select_photo)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (formState.photoUri != null)
                            stringResource(id = R.string.change_photo)
                        else
                            stringResource(id = R.string.select_photo)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Mostrar la imagen seleccionada (opcional)
                formState.photoUri?.let { uri ->
                    AsyncImage(
                        model = uri,
                        contentDescription = stringResource(id = R.string.selected_photo),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón para guardar el animal
                Button(
                    onClick = {
                        coroutineScope.launch {
                            focusManager.clearFocus()
                            addPetViewModel.addPet()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isFormValid && !isUploading
                ) {
                    if (isUploading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = stringResource(id = R.string.uploading))
                    } else {
                        Text(text = stringResource(id = R.string.save_pet))
                    }
                }
            }
        }
    )
}
