package com.example.attendme.viewModels

import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendme.model.ClassModel
import com.example.attendme.model.Department
import com.example.attendme.model.ProfessorModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Timer
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor() : ViewModel() {



    private val db = Firebase.firestore.collection("Classes")
    private val professorDb = Firebase.firestore.collection("Professor")
    private val auth = FirebaseAuth.getInstance()
    var classesList = mutableStateOf<List<ClassModel>>(emptyList())
    var professor = mutableStateOf(ProfessorModel("","","",""))
    init {
       getProfessor()
    }
    // private val db3 = Firebase.firestore.collection("Sessions")

    fun logout(onSuccess: (String?) -> Unit) {
        // viewModelScope.launch(Dispatchers.IO) {
//            db3.document(db3.whereEqualTo("userid", auth.uid).get().await().documents[0].id).delete().addOnSuccessListener {
                Firebase.auth.signOut()
                onSuccess("Logged out")
//            }.addOnFailureListener {
//                onFailure("Error logging out")
//            }
        //}
    }
    fun getClasses() = CoroutineScope(Dispatchers.IO).launch {
        val personQuery = db.whereEqualTo("profId", auth.uid).get().await()
        if (personQuery.documents.isNotEmpty()) {
            val list = mutableListOf<ClassModel>()
            for (doc in personQuery) {
                var dep = doc.get("department").toString()
                var classes = ClassModel(
                    auth.uid!!,
                    doc.get("classId").toString(),
                    doc.get("className").toString(),
                    doc.get("batch").toString(),
                    dep,
                    doc.get("noOfStudents").toString().toInt(), doc.get("profName").toString()
                )
                Log.d("@@getClasses", classes.toString())
                list.add(classes)
            }
            classesList.value = list
            Log.d("@@getClasses", classesList.toString())
        }

    }
    fun getProfessor() = CoroutineScope(Dispatchers.IO).launch {
        val professorQuery = professorDb.whereEqualTo("id",auth.uid).get().await()
        if(professorQuery.documents.isNotEmpty()){
            for (doc in professorQuery){
                var dep = doc.get("department").toString()
                var prof = ProfessorModel(auth.uid!!,doc.get("name").toString(),doc.get("email").toString(),dep)
                professor.value = prof
            }
        }
    }
}