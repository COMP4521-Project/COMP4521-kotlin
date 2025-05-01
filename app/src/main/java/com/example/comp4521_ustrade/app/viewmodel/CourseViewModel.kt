package com.example.comp4521_ustrade.app.viewmodel

import androidx.lifecycle.ViewModel

class CourseViewModel : ViewModel() {
    private val _courseTitles = listOf(
        "ACCT", "BIEN", "CENG", "CIVL", "COMP", "CPEG",
        "ELEC", "MECH", "MATH", "PHYS"
    )

    val courseTitles = _courseTitles

}