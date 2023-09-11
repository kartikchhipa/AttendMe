package com.example.attendme.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {

    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val rePassword = mutableStateOf("")

    fun register(onSuccess: (String?) -> Unit, onFailure: (String?) -> Unit) {

        Firebase.auth.createUserWithEmailAndPassword(email.value, password.value)
            .addOnSuccessListener {
                Firebase.auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                    Firebase.auth.signOut()
                    onSuccess("Verification Email Sent")
                }?.addOnFailureListener {
                    Firebase.auth.signOut()
                    onFailure(it.message)
                }
            }.addOnFailureListener {
                onFailure(it.message)
            }
    }
}