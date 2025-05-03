package com.gopal.letschat


import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class HiltModule {
    @Provides
    fun provideAuthentication() : FirebaseAuth = FirebaseAuth.getInstance()
    @Provides
    fun provideFirebaseFirestore() : FirebaseFirestore = FirebaseFirestore.getInstance()
    @Provides
    fun provideStorage() : FirebaseStorage = Firebase.storage
}