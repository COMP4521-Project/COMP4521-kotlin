package com.example.comp4521_ustrade.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {
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
    
    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                auth.createUserWithEmailAndPassword(email, password).await()
                _user.value = auth.currentUser
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Sign up failed")
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