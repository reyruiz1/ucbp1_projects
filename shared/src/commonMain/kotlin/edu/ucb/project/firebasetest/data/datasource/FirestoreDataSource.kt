package edu.ucb.project.firebasetest.data.datasource

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.serialization.Serializable

@Serializable
data class TestDocument(
    val message: String = ""
)

class FirestoreDataSource {

    private val firestore by lazy { Firebase.firestore }
    private val testCollection by lazy { firestore.collection("test") }

    suspend fun write(id: String, document: TestDocument) {
        testCollection.document(id).set(document)
    }

    suspend fun read(id: String): TestDocument? {
        val snapshot = testCollection.document(id).get()
        return if (snapshot.exists) snapshot.data<TestDocument>() else null
    }
}
