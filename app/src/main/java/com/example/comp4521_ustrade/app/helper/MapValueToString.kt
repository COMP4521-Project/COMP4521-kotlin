package com.example.comp4521_ustrade.app.helper

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.comp4521_ustrade.R

@Composable
fun mapValueToString(value: String): String {
    return when (value) {
        "All" -> stringResource(R.string.All)
        "Read" -> stringResource(R.string.Read)
        "Unread" -> stringResource(R.string.Unread)
        "English" -> stringResource(R.string.English)
        "Traditional Chinese" -> stringResource(R.string.ChineseTraditional)
        "Simplified Chinese" -> stringResource(R.string.ChineseSimplified)
        "Language" -> stringResource(R.string.Language)
        "Science" -> stringResource(R.string.Science)
        "Engineering" -> stringResource(R.string.Engineering)
        "Business" -> stringResource(R.string.Business)
        "COMP" -> stringResource(R.string.COMP)
        "CPEG" -> stringResource(R.string.CPEG)
        "CSIT" -> stringResource(R.string.CSIT)
        "IEDA" -> stringResource(R.string.IEDA)
        "MECH" -> stringResource(R.string.MECH)
        "CIVL" -> stringResource(R.string.CIVL)
        "ELEC" -> stringResource(R.string.ELEC)
        "2025" -> stringResource(R.string.Year2025)
        "2024" -> stringResource(R.string.Year2024)
        "2023" -> stringResource(R.string.Year2023)
        "2022" -> stringResource(R.string.Year2022)
        "2021" -> stringResource(R.string.Year2021)
        "Fall" -> stringResource(R.string.Fall)
        "Spring" -> stringResource(R.string.Spring)
        "Summer" -> stringResource(R.string.Summer)
        "Winter" -> stringResource(R.string.Winter)
        "Select subject" -> stringResource(R.string.SelectSubject)
        "Select subject code" -> stringResource(R.string.SelectSubjectCode)
        "Select year" -> stringResource(R.string.SelectYear)
        "Select semester" -> stringResource(R.string.SelectSemester)
        else -> "Unable to map Value"
    }
}