package com.example.attendme.screens

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
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
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.attendme.model.ConnectionStates
import com.example.attendme.navigation.Screen
import com.example.attendme.viewModels.CourseViewModel
import com.example.connectivityState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentViewAttendanceScreen( viewModel: CourseViewModel, navHostController: NavHostController, classID: String) {

    val backCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // Perform navigation to the NavigateScreen
            navHostController.popBackStack()
        }
    }
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    onBackPressedDispatcher?.addCallback(backCallback)

    val attendanceList = remember { mutableStateOf(listOf<String>()) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val connectionState by connectivityState()
    if(connectionState == ConnectionStates.Disconnected){
        NoConnectionScreen()
    }
    else {
        getAttendance1(viewModel.classID, attendanceList)
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Attendance",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontFamily = FontFamily(Font(R.font.exoregular)),
                            fontSize = 20.sp,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            navHostController.navigate(Screen.HomeScreen1.route) {
                                popUpTo(Screen.HomeScreen1.route) {
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
            }
        ) { paddingValues ->
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
                            text = "Present On",
                            fontSize = 20.sp,
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.exoregular)),
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        var count = 0
                        val items1 = attendanceList.value
                        items(items = items1) {
                            count += 1
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = it,
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily(Font(R.font.exoregular))
                                        )
                                    )
                                },
                                leadingContent = {
                                    Text(
                                        text = count.toString(), style = TextStyle(
                                            fontFamily = FontFamily(Font(R.font.exoregular))
                                        )
                                    )
                                },
                            )
                        }
                    }
                }
            }

        }
    }
}

fun getAttendance1(
    classID: String,
    attendanceList: MutableState<List<String>>
) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val attendanceList1 = mutableListOf<String>()
            val classQuery =
                Firebase.firestore.collection("Classes").whereEqualTo("classId", classID).get()
                    .await()

            for (classDoc in classQuery) {
                val attendanceQuery =
                    Firebase.firestore.collection("Classes/${classDoc.id}/Attendance").get().await()
                for(attendanceDoc in attendanceQuery){
                    val studentList = attendanceDoc.get("studentList") as List<Map<String, Any>>
                    val mappedStudentList = studentList.map {
                        MapOf(
                            it["id"] as String,
                            it["time"] as String,
                        )
                    }.toMutableList()
                    for (student in mappedStudentList){
                        if(student.id == Firebase.auth.uid){
                            attendanceList1.add(student.time)
                        }
                    }
                }
            }
            attendanceList.value = attendanceList1
            Log.d("Attendance", attendanceList1.toString())
        } catch (e: Exception) {
            Log.d("Error1", e.toString())
        }
    }
}

data class MapOf(
    val id: String,
    val time: String,
)
