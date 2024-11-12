package com.um.mascotas.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.um.mascotas.R

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun getSignInRequest(): BeginSignInRequest {

        val context = getApplication<Application>().applicationContext

        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(context.getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()
    }

    fun signInWithGoogle(
        idToken: String,
        onSuccess: (FirebaseUser?) -> Unit,
        onError: (String) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    onSuccess(user)
                } else {
                    onError("Error al autenticar con Firebase: ${task.exception?.message}")
                }
            }
    }

    fun isUserProfileComplete(onResult: (Boolean) -> Unit) {
        val uid = auth.currentUser?.uid ?: ""
        db.collection("users").document(uid)
            .get()
            .addOnSuccessListener { document ->
                onResult(document.exists())
            }
            .addOnFailureListener { e ->
                onResult(false)
            }
    }

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: (FirebaseUser?) -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess(auth.currentUser)
                } else {
                    Log.d("LoginViewModel", "Error al iniciar sesión: ${task.exception?.message}")
                    onError("Usuario o contraseña incorrectos")
                }
            }
    }

    fun registerWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: (FirebaseUser?) -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess(auth.currentUser)
                } else {
                    onError("Error al registrarse: ${task.exception?.message}")
                }
            }
    }
}
