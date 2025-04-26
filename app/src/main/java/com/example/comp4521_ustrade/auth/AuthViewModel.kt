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

class AuthViewModel(val userRepository: UserRepository) : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    
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
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Sign in failed")
            }
        }
    }
    
    fun signUp(email: String, password: String, firstName: String, lastName: String, profilePic: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                auth.createUserWithEmailAndPassword(email, password).await()
                _user.value = auth.currentUser
                createUser(firstName, lastName, profilePic)
                updateUserAuthProfile(firstName, lastName, profilePic)
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Sign up failed")
            }
        }
    }

    private fun createUser(firstName: String, lastName: String, profilePic: String) {
        viewModelScope.launch {
            try {
                val currentUser = _user.value
                if (currentUser != null) {
                    val newUser = User(
                        uid = currentUser.uid,
                        first_name = firstName,
                        last_name = lastName,
                        profile_pic = profilePic,
                    )
                    userRepository.addUser(newUser).onSuccess {
                        print("User profile created in Firestore")
                    }.onFailure { e ->
                        print("Failed to create user profile: ${e.message}")
                    }
                }
            } catch (e: Exception) {
                print("Error creating user profile: ${e.message}")
            }
        }
    }
    
    fun updateUserAuthProfile(firstName: String, lastName: String, profilePic: String) {
        // update firebase user
        val profileUpdates = userProfileChangeRequest {
            displayName = "$firstName $lastName"
            photoUri = profilePic.toUri()
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