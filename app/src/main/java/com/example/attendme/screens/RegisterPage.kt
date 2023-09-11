package com.example.attendme.screens
/*
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.attendme.R
import com.example.attendme.model.ConnectionStates
import com.example.attendme.navigation.Screen
import com.example.attendme.viewModels.RegisterScreenViewModel
import com.example.connectivityState
import kotlinx.coroutines.delay


@SuppressLint("HardwareIds")
@Composable
fun RegisterPage(navHostController: NavHostController) {
    Log.d("CurrentBackStackEntry", navHostController.currentBackStack.toString())

    val viewModel: RegisterScreenViewModel = viewModel()

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_lkwyrd8l))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    val backCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            Log.d("RegisterPage", "Back button clicked")
            // pop back stack
            navHostController.popBackStack()
        }
    }
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    onBackPressedDispatcher?.addCallback(backCallback)


    val context = LocalContext.current
    /*
    val phoneNumber: MutableState<String> = remember { mutableStateOf("No SIM Card") }
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        val telephonyManager =
            context.getSystemService(android.content.Context.TELEPHONY_SERVICE) as TelephonyManager
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_NUMBERS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        if (telephonyManager.simState == TelephonyManager.SIM_STATE_READY)
            phoneNumber.value = telephonyManager.line1Number
    } else {
        val subscriptionManager =
            context.getSystemService(android.content.Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
        // check if the device has a sim card
        if (subscriptionManager.activeSubscriptionInfoCount > 0) {
            val subscriptionId =
                subscriptionManager.activeSubscriptionInfoList[0].subscriptionId
            phoneNumber.value = subscriptionManager.getPhoneNumber(subscriptionId)
        }
    }

     */
    val m_androidID = Settings.Secure.getString(
        LocalContext.current.contentResolver,
        Settings.Secure.ANDROID_ID
    )

    val connection by connectivityState()
    if (connection == ConnectionStates.Disconnected) {
        NoConnectionScreen()
    } else {




        Log.d("Role", viewModel.role.value)

        var passwordVisible by remember { mutableStateOf(false) }
        val focusManager = LocalFocusManager.current
        var loading by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(
                    color = Color.Transparent,
                )
        ) {


            Box(
                modifier = Modifier
                    .fillMaxHeight(),
            ) {


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier
                            .scale(0.5f)
                            .padding(top = 10.dp),
                    )
                    Text(
                        text = "Registering as ${viewModel.role.value}",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 0.dp)
                            .fillMaxWidth(),
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = FontFamily(Font(R.font.exoregular))
                    )
                    Spacer(modifier = Modifier.height(15.dp))
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
                    Spacer(modifier = Modifier.padding(6.dp))
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
                    Spacer(modifier = Modifier.padding(6.dp))
                    OutlinedTextField(
                        value = viewModel.email.value,
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
                    Spacer(modifier = Modifier.padding(6.dp))

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
                    Spacer(modifier = Modifier.padding(6.dp))
                    OutlinedTextField(
                        label = {
                            Text(
                                text = "Password",
                                fontFamily = FontFamily(Font(R.font.exoregular))
                            )
                        },
                        placeholder = {

                            Text(
                                text = "Min 6 character",
                                fontFamily = FontFamily(Font(R.font.exoregular))
                            )
                        },
                        value = viewModel.password.value,
                        onValueChange = { viewModel.password.value = it },
                        shape = RoundedCornerShape(
                            topEnd = 20.dp,
                            bottomStart = 20.dp,
                            bottomEnd = 20.dp,
                            topStart = 20.dp
                        ),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_password),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                passwordVisible = !passwordVisible
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_visibility_24),
                                    contentDescription = "",
                                    tint = if (passwordVisible) MaterialTheme.colorScheme.primary else Color.Gray,
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                        ),

                        modifier = Modifier.fillMaxWidth(0.8f),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Password
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(
                                    FocusDirection.Down
                                )
                            },
                        )
                    )
                    Spacer(modifier = Modifier.padding(6.dp))
                    OutlinedTextField(
                        label = {
                            Text(
                                text = "Confirm Password",
                                fontFamily = FontFamily(Font(R.font.exoregular)),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        value = viewModel.rePassword.value,
                        onValueChange = { viewModel.rePassword.value = it },
                        isError = viewModel.password.value != viewModel.rePassword.value,
                        shape = RoundedCornerShape(
                            topEnd = 20.dp,
                            bottomStart = 20.dp,
                            bottomEnd = 20.dp,
                            topStart = 20.dp
                        ),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_password),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        singleLine = true,
                        trailingIcon = {
                            IconButton(onClick = {
                                passwordVisible = !passwordVisible
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_visibility_24),
                                    contentDescription = "",
                                    tint = if (passwordVisible) MaterialTheme.colorScheme.primary else Color.Gray,
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                        ),

                        modifier = Modifier.fillMaxWidth(0.8f),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Password
                        ),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                    )

                    Spacer(modifier = Modifier.padding(6.dp))
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
                    val gradientColor = listOf(Color(0xFF484BF1), Color(0xFF673AB7))
                    val cornerRadius = 16.dp

                    Spacer(modifier = Modifier.padding(6.dp))

                    val roundedCornerShape = RoundedCornerShape(topStart = 30.dp, bottomEnd = 30.dp)
                    if (!loading) {
                        Button(
                            enabled = viewModel.email.value.isNotEmpty() && viewModel.password.value.isNotEmpty() && viewModel.rePassword.value.isNotEmpty() && viewModel.name.value.isNotEmpty() && viewModel.role.value.isNotEmpty() && viewModel.dep_rollNo.value.isNotEmpty(),
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .height(60.dp),

                            onClick = {
                                loading = !loading
                                viewModel.register(
                                    onSuccess = {
                                        Toast.makeText(
                                            context,
                                            "Successfully Registered Please Verify Your Email",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        navHostController.navigate("login")
                                        loading = !loading
                                    },
                                    onFailure = {
                                        Toast.makeText(
                                            context,
                                            "Registration Failed: $it",
                                            Toast.LENGTH_SHORT
                                        ).show();
                                        loading = !loading
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
                    Spacer(modifier = Modifier.padding(3.dp))
                    androidx.compose.material3.TextButton(onClick = {
                        navHostController.navigate(Screen.LoginScreen.route) {
                            popUpTo(Screen.LoginScreen.route) {
                                inclusive = true
                            }
                        }

                    }) {
                        Text(
                            text = "Sign In",
                            letterSpacing = 1.sp,
                            style = MaterialTheme.typography.labelLarge,
                            fontFamily = FontFamily(Font(R.font.exoregular)),
                        )
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                }
            }
        }
    }
}

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
            ) {
            }
        }
    }
}

 */