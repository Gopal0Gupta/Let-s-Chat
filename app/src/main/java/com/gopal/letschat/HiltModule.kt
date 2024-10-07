package com.gopal.letschat


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
}