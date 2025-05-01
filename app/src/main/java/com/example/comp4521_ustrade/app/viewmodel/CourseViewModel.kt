package com.example.comp4521_ustrade.app.viewmodel

import androidx.lifecycle.ViewModel

//connected sidenav drawer and the navtab in the HomePage
class CourseViewModel : ViewModel() {
    private val _courseTitles = listOf(
        "ACCT", "BIEN", "CENG", "CIVL", "COMP", "CPEG",
        "ELEC", "MECH", "MATH", "PHYS"
    )

    val courseTitles = _courseTitles

}