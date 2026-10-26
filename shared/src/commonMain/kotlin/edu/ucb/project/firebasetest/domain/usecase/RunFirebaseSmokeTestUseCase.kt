package edu.ucb.project.firebasetest.domain.usecase

import edu.ucb.project.firebasetest.data.datasource.FirestoreDataSource
import edu.ucb.project.firebasetest.data.datasource.MessagingDataSource
import edu.ucb.project.firebasetest.data.datasource.RemoteConfigDataSource
import edu.ucb.project.firebasetest.data.datasource.TestDocument

class RunFirebaseSmokeTestUseCase(
    val firestoreDataSource: FirestoreDataSource,
    val remoteConfigDataSource: RemoteConfigDataSource,
    val messagingDataSource: MessagingDataSource
) {
    suspend fun invoke() {
        try {
            firestoreDataSource.write("smoke", TestDocument(message = "hola desde KMP"))
            val document = firestoreDataSource.read("smoke")
            println("FirebaseSmokeTest Firestore OK: $document")
        } catch (e: Exception) {
            println("FirebaseSmokeTest Firestore ERROR: ${e.message}")
        }

        try {
            val activated = remoteConfigDataSource.fetchAndActivate()
            val welcomeMessage = remoteConfigDataSource.getString("welcome_message")
            println("FirebaseSmokeTest RemoteConfig OK: activated=$activated welcome_message=$welcomeMessage")
        } catch (e: Exception) {
            println("FirebaseSmokeTest RemoteConfig ERROR: ${e.message}")
        }

        try {
            val token = messagingDataSource.getToken()
            println("FirebaseSmokeTest FCM OK: token=$token")
        } catch (e: Exception) {
            println("FirebaseSmokeTest FCM ERROR: ${e.message}")
        }
    }
}
