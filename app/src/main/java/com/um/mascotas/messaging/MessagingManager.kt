package com.um.mascotas.messaging

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessagingManager @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging
) {

    fun subscribeToPetsTopic() {
        firebaseMessaging.subscribeToTopic("pets_for_adoption")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Successfully subscribed to pets_for_adoption topic!")
                } else {
                    Log.e("FCM", "Failed to subscribe to pets_for_adoption topic", task.exception)
                }
            }
    }

    fun unsubscribeFromPetsTopic() {
        firebaseMessaging.unsubscribeFromTopic("pets_for_adoption")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Successfully unsubscribed from pets_for_adoption topic!")
                } else {
                    Log.e(
                        "FCM",
                        "Failed to unsubscribe from pets_for_adoption topic",
                        task.exception
                    )
                }
            }
    }
}
