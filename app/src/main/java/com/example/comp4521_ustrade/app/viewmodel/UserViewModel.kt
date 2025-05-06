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

    private val _firstName = MutableLiveData<String?>()
    val firstName: LiveData<String?> = _firstName

    private val _lastName = MutableLiveData<String?>()
    val lastName: LiveData<String?> = _lastName

    private val _dateOfBirth = MutableLiveData<String?>()
    val dateOfBirth: LiveData<String?> = _dateOfBirth

    private val _description = MutableLiveData<String?>()
    val description: LiveData<String?> = _description

    private val _upload_count = MutableLiveData<String?>()
    val upload_count: LiveData<String?> = _upload_count

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

    suspend fun loadUserData(uid: String) {
        try {
            val user = userRepository.getUser(uid)
            _user.value = user
            _username.value = user?.let { "${it.first_name} ${it.last_name}" }
            _firstName.value = user?.first_name
            _lastName.value = user?.last_name
            _dateOfBirth.value = user?.date_of_birth
            _description.value = user?.description
            _upload_count.value = user?.upload_count.toString()


        } catch (e: Exception) {
            // Handle error appropriately
            e.printStackTrace()
        }
    }

    fun refreshUserData() {
        _userid.value?.let { uid ->
            viewModelScope.launch {
                loadUserData(uid)
            }
        }
    }


    private val _selectedPrize = MutableLiveData<Prize?>()
    private val _confirmedPrize = MutableLiveData<Prize?>()


    val selectedPrize: LiveData<Prize?> = _selectedPrize
    val confirmPrize: LiveData<Prize?> = _confirmedPrize


    fun setSelectedPrize(prize: Prize) {
        _selectedPrize.value = prize
    }

    fun setConfirmedPrize(prize: Prize) {
        _confirmedPrize.value = prize
    }
}