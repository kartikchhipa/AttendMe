package com.example.attendme.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StudentModel1(
    val id: String,
    val name: String,
    val email: String,
    val rollNo: String,
    val classes : List<String>,
): Parcelable
