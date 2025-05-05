package com.example.comp4521_ustrade.app.data.dao

data class Document (
    val id: String,
    val title: String,
    val description: String,
    val subject: String,
    val course: DocuCourse,
    val uploaded_by: String,
    val upload_date: String,
    val document_name: String
)


data class DocuCourse (
    val subject: String,
    val code: String,
    val name: String,
)