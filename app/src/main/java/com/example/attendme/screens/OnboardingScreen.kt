package com.example.attendme.screens

import android.annotation.SuppressLint
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.example.attendme.R
import com.example.attendme.model.ConnectionStates
import com.example.attendme.navigation.Screen
import com.example.attendme.viewModels.OnboardingViewModel
import com.example.connectivityState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay

@SuppressLint("HardwareIds")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(navHostController: NavHostController) {
    val context = LocalContext.current
    val viewModel: OnboardingViewModel = viewModel()
    val focusManager = LocalFocusManager.current
    val m_androidID = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    var loading by remember { mutableStateOf(false) }
    val backCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.logout(
                onSuccess = {
                    navHostController.navigate(Screen.LoginScreen.route) {
                        popUpTo(Screen.LoginScreen.route) {
                            inclusive = true
                        }
                    }
                },
                onFailure = {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                })
        }
    }
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    onBackPressedDispatcher?.addCallback(backCallback)
    val connectionState by connectivityState()
    if (connectionState == ConnectionStates.Disconnected) {
        NoConnectionScreen()
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Onboarding",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontFamily = FontFamily(Font(R.font.exoregular)),
                            fontSize = 20.sp,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            viewModel.logout(
                                onSuccess = {
                                    navHostController.navigate(Screen.LoginScreen.route) {
                                        popUpTo(Screen.LoginScreen.route) {
                                            inclusive = true
                                        }
                                    }
                                },
                                onFailure = {
                                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                )
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Registering As ${viewModel.role.value}",
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.exoregular))
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = viewModel.name.value,
                            onValueChange = { viewModel.name.value = it },
                            shape = RoundedCornerShape(
                                topEnd = 20.dp,
                                bottomStart = 20.dp,
                                bottomEnd = 20.dp,
                                topStart = 20.dp
                            ),
                            label = {
                                Text(
                                    text = "Name",
                                    fontFamily = FontFamily(Font(R.font.exoregular))
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(0.8f),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Text
                            ),
                            keyboardActions = KeyboardActions(onNext = {
                                focusManager.moveFocus(
                                    FocusDirection.Down
                                )
                            }),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_person_outline_24),
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                        )
                        viewModel.androidID.value = m_androidID
                        Spacer(modifier = Modifier.padding(8.dp))
                        OutlinedTextField(
                            readOnly = true,
                            value = viewModel.androidID.value,
                            onValueChange = { viewModel.androidID.value = it },
                            shape = RoundedCornerShape(
                                topEnd = 20.dp,
                                bottomStart = 20.dp,
                                bottomEnd = 20.dp,
                                topStart = 20.dp
                            ),
                            label = {
                                Text(
                                    text = "Phone Number",
                                    fontFamily = FontFamily(Font(R.font.exoregular))
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(0.8f),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Text
                            ),
                            keyboardActions = KeyboardActions(onNext = {
                                focusManager.moveFocus(
                                    FocusDirection.Down
                                )
                            }),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_local_phone_24),
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },

                            )
                        Spacer(modifier = Modifier.padding(8.dp))
                        viewModel.email?.let {
                            OutlinedTextField(
                                readOnly = true,
                                value = it.value,
                                onValueChange = { viewModel.email.value = it },
                                shape = RoundedCornerShape(
                                    topEnd = 20.dp,
                                    bottomStart = 20.dp,
                                    bottomEnd = 20.dp,
                                    topStart = 20.dp
                                ),
                                label = {
                                    Text(
                                        text = "Email Address",
                                        fontFamily = FontFamily(Font(R.font.exoregular))
                                    )
                                },

                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Next,
                                    keyboardType = KeyboardType.Email
                                ),
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_mail),
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                                ),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(0.8f),
                                keyboardActions = KeyboardActions(onNext = {
                                    focusManager.moveFocus(
                                        FocusDirection.Down
                                    )
                                })
                            )
                        }
                        Spacer(modifier = Modifier.padding(8.dp))

                        OutlinedTextField(
                            value = viewModel.dep_rollNo.value.uppercase(),
                            onValueChange = { viewModel.dep_rollNo.value = it.uppercase() },
                            shape = RoundedCornerShape(
                                topEnd = 20.dp,
                                bottomStart = 20.dp,
                                bottomEnd = 20.dp,
                                topStart = 20.dp
                            ),
                            label = {
                                Text(
                                    if (viewModel.role.value == "Student") "Roll Number" else "Department",
                                    fontFamily = FontFamily(Font(R.font.exoregular))
                                )
                            },

                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(0.8f),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Text
                            ),
                            keyboardActions = KeyboardActions(onNext = {
                                focusManager.moveFocus(
                                    FocusDirection.Down
                                )
                            }),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_numbers_24),
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {

                                    RadioButton(
                                        selected = (viewModel.role.value == "Teacher"),
                                        onClick = {
                                            viewModel.role.value = "Teacher"
                                        },
                                        enabled = true
                                    )
                                    Text(
                                        text = "Teacher",
                                        fontFamily = FontFamily(Font(R.font.exoregular)),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    RadioButton(
                                        selected = (viewModel.role.value == "Student"),
                                        onClick = {
                                            viewModel.role.value = "Student"
                                        },
                                        enabled = true
                                    )
                                    Text(
                                        text = "Student",
                                        fontFamily = FontFamily(Font(R.font.exoregular)),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                }

                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        if (!loading) {
                            Button(
                                enabled = viewModel.email?.value?.isNotEmpty() == true && viewModel.name.value.isNotEmpty() && viewModel.role.value.isNotEmpty() && viewModel.dep_rollNo.value.isNotEmpty(),
                                modifier = Modifier
                                    .fillMaxWidth(0.6f)
                                    .height(60.dp),

                                onClick = {
                                    loading = !loading
                                    viewModel.register(
                                        onSuccess = {
                                            if (viewModel.role.value == "Teacher") {
                                                navHostController.navigate(Screen.HomeScreen.route) {
                                                    popUpTo(Screen.LoginScreen.route) {
                                                        inclusive = true
                                                    }
                                                }
                                            } else if (viewModel.role.value == "Student") {
                                                navHostController.navigate(Screen.HomeScreen1.route) {
                                                    popUpTo(Screen.LoginScreen.route) {
                                                        inclusive = true
                                                    }
                                                }
                                            }
                                            loading = !loading
                                        },
                                        onFailure = {
                                            if (it == "Account already exists") {
                                                Toast.makeText(
                                                    context,
                                                    "Account already exists",
                                                    Toast.LENGTH_SHORT
                                                ).show();
                                                loading = !loading;
                                                navHostController.navigate(Screen.LoginScreen.route) {
                                                    popUpTo(Screen.LoginScreen.route) {
                                                        inclusive = true
                                                    }
                                                }
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Registration Failed: $it",
                                                    Toast.LENGTH_SHORT
                                                ).show();
                                                loading = !loading
                                            }

                                        }
                                    )
                                },
                                contentPadding = PaddingValues(),
                                shape = RoundedCornerShape(
                                    topEnd = 30.dp,
                                    topStart = 30.dp,
                                    bottomEnd = 30.dp,
                                    bottomStart = 30.dp
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        "Create An Account",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily(Font(R.font.exoregular))
                                    )
                                }
                            }

                        } else {
                            LoadingAnimation3()
                        }
                    }

                }


            })
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun LoadingAnimation3(
    circleColor: Color = MaterialTheme.colorScheme.primary,
    animationDelay: Int = 1500
) {

    // 3 circles
    val circles = listOf(
        remember {
            Animatable(initialValue = 0f)
        },
        remember {
            Animatable(initialValue = 0f)
        },
        remember {
            Animatable(initialValue = 0f)
        }
    )

    circles.forEachIndexed { index, animatable ->
        LaunchedEffect(Unit) {
            delay(timeMillis = (animationDelay / 3L) * (index + 1))

            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = animationDelay,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    Box(
        modifier = Modifier
            .size(size = 60.dp)
            .background(color = Color.Transparent)
            .fillMaxWidth()
    ) {

        circles.forEachIndexed { index, animatable ->
            Box(
                modifier = Modifier
                    .scale(scale = animatable.value)
                    .size(size = 60.dp)
                    .clip(shape = CircleShape)
                    .background(
                        color = circleColor
                            .copy(alpha = (1 - animatable.value))
                    )
            )
        }
    }

}
