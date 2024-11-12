// ProfileSetupScreen.kt

package com.um.mascotas.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.um.mascotas.model.enums.UserType
import com.um.mascotas.viewmodel.ProfileSetupViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileSetupScreen(
    navController: NavController,
    profileSetupViewModel: ProfileSetupViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    var userType by remember { mutableStateOf(UserType.ADOPTER) }
    var organizationName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Completa tu perfil", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Selector de tipo de usuario
        Text("Soy un:")

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            RadioButton(
                selected = userType == UserType.DONOR,
                onClick = { userType = UserType.DONOR }
            )
            Text("Donante")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = userType == UserType.ADOPTER,
                onClick = { userType = UserType.ADOPTER }
            )
            Text("Adoptante")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campos adicionales para donantes
        if (userType == UserType.DONOR) {
            OutlinedTextField(
                value = organizationName,
                onValueChange = { organizationName = it },
                label = { Text("Nombre de la organización (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Número de teléfono") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Dirección (opcional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    profileSetupViewModel.saveUserProfile(
                        userType = userType,
                        organizationName = if (userType == UserType.DONOR) organizationName else null,
                        phoneNumber = phoneNumber,
                        address = address
                    ) {
                        // Navegar a la pantalla principal
                        navController.navigate("home") {
                            popUpTo("profileSetup") { inclusive = true }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = phoneNumber.isNotEmpty()
        ) {
            Text("Guardar")
        }
    }
}
