package com.example.comp4521_ustrade.app.data.dao

data class Document (
    val id: String,
    val title: String,
    val description: String,
    val subject: String,
    val subjectCode: String,
    val course: String,
    val uploaded_by: String,
    val upload_date: String,
    val document_name: String
)


data class DocuCourse (
    val id: String,
    val code: String, //e.g 4521
    val subject: String, //e.g. COMP
)