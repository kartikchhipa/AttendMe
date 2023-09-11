package com.example.attendme.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendme.model.Department
import com.example.attendme.model.ProfessorModel
import com.example.attendme.model.StudentModel
import com.example.attendme.model.StudentModel1
import com.example.attendme.model.UserRole
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

/*
@HiltViewModel
class RegisterScreenViewModel @Inject constructor() : ViewModel() {
    val name = mutableStateOf("")
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val rePassword = mutableStateOf("")
    val dep_rollNo = mutableStateOf("")

    // val phonenumber = mutableStateOf("")
    val androidID = mutableStateOf("")
    val role = mutableStateOf("Student")

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db1 = Firebase.firestore.collection("Students")
    private val db = Firebase.firestore.collection("Professor")
    private val db2 = Firebase.firestore.collection("User Role")

    fun register(onSuccess: (String?) -> Unit, onFailure: (String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {

            val query = db2.whereEqualTo("androidID", androidID.value).get().await()
            if (query.documents.size > 0) {
                withContext(Dispatchers.Main) {
                    onFailure("This device is already registered")
                }
                return@launch
            }

            if (role.value == "Teacher") {

                if (name.value.isEmpty() || email.value.isEmpty() || password.value.isEmpty() || rePassword.value.isEmpty() || dep_rollNo.value.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        onFailure("Fill the required fields")
                    }
                    return@launch
                }
                if (password.value != rePassword.value) {
                    withContext(Dispatchers.Main) {
                        onFailure("Passwords do not match")
                    }
                    return@launch
                }
                auth.createUserWithEmailAndPassword(email.value, password.value)
                    .addOnSuccessListener { it ->
                        val user = it.user?.let { it1 ->
                            ProfessorModel(
                                it1.uid,
                                name.value,
                                email.value,
                                dep_rollNo.value,
                            )
                        }
                        auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                            db.add(user!!).addOnSuccessListener {
                                val userRole = UserRole(role.value, email.value, androidID.value)
                                db2.add(userRole).addOnSuccessListener {
                                    onSuccess(role.value)
                                }.addOnFailureListener {
                                    onFailure(it.message)
                                }
                            }.addOnFailureListener {
                                onFailure(it.message)
                            }
                        }?.addOnFailureListener { onFailure(it.message) }
                    }.addOnFailureListener {
                        onFailure(it.message)
                    }
            } else {
                if (name.value.isEmpty() || email.value.isEmpty() || password.value.isEmpty() || rePassword.value.isEmpty() || dep_rollNo.value.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        onFailure("Fill the required fields")
                    }
                    return@launch
                }
                if (password.value != rePassword.value) {
                    withContext(Dispatchers.Main) {
                        onFailure("Passwords do not match")
                    }
                    return@launch
                }
                // convert dep_rollNo string to capital case
                val rollNo = dep_rollNo.value.uppercase()
                val query = db1.whereEqualTo("rollNo", rollNo).get().await()
                if (query.documents.isNotEmpty()) {
                    withContext(Dispatchers.Main) {
                        onFailure("Roll Number already exists")
                    }
                    return@launch
                }
                Log.d("RegisterScreenViewModel", "register1: ${rollNo}")
                auth.createUserWithEmailAndPassword(email.value, password.value)
                    .addOnSuccessListener { it ->
                        Log.d("RegisterScreenViewModel", "register2: ${rollNo}")
                        val user = it.user?.let { it1 ->
                            StudentModel(
                                it1.uid,
                                name.value,
                                email.value,
                                dep_rollNo.value,
                                listOf(),
                            )
                        }
                        Log.d("RegisterScreenViewModel", "register3: ${rollNo}")
                        auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                            db1.add(user!!).addOnSuccessListener {
                                val userRole = UserRole(role.value, email.value, androidID.value)
                                db2.add(userRole).addOnSuccessListener {
                                    onSuccess(role.value)
                                }.addOnFailureListener {
                                    onFailure(it.message)
                                }
                            }.addOnFailureListener {
                                onFailure(it.message)
                            }
                        }?.addOnFailureListener {
                            onFailure(it.message)
                        }
                    }.addOnFailureListener {
                        onFailure(it.message)
                    }
            }

        }
    }
}

 */