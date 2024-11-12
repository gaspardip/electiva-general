package com.um.mascotas.view


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.um.mascotas.R
import com.um.mascotas.model.enums.UserType
import com.um.mascotas.view.components.PetList
import com.um.mascotas.viewmodel.SessionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, sessionViewModel: SessionViewModel = hiltViewModel()) {

    val currentUser by sessionViewModel.currentUser.collectAsState()
    val isLoadingUser by sessionViewModel.isLoadingUser.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.welcome)) },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("profile")
                    }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Perfil"
                        )
                    }

                    IconButton(onClick = {
                        sessionViewModel.signOut()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar sesión"
                        )
                    }


                }
            )
        },
        floatingActionButton = {
            if (currentUser?.userType == UserType.DONOR) {
                ExtendedFloatingActionButton(
                    onClick = { navController.navigate("addAnimal") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(id = R.string.add_pet)
                        )
                    },
                    text = { Text(stringResource(id = R.string.add_new_pet)) },
                    containerColor = MaterialTheme.colorScheme.primary
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (isLoadingUser) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                return@Column
            }

            if (currentUser == null) {
                Text(
                    text = "No se pudo cargar la información del usuario.",
                    style = MaterialTheme.typography.bodyLarge
                )

                return@Column
            }

            Text(
                text = "Hola, ${currentUser!!.displayName}!",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            PetList(
                navController = navController
            )
        }
    }
}
