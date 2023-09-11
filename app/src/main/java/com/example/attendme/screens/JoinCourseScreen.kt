package com.example.attendme.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.attendme.R
import com.example.attendme.model.ConnectionStates
import com.example.attendme.model.Department
import com.example.attendme.navigation.Screen
import com.example.attendme.viewModels.ClassAddScreenViewModel
import com.example.attendme.viewModels.HomeScreenViewModel
import com.example.attendme.viewModels.HomeScreenViewModel1
import com.example.connectivityState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinCourseScreen(navHostController: NavHostController) {

    val context = LocalContext.current
    val backCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // Perform navigation to the NavigateScreen
            navHostController.navigate(Screen.HomeScreen1.route) {
                popUpTo(Screen.HomeScreen1.route) {
                    inclusive = true
                }
            }
        }
    }
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    onBackPressedDispatcher?.addCallback(backCallback)

    val connection by connectivityState()
    if (connection == ConnectionStates.Disconnected) {
        NoConnectionScreen()
    } else {


        val viewModel: HomeScreenViewModel1 = viewModel()
        var classID by remember { mutableStateOf("") }
        var expanded by remember { mutableStateOf(false) }
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        val focusManager = LocalFocusManager.current
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Join Class",
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
                    actions = {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            onClick = {
                                viewModel.classId.value = classID
                                viewModel.enrollInClass(
                                    onSuccess = {
                                        viewModel.classId.value = ""
                                        viewModel.getAllEnrolledClasses()
                                        navHostController.navigate(Screen.HomeScreen1.route) {
                                            popUpTo(Screen.HomeScreen1.route) {
                                                inclusive = true
                                            }
                                        }
                                    },
                                    onFailure = {
                                        viewModel.getAllEnrolledClasses()
                                        Toast.makeText(
                                            context,
                                            "Some error: $it",
                                            Toast.LENGTH_LONG,
                                        ).show()
                                    },
                                )

                            },
                            enabled = classID.length == 6
                        ) {
                            Text(text = "Join", fontFamily = FontFamily(Font(R.font.exoregular)))
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
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            },
            content = { paddingValues ->
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize(),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 10.dp),
                        ) {
                            Text(
                                text = "You're currently signed in as",
                                fontFamily = FontFamily(Font(R.font.exosemibold))
                            )
                        }
                        ListItem(
                            headlineContent = {
                                Text(
                                    viewModel.student.value.name,
                                    fontFamily = FontFamily(Font(R.font.exoregular)),
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            supportingContent = {
                                Text(
                                    viewModel.student.value.email,
                                    fontFamily = FontFamily(Font(R.font.exoregular))
                                )
                            },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Rounded.AccountCircle,
                                    contentDescription = "Localized description",
                                    modifier = Modifier.size(48.dp)
                                )
                            },
                            modifier = Modifier.padding(0.dp)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 26.dp, end = 26.dp, top = 7.dp),
                        ) {
                            Divider(thickness = 0.8.dp)

                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp, end = 24.dp, start = 24.dp)
                        ) {
                            Text(
                                text = "Ask your teacher for the class code, \nthen enter it here",
                                fontFamily = FontFamily(Font(R.font.exoregular)),
                                fontSize = 13.sp,
                                lineHeight = 13.sp
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            OutlinedTextField(
                                value = classID,
                                onValueChange = { classID = it },
                                label = {
                                    Text(
                                        text = "Class Code",
                                        fontFamily = FontFamily(Font(R.font.exoregular)),
                                        fontSize = 12.sp,
                                        maxLines = 1
                                    )
                                },
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Next,
                                    keyboardType = KeyboardType.Email
                                ),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                keyboardActions = KeyboardActions(onNext = {
                                    focusManager.moveFocus(
                                        FocusDirection.Down
                                    )
                                })
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = "To sign in with a class code",
                                fontFamily = FontFamily(Font(R.font.exoregular)),
                                fontSize = 15.sp,
                                lineHeight = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )



                            ListItem(headlineContent = {
                                Text(
                                    "Use an authorised account",
                                    fontFamily = FontFamily(Font(R.font.exoregular)),
                                    fontSize = 13.sp,
                                    lineHeight = 13.sp
                                )
                            })
                            ListItem(headlineContent = {
                                Text(
                                    "Use a class code, with 6-7 letters and numbers, and no spaces or symbols",
                                    fontFamily = FontFamily(Font(R.font.exoregular)),
                                    fontSize = 13.sp,
                                    lineHeight = 13.sp
                                )
                            })
                            ListItem(headlineContent = {
                                Text(
                                    "If you are having trouble joining the class, go to the report it on Feedback Section",
                                    fontFamily = FontFamily(Font(R.font.exoregular)),
                                    fontSize = 13.sp,
                                    lineHeight = 13.sp
                                )
                            })

                        }

                    }
                }


            }
        )
    }
}