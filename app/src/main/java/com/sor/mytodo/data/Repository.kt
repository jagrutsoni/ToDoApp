package com.sor.mytodo.data

import com.sor.mytodo.ToDo
import kotlinx.coroutines.flow.Flow

interface Repository {

   suspend fun getToDoListFromFirestore(): Flow<List<ToDo>>
   suspend fun addToDoAtFirestore(todo: ToDo)
   suspend fun removeToDoFromFirestore(docID: String)
   suspend fun updateToDoAtFirestore(docID: String, todoForUpdate: HashMap<String, Any>)

}