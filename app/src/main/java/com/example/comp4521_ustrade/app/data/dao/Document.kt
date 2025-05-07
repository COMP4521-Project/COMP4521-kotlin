package com.example.comp4521_ustrade.app.data.dao

data class Document (
    val id: String,
    val title: String,
    val description: String,
    val subject: String,
    val subjectCode: String,
    val course: String,
    val year: String,
    val semester: String,
    val uploaded_by: String,
    val upload_date: String,
    val document_name: String,
    val like_count: Int = 0,
    val dislike_count: Int = 0,
    val file_urls: List<String> = emptyList(),
    val file_types: List<String> = emptyList()
)

