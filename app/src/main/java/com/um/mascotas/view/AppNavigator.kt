package com.um.mascotas.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.*
import com.um.mascotas.utils.SnackbarEvent
import com.um.mascotas.utils.SnackbarManager
import com.um.mascotas.viewmodel.SessionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigator(sessionViewModel: SessionViewModel = hiltViewModel()) {
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()

    val currentUser by sessionViewModel.currentUser.collectAsState();

    LaunchedEffect(Unit) {
        SnackbarManager.messages.collect { snackbarEvent ->

            when (snackbarEvent) {
                is SnackbarEvent.Message -> {
                    snackbarHostState.showSnackbar(
                        message = snackbarEvent.message,
                        duration = SnackbarDuration.Short
                    )
                }

                is SnackbarEvent.Error -> {
                    snackbarHostState.showSnackbar(
                        message = snackbarEvent.message,
                        withDismissAction = true,
                        duration = SnackbarDuration.Long
                    )
                }
            }

        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (currentUser != null) "home" else "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") {
                LoginScreen(navController = navController)
            }
            composable("home") {
                HomeScreen(navController = navController)
            }
            composable("profile") {
                ProfileScreen(navController = navController)
            }
            composable("profileSetup") {
                ProfileSetupScreen(navController = navController)
            }
            composable("addAnimal") {
                AddPetScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable("petDetail/{petId}") { backStackEntry ->
                val petId = backStackEntry.arguments?.getString("petId")!!
                PetDetailScreen(navController = navController, petId = petId)
            }
        }
    }
}
