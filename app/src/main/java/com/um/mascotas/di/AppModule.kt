package com.um.mascotas.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.um.mascotas.messaging.MessagingManager
import com.um.mascotas.repository.BreedRepository
import com.um.mascotas.repository.PetsRepository
import com.um.mascotas.repository.StorageRepository
import com.um.mascotas.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage() = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseMessaging() = FirebaseMessaging.getInstance()

    @Provides
    @Singleton
    fun provideMessagingManager(
        firebaseMessaging: FirebaseMessaging
    ) = MessagingManager(firebaseMessaging)

    @Provides
    @Singleton
    fun provideUserRepository(
        auth: FirebaseAuth,
        db: FirebaseFirestore
    ) = UserRepository(auth, db)

    @Provides
    @Singleton
    fun providePetsRepository(
        db: FirebaseFirestore,
        userRepository: UserRepository
    ) = PetsRepository(db, userRepository)

    @Provides
    @Singleton
    fun provideStorageRepository(
        storage: FirebaseStorage
    ) = StorageRepository(storage)

    @Provides
    @Singleton
    fun provideBreedRepository() = BreedRepository()
}
