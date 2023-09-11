package com.example.attendme.screens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.attendme.R
import com.example.attendme.model.AttendanceModel
import com.example.attendme.model.AttendanceModel1
import com.example.attendme.model.ConnectionStates
import com.example.attendme.navigation.Screen
import com.example.attendme.viewModels.CourseViewModel
import com.example.connectivityState
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale


@SuppressLint(
    "MutableCollectionMutableState", "UnusedMaterial3ScaffoldPaddingParameter",
    "UnrememberedMutableState"
)
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewAttendance(viewModel: CourseViewModel, navHostController: NavHostController, path: String) {

    val backCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            navHostController.popBackStack()
        }
    }
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    onBackPressedDispatcher?.addCallback(backCallback)

    val attendanceList = remember { mutableStateOf(listOf<AttendanceModel1>()) }
    val datePickerState = rememberDatePickerState()
    val connection by connectivityState()
    if (connection == ConnectionStates.Disconnected) {
        NoConnectionScreen()
    } else {
        val openDialog = remember { mutableStateOf(false) }
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "AttendMe",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontFamily = FontFamily(Font(R.font.exoregular)),
                            fontSize = 20.sp,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            navHostController.navigate(Screen.HomeScreen.route) {
                                popUpTo(Screen.HomeScreen.route) {
                                    inclusive = true
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { openDialog.value = true }) {
                    Icon(Icons.Rounded.DateRange, contentDescription = "calender")
                }
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (openDialog.value) {

                        datePickerState.selectedDateMillis?.let { formatDateFromMillis(it) }
                            ?.let {
                                Log.d(
                                    "123",
                                    it
                                )
                            }
                        val confirmEnabled =
                            derivedStateOf { datePickerState.selectedDateMillis != null }
                        DatePickerDialog(
                            onDismissRequest = {
                                openDialog.value = false
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        openDialog.value = false;
                                        getAttendance(
                                            classID = viewModel.currClass.value.classId,
                                            date = datePickerState.selectedDateMillis?.let {
                                                formatDateFromMillis(
                                                    it
                                                )
                                            }!!,
                                            attendanceList = attendanceList
                                        )
                                    },
                                    enabled = confirmEnabled.value
                                ) {

                                    Text("OK")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = {
                                        openDialog.value = false
                                    }
                                ) {
                                    Text("Cancel")
                                }
                            }
                        ) {
                            DatePicker(state = datePickerState)
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(), verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)

                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Present Students",
                                fontSize = 20.sp,
                                style = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.exoregular)),
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Selected Date : ${
                                    datePickerState.selectedDateMillis?.let {
                                        formatDateFromMillis(
                                            it
                                        )
                                    }
                                }",
                                style = TextStyle(fontFamily = FontFamily(Font(R.font.exoregular)))
                            )

                        }
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            val items1 = attendanceList.value
                            items(items = items1) {

                                ListItem(
                                    headlineContent = {
                                        Text(
                                            text = it.studentName,
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily(Font(R.font.exoregular))
                                            )
                                        )
                                    },
                                    supportingContent = {
                                        Text(
                                            text = it.rollNo,
                                            fontFamily = FontFamily(Font(R.font.exoregular)),
                                        )
                                    },
                                    leadingContent = {
                                        IconButton(enabled = false, onClick = { }) {
                                            Text(text = it.studentName[0].toString())
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

            }
        )
    }

}

fun getAttendance(
    classID: String,
    date: String,
    attendanceList: MutableState<List<AttendanceModel1>>
) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val attendanceList1 = mutableListOf<AttendanceModel1>()
            val classQuery =
                Firebase.firestore.collection("Classes").whereEqualTo("classId", classID).get()
                    .await()

            for (classDoc in classQuery) {
                Log.d("Class", classDoc.id)
                val attendanceQuery =
                    Firebase.firestore.collection("Classes/${classDoc.id}/Attendance")
                        .whereEqualTo("date", date).get().await()
                if (attendanceQuery.documents.isNotEmpty()) {
                    val studentList =
                        attendanceQuery.documents[0].get("studentList") as List<Map<String, Any>>
                    val mappedStudentList = studentList.map {
                        AttendanceModel1(
                            it["id"] as String,
                            it["studentName"] as String,
                            it["time"] as String,
                            it["rollNo"] as String,
                        )
                    }.toMutableList()
                    attendanceList1.addAll(mappedStudentList)
                }
            }
            attendanceList.value = attendanceList1
            Log.d("Attendance", attendanceList1.toString())
        } catch (e: Exception) {
            Log.d("Error1", e.toString())
        }
    }
}

fun formatDateFromMillis(millis: Long): String {
    val pattern = "yyyy-MM-dd"
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(Date(millis))
}


