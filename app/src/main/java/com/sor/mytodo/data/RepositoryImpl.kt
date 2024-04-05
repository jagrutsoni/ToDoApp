package com.sor.mytodo.data

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sor.mytodo.ToDo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class RepositoryImpl : Repository {

    private var db: FirebaseFirestore = Firebase.firestore
    private val TAG = "ToDoAppRepo"
    private val COLLECTION_NAME = "todos"


    override suspend fun getToDoListFromFirestore() = callbackFlow {
        withContext(Dispatchers.IO) {
            db.collection(COLLECTION_NAME)
                .get()
                .addOnSuccessListener { result ->
                    trySend(result.map { document ->
                        ToDo(
                            document.data["id"].toString(),
                            document.data["task"].toString(),
                            document.data["completed"].toString().toBoolean()
                        )
                    })
                }
                .addOnFailureListener { exception ->
                    Log.d(
                        TAG,
                        "Error getting documents: ",
                        exception
                    )
                }

        }
        awaitClose { }
    }

    override suspend fun addToDoAtFirestore(todo: ToDo) {
        withContext(Dispatchers.IO) {
            db.collection(COLLECTION_NAME)
                .document(todo.id)
                .set(todo)
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot added document") }
                .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
        }
    }

    override suspend fun removeToDoFromFirestore(docID: String) {
        withContext(Dispatchers.IO) {
            db.collection(COLLECTION_NAME).document(docID)
                .delete()
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
        }
    }

    override suspend fun updateToDoAtFirestore(docID: String, todoForUpdate: HashMap<String, Any>) {
        withContext(Dispatchers.IO) {
            db.collection(COLLECTION_NAME)
                .document(docID)
                .update(todoForUpdate as Map<String, Any>);
        }
    }
}
