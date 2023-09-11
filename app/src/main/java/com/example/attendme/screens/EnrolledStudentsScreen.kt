package com.example.attendme.screens

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
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
import com.example.attendme.viewModels.HomeScreenViewModel
import com.example.attendme.viewModels.StudentListViewModel
import com.example.connectivityState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnrolledStudentsScreen(viewModel: StudentListViewModel, navHostController: NavHostController) {

    val backCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            navHostController.popBackStack()
        }
    }
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    onBackPressedDispatcher?.addCallback(backCallback)
    val context = LocalContext.current
    val connection by connectivityState()
    if(connection == ConnectionStates.Disconnected){
        NoConnectionScreen()
    }
    else {
        val viewModel_Prof = HomeScreenViewModel()
        LaunchedEffect(key1 = true, block = { viewModel.getAllStudent() })
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        var expanded by remember { mutableStateOf(false) }
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
                    actions = {
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
                                text = { Text("Send Feedback") },
                                onClick = {
                                          navHostController.navigate(Screen.SendFeedback.route)
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.Email,
                                        contentDescription = null
                                    )
                                })
                            DropdownMenuItem(
                                text = { Text("Logout") },
                                onClick = {
                                    viewModel_Prof.logout(
                                        onSuccess = {
                                            navHostController.navigate(Screen.LoginScreen.route) {
                                                popUpTo(Screen.LoginScreen.route) {
                                                    inclusive = true
                                                }
                                            }
                                        },
//                                        onFailure = {
//                                            Toast.makeText(
//                                                context,
//                                                "Logout Failed",
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//
//                                        }
                                    )

                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.Close,
                                        contentDescription = null
                                    )
                                })
                        }

                    },
                    scrollBehavior = scrollBehavior
                )
            },
            content = { paddingValues ->
                Surface(color = MaterialTheme.colorScheme.surface) {
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
                            var count = 0
                            Text(
                                text = "Enrolled Students", style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily(Font(R.font.exoregular))
                                ), fontSize = 28.sp,
                                modifier = Modifier.padding(16.dp)
                            )
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                val items1 = viewModel.studentsList.value.sortedBy { it.rollNo }
                                items(items = items1) {
                                    count += 1
                                    ListItem(
                                        headlineContent = {
                                            Text(
                                                text = it.name,
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
                                                Text(text = it.name[0].toString())
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            })
    }
}