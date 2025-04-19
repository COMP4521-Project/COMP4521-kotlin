package com.example.comp4521_ustrade.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.comp4521_ustrade.app.models.Prize

class UserViewModel : ViewModel() {


    private val _uploadCount = MutableLiveData(0)
    private val _selectedPrize = MutableLiveData<Prize?>()


    fun getUploadCount() {
        // Todo: This function is used to get the upload count from the database or any other source.
        // The implementation of this function is not provided in the current code.
        // You can add your logic here to fetch the upload count and update _uploadCount LiveData.
    }


    // open for access
    val uploadCount : LiveData<Int> = _uploadCount
    val selectedPrize : LiveData<Prize?> = _selectedPrize


    fun addUploadCount() {
        _uploadCount.value = _uploadCount.value?.plus(1)
    }


    fun setSelectedPrize(prize: Prize) {
        _selectedPrize.value = prize
    }



}