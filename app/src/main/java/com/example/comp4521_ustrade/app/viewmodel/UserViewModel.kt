package com.example.comp4521_ustrade.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {

    private val _level = MutableLiveData(0)
    private val _progress = MutableLiveData(0f)
    private val _uploadCount = MutableLiveData(0)

    //Todo: from database
    fun getUploadCount() {
        // This function is used to get the upload count from the database or any other source.
        // The implementation of this function is not provided in the current code.
        // You can add your logic here to fetch the upload count and update _uploadCount LiveData.
    }


    // open for access
    val uploadCount : LiveData<Int> = _uploadCount
//    val level : LiveData<Int> = _level
//    val progress : LiveData<Float> = _progress

    fun levelUp() {
        _level.value = _level.value?.plus(1)
    }

    fun addUploadCount() {
        _uploadCount.value = _uploadCount.value?.plus(1)
    }

    fun setProgress(value: Float) {
        _progress.value = value
    }



}