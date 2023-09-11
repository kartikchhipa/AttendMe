package com.example.attendme.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.attendme.R
import com.example.attendme.model.ConnectionStates
import com.example.attendme.navigation.Screen
import com.example.attendme.viewModels.ClassAddScreenViewModel
import com.example.attendme.viewModels.HomeScreenViewModel
import com.example.connectivityState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun ClassAddScreen(navHostController: NavHostController) {

    val backCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            navHostController.popBackStack()
        }
    }
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    onBackPressedDispatcher?.addCallback(backCallback)

    val connection by connectivityState()
    if (connection == ConnectionStates.Disconnected) {
        NoConnectionScreen()
    } else {
        val viewModel_Prof: HomeScreenViewModel = viewModel()
        val viewModel: ClassAddScreenViewModel = viewModel()
        viewModel.profName.value = viewModel_Prof.professor.value.name
        val context = LocalContext.current
        var expanded by remember { mutableStateOf(false) }
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        val focusManager = LocalFocusManager.current
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
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            onClick = {
                                viewModel.createClass(
                                    onSuccess = {
                                        navHostController.navigate(Screen.HomeScreen.route) {
                                            popUpTo(Screen.HomeScreen.route) {
                                                inclusive = true
                                            }
                                        }
                                    },
                                    onFailure = {
                                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }) {
                            Text(text = "Create")
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
                                    viewModel_Prof.logout(
                                        onSuccess = {
                                            navHostController.navigate(Screen.LoginScreen.route) {
                                                popUpTo(Screen.LoginScreen.route) {
                                                    inclusive = true
                                                }
                                            }
                                        },
//                                        onFailure = {
//                                            Toast.makeText(context, it, Toast.LENGTH_SHORT)
//                                                .show()
//                                        }
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
            content = { paddingValues ->
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.padding(paddingValues),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Column(modifier = Modifier.fillMaxWidth(0.8f)) {
                            Text(
                                text = "Create Class",
                                fontFamily = FontFamily(Font(R.font.exoregular)),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = "Enter the details of the class you want to create",
                                fontFamily = FontFamily(Font(R.font.exoregular)),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                        }

                        OutlinedTextField(
                            value = viewModel.className.value,
                            onValueChange = { viewModel.className.value = it },
                            shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
                            placeholder = {
                                Text(
                                    text = "Class Name",
                                    fontFamily = FontFamily(Font(R.font.exoregular)),
                                    fontSize = 12.sp,
                                    maxLines = 1
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Email
                            ),
                            leadingIcon = {
                                Icon(
                                    /*painter = painterResource(id = R.drawable.baseline_text_format_24),*/
                                    painter = painterResource(id = R.drawable.baseline_biotech_24),
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(0.8f),
                            keyboardActions = KeyboardActions(onNext = {
                                focusManager.moveFocus(
                                    FocusDirection.Down
                                )
                            })
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = viewModel.batch.value,
                            onValueChange = { viewModel.batch.value = it },
                            shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
                            placeholder = {
                                Text(
                                    text = "Offered to Batches",
                                    fontFamily = FontFamily(Font(R.font.exoregular)),
                                    maxLines = 1,
                                    fontSize = 12.sp,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Email
                            ),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_text_format_24),
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(0.8f),
                            keyboardActions = KeyboardActions(onNext = {
                                focusManager.moveFocus(
                                    FocusDirection.Down
                                )
                            })
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = viewModel.department.value,
                            onValueChange = { viewModel.department.value = it },
                            shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
                            placeholder = {
                                Text(
                                    text = "Offered for Departments",
                                    fontFamily = FontFamily(Font(R.font.exoregular)),
                                    maxLines = 1,
                                    fontSize = 12.sp
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Email
                            ),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_text_format_24),
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(0.8f),
                            keyboardActions = KeyboardActions(onNext = {
                                focusManager.moveFocus(
                                    FocusDirection.Down
                                )
                            })
                        )
                    }
                }
            }
        )
    }
}