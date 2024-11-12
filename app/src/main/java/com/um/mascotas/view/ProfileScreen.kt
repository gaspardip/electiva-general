// ProfileScreen.kt

package com.um.mascotas.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.um.mascotas.model.enums.UserType
import com.um.mascotas.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val userState by profileViewModel.user.collectAsState()
    val isLoading by profileViewModel.isLoading.collectAsState()
    val isSaving by profileViewModel.isSaving.collectAsState()

    var displayName by remember { mutableStateOf("") }
    var organizationName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        profileViewModel.getUserProfile()
    }

    LaunchedEffect(userState) {
        userState?.let { user ->
            displayName = user.displayName
            organizationName = user.organizationName ?: ""
            phoneNumber = user.phoneNumber ?: ""
            address = user.address ?: ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") }
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
        } else if (userState != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Correo Electrónico:",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = userState!!.email,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Tipo de Usuario:",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = if (userState!!.userType == UserType.DONOR) "Donante" else "Adoptante",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = displayName,
                    onValueChange = { displayName = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (userState!!.userType == UserType.DONOR) {
                    Spacer(modifier = Modifier.height(8.dp))
                    // Campo de Organización
                    OutlinedTextField(
                        value = organizationName,
                        onValueChange = { organizationName = it },
                        label = { Text("Organización (opcional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Campo de Número de Teléfono
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Número de Teléfono") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Campo de Dirección
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Dirección (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        profileViewModel.updateUserProfile(
                            displayName = displayName,
                            organizationName = organizationName,
                            phoneNumber = phoneNumber,
                            address = address
                        )
                    },
                    enabled = !isSaving,
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Guardar") }
            }
        } else {
            // Mostrar mensaje de error si no se pudo cargar el perfil
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No se pudo cargar tu perfil.")
            }
        }
    }
}
