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
    val like_count: Int = 0,
    val dislike_count: Int = 0,
    val thumbnailUrl: String? = null,
    val downloadCount: Int = 0,
    val upload_documents: List<UploadDocument>
)

data class UploadDocument (
    val document_name: String,
    val file_url: String,
    val file_type: String,
    val coverImageUrl: String?
)