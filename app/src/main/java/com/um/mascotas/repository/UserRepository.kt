package com.um.mascotas.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.um.mascotas.model.User
import com.um.mascotas.model.enums.UserType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    db: FirebaseFirestore
) {
    private val collection = db.collection("users");

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> get() = _currentUser

    private val _isLoadingUser = MutableStateFlow(true)
    val isLoadingUser: StateFlow<Boolean> get() = _isLoadingUser

    suspend fun fetchUserById(userId: String): User? {
        val documentSnapshot = collection.document(userId).get().await()
        return documentSnapshot.toUser()
    }

    suspend fun fetchCurrentUser(): User? {
        _isLoadingUser.value = true

        try {
            val uid = auth.currentUser?.uid ?: ""

            _currentUser.value = fetchUserById(uid)

            return _currentUser.value
        } catch (e: Exception) {
            _currentUser.value = null
        } finally {
            _isLoadingUser.value = false
        }

        return null;
    }

    suspend fun saveUser(user: User) {
        collection.document(user.id).set(user).await()
    }

    fun signOut() {
        auth.signOut()
        _currentUser.value = null
    }
}

fun DocumentSnapshot.toUser(): User? {
    val userTypeString = getString("userType") ?: UserType.ADOPTER.name

    return this.toObject(User::class.java)?.copy(
        userType = UserType.valueOf(userTypeString),
    )
}
