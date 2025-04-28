package com.example.comp4521_ustrade.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comp4521_ustrade.app.data.dao.User
import com.example.comp4521_ustrade.app.data.repository.UserRepository
import com.example.comp4521_ustrade.app.models.Prize
import com.example.comp4521_ustrade.auth.AuthViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserViewModel(private val authViewModel: AuthViewModel) : ViewModel() {
    private val userRepository = UserRepository()
    
    private val _userid = MutableLiveData<String?>()
    val userid: LiveData<String?> = _userid

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private val _username = MutableLiveData<String?>()
    val username: LiveData<String?> = _username

    init {
        // Observe auth state changes
        viewModelScope.launch {
            authViewModel.user.collectLatest { firebaseUser ->
                _userid.value = firebaseUser?.uid
                // Load user data when UID changes
                _userid.value?.let { uid ->
                    loadUserData(uid)
                }
            }
        }
    }

    private suspend fun loadUserData(uid: String) {
        try {
            val user = userRepository.getUser(uid)
            _user.value = user
            _username.value = user?.let { "${it.first_name} ${it.last_name}" }
        } catch (e: Exception) {
            // Handle error appropriately
            e.printStackTrace()
        }
    }

    private val _uploadCount = MutableLiveData(0)
    private val _selectedPrize = MutableLiveData<Prize?>()
    private val _confirmedPrize = MutableLiveData<Prize?>()

    fun getUploadCount() {
        // Todo: Add logic to fetch the upload count from the database or other sources.
    }

    val uploadCount: LiveData<Int> = _uploadCount
    val selectedPrize: LiveData<Prize?> = _selectedPrize
    val confirmPrize: LiveData<Prize?> = _confirmedPrize

    fun addUploadCount() {
        _uploadCount.value = _uploadCount.value?.plus(1)
    }

    fun setSelectedPrize(prize: Prize) {
        _selectedPrize.value = prize
    }

    fun setConfirmedPrize(prize: Prize) {
        _confirmedPrize.value = prize
    }
}