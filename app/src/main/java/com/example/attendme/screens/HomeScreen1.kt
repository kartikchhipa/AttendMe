package com.example.attendme.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.attendme.R
import com.example.attendme.model.ClassModel
import com.example.attendme.model.ClassModel1
import com.example.attendme.model.ConnectionStates
import com.example.attendme.navigation.Screen
import com.example.attendme.viewModels.HomeScreenViewModel1
import com.example.connectivityState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen1(navHostController: NavHostController) {

    var doubleBackToExitPressedOnce by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val backCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (doubleBackToExitPressedOnce) {
                context.startActivity(Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_HOME)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                })
            } else {
                doubleBackToExitPressedOnce = true
                Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()
                Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
            }

        }
    }
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    onBackPressedDispatcher?.addCallback(backCallback)


    val connection by connectivityState()
    if (connection == ConnectionStates.Disconnected) {
        NoConnectionScreen()
    } else {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        val openDialog = remember { mutableStateOf(false) }
        var expanded by remember { mutableStateOf(false) }
        val viewModel: HomeScreenViewModel1 = viewModel()
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
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                    actions = {

                        IconButton(onClick = { openDialog.value = true }) {
                            Icon(
                                imageVector = Icons.Filled.AccountCircle,
                                contentDescription = "Localized description"
                            )
                        }
                        IconButton(onClick = { expanded = true }) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "Localized description"
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Logout") },
                                onClick = {
                                    viewModel.logout(
                                        onSuccess = {
                                            navHostController.navigate(Screen.LoginScreen.route) {
                                                popUpTo(Screen.HomeScreen.route) {
                                                    inclusive = true
                                                }
                                            }
                                        },
                                        onFailure = {
                                            Toast.makeText(
                                                context,
                                                "Logout Failed",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.Close,
                                        contentDescription = null
                                    )
                                })
                            DropdownMenuItem(
                                text = { Text("Send Feedback") },
                                onClick = { navHostController.navigate(Screen.SendFeedback.route) },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.Email,
                                        contentDescription = null
                                    )
                                })
                        }

                    },
                    scrollBehavior = scrollBehavior
                )
            },
            content = { innerPadding ->
                LazyColumn(
                    contentPadding = innerPadding,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(items = viewModel.enrolledClassesList.value) { classModel ->
                        ClassCard1(classModel) {
                            try {
                                navHostController.navigate("student_view_attendance_screen/${it}")
                            } catch (e: Exception) {
                                Toast.makeText(context, "Some error: $e", Toast.LENGTH_LONG).show()
                            }

                        }
                    }
                }
            },
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    actions = {

                        ExtendedFloatingActionButton(
                            onClick = {

                                val student = viewModel.student.value
                                Log.d("@@Homescreen", student.toString())
                                navHostController.currentBackStackEntry?.savedStateHandle?.set(
                                    "studentModel",
                                    student
                                )
                                navHostController.navigate(Screen.QRScannerScreen.route)
                            },
                            modifier = Modifier.padding(start = 16.dp),
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(
                                defaultElevation = 3.dp
                            ),
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor
                        ) {
                            Text(text = "Mark Attendance")
                        }
                    },
                    floatingActionButton = {

                        FloatingActionButton(
                            onClick = {
                                navHostController.navigate(Screen.JoinCourseScreen.route)
                            },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(
                                defaultElevation = 3.dp
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                "Localized description",
                                Modifier.size(24.dp)
                            )
                        }
                    }
                )
            }

        )
        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Email",
                        )
                        Text(
                            text = viewModel.student.value.name,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.exoregular)),
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = Firebase.auth.currentUser?.email.toString(),
                            fontFamily = FontFamily(Font(R.font.exoregular)),
                        )

                    }
                },
                confirmButton = {

                },
                dismissButton = {
                }
            )


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassCard1(classModel: ClassModel1, onClick: (String) -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .height(180.dp),
        onClick = { onClick(classModel.classId) },

        ) {
        Text(
            text = classModel.className,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp),
            maxLines = 1,
            fontFamily = FontFamily(Font(R.font.exoregular))
        )
        Text(
            text = classModel.batch,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
            maxLines = 1,
            fontFamily = FontFamily(Font(R.font.exoregular))
        )
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = classModel.profName,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
            maxLines = 1,
            fontFamily = FontFamily(Font(R.font.exoregular))
        )
    }
}
