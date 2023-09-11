package com.example.attendme.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendme.model.ProfessorModel
import com.example.attendme.model.StudentModel
import com.example.attendme.model.UserRole
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {
    val name = mutableStateOf("")
    val email = Firebase.auth.currentUser?.email?.let { mutableStateOf(it) }
    val dep_rollNo = mutableStateOf("")
    val androidID = mutableStateOf("")
    val role = mutableStateOf("Student")

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db2 = Firebase.firestore.collection("Students")
    private val db1 = Firebase.firestore.collection("Professor")
    private val db = Firebase.firestore.collection("User Role")
    private val db3 = Firebase.firestore.collection("Sessions")

    fun logout(onSuccess: (String?) -> Unit, onFailure: (String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            db3.document(db3.whereEqualTo("userid", auth.uid).get().await().documents[0].id).delete().addOnSuccessListener {
                Firebase.auth.signOut()
                onSuccess("Logged out")
            }.addOnFailureListener {
                onFailure("Error logging out")
            }
        }

    }

    fun register(onSuccess: (String?) -> Unit, onFailure: (String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            if (email?.value?.isEmpty() == true || name.value.isEmpty() || dep_rollNo.value.isEmpty()) {
                withContext(Dispatchers.Main) {
                    onFailure("Fill the required fields")
                }
                return@launch
            }
            val query1 = db.whereEqualTo("androidID", androidID.value).get().await()
            val query2 = db.whereEqualTo("email", email?.value).get().await()

            if (query2.documents.size > 0) {
                withContext(Dispatchers.Main) {
                    Firebase.auth.signOut()
                    onFailure("Account already exists")
                }
                return@launch
            }

            if (query1.documents.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    onFailure("Device already registered with another account")
                }
                return@launch
            }

            if (role.value == "Teacher") {
                val user = Firebase.auth.uid?.let {
                    email?.let { it1 ->
                        ProfessorModel(
                            it,
                            name.value,
                            it1.value,
                            dep_rollNo.value,
                        )
                    }
                }
                db1.add(user!!).addOnSuccessListener {
                    val userRole =
                        email?.let { it1 -> UserRole(role.value, it1.value, androidID.value) }
                    userRole?.let { it1 ->
                        db.add(it1).addOnSuccessListener {
                            onSuccess(role.value)
                        }.addOnFailureListener {
                            onFailure(it.message)
                        }
                    }
                }.addOnFailureListener {
                    onFailure(it.message)
                }
            } else {
                val rollNo = dep_rollNo.value.uppercase()
                val query = db1.whereEqualTo("rollNo", rollNo).get().await()
                if (query.documents.isNotEmpty()) {
                    withContext(Dispatchers.Main) {
                        onFailure("Roll Number already exists")
                    }
                    return@launch
                }

                val user = Firebase.auth.uid?.let {
                    email?.let { it1 ->
                        StudentModel(
                            it,
                            name.value,
                            it1.value,
                            dep_rollNo.value,
                            listOf()
                        )
                    }
                }
                db2.add(user!!).addOnSuccessListener {
                    val userRole =
                        email?.let { it1 -> UserRole(role.value, it1.value, androidID.value) }
                    userRole?.let { it1 ->
                        db.add(it1).addOnSuccessListener {
                            onSuccess(role.value)
                        }.addOnFailureListener {
                            onFailure(it.message)
                        }
                    }
                }.addOnFailureListener {
                    onFailure(it.message)
                }
            }
        }
    }
}