package com.example.comp4521_ustrade.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comp4521_ustrade.app.data.dao.User
import com.example.comp4521_ustrade.app.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import androidx.core.net.toUri
import com.google.firebase.messaging.FirebaseMessaging

class AuthViewModel() : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    val userRepository = UserRepository()
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState
    
    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user: StateFlow<FirebaseUser?> = _user
    
    init {
        _user.value = auth.currentUser
    }
    
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                auth.signInWithEmailAndPassword(email, password).await()
                _user.value = auth.currentUser
                
                // Get FCM token and update user
                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val token = task.result
                        viewModelScope.launch {
                            _user.value?.uid?.let { uid ->
                                userRepository.updateUser(uid, mapOf("fcm_token" to token))
                            }
                        }
                    }
                }
                
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Sign in failed")
            }
        }
    }
    
    fun signUp(email: String, password: String, firstName: String, lastName: String, profilePic: String?) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                auth.createUserWithEmailAndPassword(email, password).await()
                _user.value = auth.currentUser
//                updateUserAuthProfile(firstName, lastName, profilePic)
                val token = FirebaseMessaging.getInstance().token.await() // Get token synchronously
                createUser(firstName, lastName, profilePic, token)
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Sign up failed")
            }
        }
    }

    private suspend fun createUser(firstName: String, lastName: String, profilePic: String?, fcmToken: String) {
        try {
            val currentUser = _user.value
            if (currentUser != null) {
                val newUser = User(
                    uid = currentUser.uid,
                    first_name = firstName,
                    last_name = lastName,
                    profile_pic = profilePic,
                    fcm_token = fcmToken
                )
                userRepository.addUser(newUser)
                print("User profile created in Firestore")
            }
        } catch (e: Exception) {
            print("Error creating user profile: ${e.message}")
        }
    }
    
    fun updateUserAuthProfile(firstName: String, lastName: String, profilePic: String?) {
        // update firebase auth user
        val profileUpdates = userProfileChangeRequest {
            displayName = "$firstName $lastName"
            photoUri = profilePic?.toUri()
        }
        _user.value = auth.currentUser
        _user.value!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    print("User profile updated.")
                }
            }
    }

    
    fun signOut() {
        auth.signOut()
        _user.value = null
        _authState.value = AuthState.Initial
    }
    
    fun resetPassword(email: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                auth.sendPasswordResetEmail(email).await()
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Password reset failed")
            }
        }
    }
}

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
} 