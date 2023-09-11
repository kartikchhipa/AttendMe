package com.example.attendme.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import com.example.attendme.BuildConfig
import com.example.attendme.R
import com.example.attendme.model.ConnectionStates
import com.example.attendme.navigation.Screen
import com.example.attendme.viewModels.CourseViewModel
import com.example.attendme.viewModels.HomeScreenViewModel
import com.example.connectivityState
import java.io.File
import java.net.URLConnection


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "IntentReset", "QueryPermissionsNeeded")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseViewScreen(
    viewModel: CourseViewModel,
    navHostController: NavHostController,
    path: String
) {
    val backCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            navHostController.popBackStack()
        }
    }
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    onBackPressedDispatcher?.addCallback(backCallback)
    val context = LocalContext.current
    val connection by connectivityState()
    if (connection == ConnectionStates.Disconnected) {
        NoConnectionScreen()
    } else {
        val viewModel_Prof = HomeScreenViewModel()
        var expanded by remember { mutableStateOf(false) }
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
                            navHostController.popBackStack()
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
                            .fillMaxSize()
                            .padding(paddingValues)
                            .verticalScroll(rememberScrollState())
                    ) {
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {

                                Text(
                                    text = "Course Details", style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily(Font(R.font.exoregular))
                                    ), fontSize = 28.sp,
                                    modifier = Modifier.padding(16.dp)
                                )


                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()

                            ) {
                                ListItem(
                                    headlineContent = {
                                        Text(
                                            text = "Course Title",
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily(Font(R.font.exoregular))
                                            )
                                        )
                                    },
                                    supportingContent = {
                                        Text(
                                            text = viewModel.currClass.value.className,
                                            fontFamily = FontFamily(Font(R.font.exoregular)),
                                        )
                                    },
                                )
                                ListItem(
                                    headlineContent = {
                                        Text(
                                            text = "Joining Code",
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily(Font(R.font.exoregular))
                                            )
                                        )
                                    },
                                    supportingContent = {
                                        Text(
                                            text = viewModel.classID,
                                            fontFamily = FontFamily(Font(R.font.exoregular)),
                                        )
                                    },
                                )
                                ListItem(
                                    headlineContent = {
                                        Text(
                                            text = "Offered to Batches",
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily(Font(R.font.exoregular))
                                            )
                                        )
                                    },
                                    supportingContent = {
                                        Text(
                                            text = viewModel.currClass.value.batch,
                                            fontFamily = FontFamily(Font(R.font.exoregular)),
                                        )
                                    },
                                )
                                ListItem(
                                    headlineContent = {
                                        Text(
                                            text = "Offered for Departments",
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily(Font(R.font.exoregular))
                                            )
                                        )
                                    },
                                    supportingContent = {
                                        Text(
                                            text = viewModel.currClass.value.department,
                                            fontFamily = FontFamily(Font(R.font.exoregular)),
                                        )
                                    },
                                )
                                ListItem(
                                    headlineContent = {
                                        Text(
                                            text = "Enrolled Students",
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily(Font(R.font.exoregular))
                                            )
                                        )
                                    },
                                    supportingContent = {
                                        Text(
                                            text = viewModel.currClass.value.noOfStudents.toString(),
                                            fontFamily = FontFamily(Font(R.font.exoregular)),
                                        )
                                    },
                                    trailingContent = {
                                        IconButton(onClick = {
                                            navHostController.navigate("enrolled_student/${viewModel.classID}")
                                        }) {
                                            Icon(
                                                imageVector = Icons.Rounded.Info,
                                                contentDescription = null
                                            )
                                        }
                                    }
                                )
                            }
                        }
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = viewModel.otpValue.value,
                                style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Black)
                            )
                            Text(
                                text = "Enter the digits in the website\n to start generating QR\n" +
                                        "Do not share!!\nAlso Stop the QR Generator\nfrom website once done",
                                style = TextStyle(fontStyle = FontStyle.Italic),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 10.dp)
                            )

                        }

                    }
                }

            },
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    actions = {

                        IconButton(
                            onClick = {
                                viewModel.downloadCsv(
                                    context = context
                                )
                            },
                            modifier = Modifier.padding(start = 20.dp, end = 14.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.cloud_download_fill0_wght400_grad0_opsz48),
                                contentDescription = "Localized description"
                            )
                        }
                        IconButton(onClick = {

                            try{
                                navHostController.navigate("view_attendance/${viewModel.classID}")
                            }
                            catch (e:Exception){
                                Toast.makeText(context,"Some error: ${e.message}",Toast.LENGTH_LONG).show()
                            }
                            }) {
                            Icon(
                                painter = painterResource(id = R.drawable.data_loss_prevention_fill0_wght400_grad0_opsz48),
                                contentDescription = "Localized description",
                            )
                        }
                    },
                    floatingActionButton = {
                        if (!viewModel.isAttendance.value) {
                            ExtendedFloatingActionButton(
                                onClick = {
                                    if (viewModel.otpValue.value == "******") {
                                        viewModel.addOtpAndClassID()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Some error: OTP value already generated",
                                            Toast.LENGTH_LONG,
                                        ).show()
                                    }
                                },
                                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(
                                    defaultElevation = 3.dp
                                )
                            ) {
                                Text("Start Attendance ")
                                Icon(
                                    painter = painterResource(id = R.drawable.not_started_fill0_wght400_grad0_opsz48),
                                    "Localized description",
                                    Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                )
            }
        )
    }
}
