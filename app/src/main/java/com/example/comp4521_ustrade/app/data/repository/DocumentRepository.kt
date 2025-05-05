package com.example.comp4521_ustrade.app.data.repository

import com.example.comp4521_ustrade.app.data.dao.DocuCourse
import com.example.comp4521_ustrade.app.data.dao.Document
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
                "course" to document.course,
                "document_name" to document.document_name

            )
            documentsCollection.document(document.id).set(documentMap).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun updateDocument(id: String, updates: Map<String, Any>) {
        try {
            documentsCollection.document(id).update(updates).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getDocument(id: String): Document? {
        return try {
            val document = documentsCollection.document(id).get().await()
            if (document.exists()) {
                val data = document.data
                if (data != null) {
                    Document(
                        id = data["id"] as String,
                        title = data["title"] as String,
                        description = data["description"] as String,
                        uploaded_by = data["uploaded_by"] as String,
                        upload_date = data["upload_date"] as String,
                        subject = data["subject"] as String,
                        course = data["course"] as DocuCourse,
                        document_name = data["document_name"] as String
                    )
                } else null
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getAllDocuments(): List<Document> {
        return try {
            val snapshot = documentsCollection.get().await()
            snapshot.documents.mapNotNull { doc ->
                val data = doc.data
                if (data != null) {
                    Document(
                        id = data["id"] as String,
                        title = data["title"] as String,
                        description = data["description"] as String,
                        uploaded_by = data["uploaded_by"] as String,
                        upload_date = data["upload_date"] as String,
                        subject = data["subject"] as String,
                        course = data["course"] as DocuCourse,
                        document_name = data["document_name"] as String
                    )
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
        }
    }
}