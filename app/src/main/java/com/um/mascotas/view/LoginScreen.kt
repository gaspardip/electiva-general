// LoginScreen.kt

package com.um.mascotas.view

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.um.mascotas.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel()
) {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode != Activity.RESULT_OK) {
                Toast.makeText(context, "Inicio de sesión cancelado", Toast.LENGTH_SHORT).show()
                isLoading = false
                return@rememberLauncherForActivityResult
            }

            try {
                val oneTapClient = Identity.getSignInClient(context)
                val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken

                if (idToken == null) {
                    Toast.makeText(
                        context,
                        "No se pudo obtener el ID Token",
                        Toast.LENGTH_SHORT
                    ).show()
                    isLoading = false
                    return@rememberLauncherForActivityResult
                }

                isLoading = true

                loginViewModel.signInWithGoogle(idToken,
                    onSuccess = { user ->
                        isLoading = false
                        loginViewModel.isUserProfileComplete { isComplete ->
                            if (isComplete) {
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                navController.navigate("profileSetup") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        }
                    },
                    onError = { message ->
                        isLoading = false
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                )

            } catch (e: ApiException) {
                isLoading = false
                Toast.makeText(
                    context,
                    "Error al obtener credenciales: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    )

    // UI
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Iniciar Sesión",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Bienvenido a PetConnect",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Campos de correo y contraseña
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo electrónico") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón de Iniciar Sesión
                    Button(
                        onClick = {
                            isLoading = true
                            loginViewModel.signInWithEmailAndPassword(
                                email = email,
                                password = password,
                                onSuccess = { user ->
                                    isLoading = false
                                    loginViewModel.isUserProfileComplete { isComplete ->
                                        if (isComplete) {
                                            navController.navigate("home") {
                                                popUpTo("login") { inclusive = true }
                                            }
                                        } else {
                                            navController.navigate("profileSetup") {
                                                popUpTo("login") { inclusive = true }
                                            }
                                        }
                                    }
                                },
                                onError = { message ->
                                    isLoading = false
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = email.isNotEmpty() && password.isNotEmpty()
                    ) {
                        Text(text = "Iniciar Sesión")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Botón de Registrarse
                    Button(
                        onClick = {
                            isLoading = true
                            loginViewModel.registerWithEmailAndPassword(
                                email = email,
                                password = password,
                                onSuccess = { user ->
                                    isLoading = false
                                    navController.navigate("profileSetup") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onError = { message ->
                                    isLoading = false
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = email.isNotEmpty() && password.isNotEmpty()
                    ) {
                        Text(text = "Registrarse")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "O continúa con",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            isLoading = true
                            val oneTapClient = Identity.getSignInClient(context)
                            val signInRequest = loginViewModel.getSignInRequest()
                            oneTapClient.beginSignIn(signInRequest)
                                .addOnSuccessListener { result ->
                                    try {
                                        val intentSenderRequest =
                                            IntentSenderRequest.Builder(result.pendingIntent.intentSender)
                                                .build()
                                        launcher.launch(intentSenderRequest)
                                    } catch (e: Exception) {
                                        isLoading = false
                                        Toast.makeText(
                                            context,
                                            "Error al iniciar One Tap UI: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                .addOnFailureListener { e ->
                                    isLoading = false
                                    Toast.makeText(
                                        context,
                                        "Error al iniciar sesión: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Continuar con Google")
                    }
                }
            }
        }
    }
}
