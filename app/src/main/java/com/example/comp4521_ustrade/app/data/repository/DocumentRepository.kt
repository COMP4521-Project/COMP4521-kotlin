package com.example.comp4521_ustrade.app.data.repository

import com.example.comp4521_ustrade.app.data.dao.Document
import com.example.comp4521_ustrade.app.data.dao.UploadDocument
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class DocumentRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val documentsCollection = firestore.collection("documents")

    suspend fun addDocument(document: Document) {
        try {
            val documentMap = hashMapOf(
                "id" to document.id,
                "title" to document.title,
                "description" to document.description,
                "uploaded_by" to document.uploaded_by,
                "upload_date" to document.upload_date,
                "subject" to document.subject,
                "subjectCode" to document.subjectCode,
                "course" to document.course,
                "year" to document.year,
                "semester" to document.semester,
                "thumbnailUrl" to document.thumbnailUrl,
                "upload_documents" to document.upload_documents,
                "downloadCount" to document.downloadCount,
                "like_count" to document.like_count,
                "dislike_count" to document.dislike_count
            )
            documentsCollection.document(document.id).set(documentMap).await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun updateDocument(id: String, updates: Map<String, Any>) {
        try {
            documentsCollection.document(id).update(updates).await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    // Helper function to convert Firestore document data to Document object
    @Suppress("UNCHECKED_CAST")
    private fun mapToDocument(data: Map<String, Any>): Document? {
        return try {
            // Enhanced error handling for each field
            val id = data["id"] as? String ?: return null
            val title = data["title"] as? String ?: "Untitled Document"
            val description = data["description"] as? String ?: ""
            val uploadedBy = data["uploaded_by"] as? String ?: ""
            val uploadDate = data["upload_date"] as? String ?: ""
            val subject = data["subject"] as? String ?: ""
            val subjectCode = data["subjectCode"] as? String ?: ""
            val course = data["course"] as? String ?: ""
            val year = data["year"] as? String ?: ""
            val semester = data["semester"] as? String ?: ""
            val thumbnailUrl = data["thumbnailUrl"] as? String
            val likeCount = (data["like_count"] as? Number)?.toInt() ?: 0
            val dislikeCount = (data["dislike_count"] as? Number)?.toInt() ?: 0
            val downloadCount = (data["downloadCount"] as? Number)?.toInt() ?: 0
            
            // Handle the upload_documents list with extra care
            val uploadDocsList = (data["upload_documents"] as? List<Map<String, Any>>) ?: emptyList()
            val uploadDocuments = uploadDocsList.mapNotNull { uploadDocData ->
                try {
                    UploadDocument(
                        document_name = uploadDocData["document_name"] as? String ?: "Unnamed",
                        file_url = uploadDocData["file_url"] as? String ?: "",
                        file_type = uploadDocData["file_type"] as? String ?: "",
                        coverImageUrl = uploadDocData["coverImageUrl"] as? String
                    )
                } catch (e: Exception) {
                    println("DEBUG: Error mapping upload document: ${e.message}")
                    null
                }
            }
            
            Document(
                id = id,
                title = title,
                description = description,
                uploaded_by = uploadedBy,
                upload_date = uploadDate,
                subject = subject,
                subjectCode = subjectCode,
                course = course,
                year = year,
                semester = semester,
                thumbnailUrl = thumbnailUrl,
                like_count = likeCount,
                dislike_count = dislikeCount,
                downloadCount = downloadCount,
                upload_documents = uploadDocuments
            )
        } catch (e: Exception) {
            println("DEBUG: Error in mapToDocument: ${e.message}")
            null
        }
    }

    suspend fun getDocument(id: String): Document? {
        return try {
            val document = documentsCollection.document(id).get().await()
            if (document.exists()) {
                val data = document.data
                if (data != null) {
                    mapToDocument(data)
                } else null
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getSubjectSpecificDocuments(subject: String): List<Document> {
        return try {
            val snapshot = documentsCollection.whereEqualTo("subject", subject).get().await()
            snapshot.documents.mapNotNull { doc ->
                val data = doc.data
                if (data != null) {
                    mapToDocument(data)
                } else null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getUserSpecificDocuments(userID: String): List<Document> {
        return try {
            val snapshot = documentsCollection.whereEqualTo("uploaded_by", userID).get().await()
            snapshot.documents.mapNotNull { doc ->
                val data = doc.data
                if (data != null) {
                    mapToDocument(data)
                } else null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getAllDocuments(): List<Document> {
        return try {
            val snapshot = documentsCollection.get().await()
            snapshot.documents.mapNotNull { doc ->
                val data = doc.data
                if (data != null) {
                    mapToDocument(data)
                } else null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun deleteDocument(id: String) {
        try {
            documentsCollection.document(id).delete().await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun getDocumentsByIds(ids: List<String>): List<Document> {
        return try {
            if (ids.isEmpty()) return emptyList()
            val tasks = ids.map { documentsCollection.document(it).get() }
            val snapshots = tasks.map { it.await() }
            snapshots.mapNotNull { doc ->
                val data = doc.data
                if (data != null) {
                    mapToDocument(data)
                } else null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    //extra features
    suspend fun addLikeToDocument(id: String) {
        try {
            documentsCollection.document(id)
                .update("like_count", FieldValue.increment(1))
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun removeLikeFromDocument(id: String) {
        try {
            documentsCollection.document(id)
                .update("like_count", FieldValue.increment(-1))
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun addDislikeToDocument(id: String) {
        try {
            documentsCollection.document(id)
                .update("dislike_count", FieldValue.increment(1))
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun removeDislikeFromDocument(id: String) {
        try {
            documentsCollection.document(id)
                .update("dislike_count", FieldValue.increment(-1))
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //search functions
    suspend fun searchDocumentsByCoursePrefix(query: String): List<Document> {
        return try {
            val snapshot = documentsCollection
                .orderBy("course")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .get()
                .await()
            snapshot.documents.mapNotNull { doc ->
                val data = doc.data
                if (data != null) {
                    mapToDocument(data)
                } else null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun searchDocumentsByCourseContains(query: String): List<Document> {
        return try {
            val snapshot = documentsCollection.get().await()
            snapshot.documents.mapNotNull { doc ->
                val data = doc.data
                if (data != null) {
                    val course = data["course"] as String
                    if (course.contains(query, ignoreCase = true)) {
                        mapToDocument(data)
                    } else null
                } else null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun searchDocumentsBySubjectAndCourse(subject: String, query: String): List<Document> {
        return try {
            val snapshot = documentsCollection
                .whereEqualTo("subject", subject)
                .get()
                .await()
            snapshot.documents.mapNotNull { doc ->
                val data = doc.data
                if (data != null) {
                    val course = data["course"] as String
                    if (course.contains(query, ignoreCase = true)) {
                        mapToDocument(data)
                    } else null
                } else null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}