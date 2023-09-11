package com.example.attendme.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.attendme.model.StudentModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class StudentListViewModel@Inject constructor(val classID: String) : ViewModel() {
    private val db = Firebase.firestore.collection("Classes")
    private val studentDb = Firebase.firestore.collection("Students")
    var student = StudentModel("","","","", emptyList())
    var studentsList = mutableStateOf<List<StudentModel>>(emptyList())
    fun getAllStudent() = CoroutineScope(Dispatchers.IO).launch{
        val studentQuery = studentDb.get().await()
        if(studentQuery.documents.isNotEmpty()){
            var idList: MutableList<String>
            var studentList = mutableListOf<StudentModel>()
            var currStudent : StudentModel
            for(doc in studentQuery){
               idList =  doc.get("classes") as MutableList<String>
                for(value in idList){
                    if(value == classID){
                        currStudent = StudentModel(doc.get("id").toString(),doc.get("name").toString(),doc.get("email").toString(),doc.get("rollNo").toString(),idList)
                        studentList.add(currStudent)
                        break
                    }
                }
            }
            studentsList.value = studentList

        }
    }
}