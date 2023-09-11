package com.example.attendme.viewModels

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.EmailAuthProvider
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
import kotlin.coroutines.suspendCoroutine


@HiltViewModel
class LoginScreenViewModel @Inject constructor() : ViewModel() {
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val androidID = mutableStateOf("")


    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val db = Firebase.firestore.collection("User Role")
    private val db3 = Firebase.firestore.collection("Sessions")

    fun login(onSuccess: (String?) -> Unit, onFailure: (String?) -> Unit) {
        auth.signInWithEmailAndPassword(email.value, password.value).addOnSuccessListener {
            viewModelScope.launch {
                val query = db.whereEqualTo("email", email.value).get().await()
                // val query3 = db3.whereEqualTo("userid", Firebase.auth.uid).get().await()
                // if (query3.documents.size == 0) {
                if (auth.currentUser?.isEmailVerified == true) {
                    if (query.documents.size == 0) {
//                        db3.add(
//                            hashMapOf(
//                                "userid" to Firebase.auth.uid,
//                            )
//                        )
                        onSuccess("Onboarding")
                    } else {
                        val retrievedAndroidID =
                            query.documents[0].get("androidID").toString()
                        if (retrievedAndroidID != androidID.value) {
                            Firebase.auth.signOut()
                            onFailure("Cannot Use this account on your device")
                        } else {
                            val role = query.documents[0].get("role").toString()
//                            db3.add(
//                                hashMapOf(
//                                    "userid" to Firebase.auth.uid,
//                                )
//                            )
                            onSuccess(role)
                        }
                    }
                } else {
                    Firebase.auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                        auth.signOut()
                        onSuccess("Verification")
                    }?.addOnFailureListener {
                        auth.signOut()
                        onFailure(it.message)
                    }
                }
//                } else {
//                    auth.signOut()
//                    onFailure("This account is already logged in on another device")
//                }
            }
        }.addOnFailureListener {
            onFailure(it.message)
        }
    }

}