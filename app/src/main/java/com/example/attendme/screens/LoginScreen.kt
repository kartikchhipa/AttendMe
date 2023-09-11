package com.example.attendme.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.provider.Settings.Secure
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.attendme.viewModels.LoginScreenViewModel
import com.example.attendme.viewModels.RegisterViewModel
import com.example.connectivityState
import kotlinx.coroutines.delay


@SuppressLint("HardwareIds")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    Log.d("CurrentBackStackEntry", navController.currentBackStackEntry.toString())

    val context = LocalContext.current
    var doubleBackToExitPressedOnce by remember { mutableStateOf(false) }

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

    val m_androidId = Secure.getString(
        context.contentResolver, Secure.ANDROID_ID
    )


    // lottie animation for login screen
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_lkww19t7))
    val progress by animateLottieCompositionAsState(
        composition = composition, iterations = LottieConstants.IterateForever
    )

    // checking internet connection
    val connection by connectivityState()
    if (connection == ConnectionStates.Disconnected) {
        NoConnectionScreen()
    } else {
        var loading by remember { mutableStateOf(false) }
        val viewModel: LoginScreenViewModel = viewModel()
        val focusManager = LocalFocusManager.current
        viewModel.androidID.value = m_androidId

        Surface {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.TopCenter
                ) {
                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier.size(260.dp)
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.68f)
                        .clip(
                            RoundedCornerShape(
                                topStart = 40.dp,
                                topEnd = 40.dp,
                                bottomEnd = 0.dp,
                                bottomStart = 0.dp
                            )
                        )
                        .background(MaterialTheme.colorScheme.surface)

                ) {
                    val loginText = "Log in to your account"
                    val loginWord = "Log in"
                    val loginAnnotatedString = buildAnnotatedString {
                        append(loginText)
                        addStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.onSurface,
                            ), start = 0, end = loginText.length
                        )
                        addStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            ), start = 0, end = loginWord.length
                        )
                    }

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp),
                        text = loginAnnotatedString,
                        textAlign = TextAlign.Center,
                        fontSize = 22.sp,
                        fontFamily = FontFamily(Font(R.font.exoregular))
                    )
                    Spacer(modifier = Modifier.padding(15.dp))
                    Column {
                        Text(
                            text = "Email Address",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Left,
                            fontFamily = FontFamily(Font(R.font.exoregular))
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        OutlinedTextField(value = viewModel.email.value,
                            onValueChange = { viewModel.email.value = it },
                            shape = RoundedCornerShape(
                                topEnd = 20.dp,
                                bottomStart = 20.dp,
                                bottomEnd = 20.dp,
                                topStart = 20.dp
                            ),
                            placeholder = {
                                Text(
                                    text = "Email Address",
                                    fontFamily = FontFamily(Font(R.font.exoregular))
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next, keyboardType = KeyboardType.Email
                            ),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.primary
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(0.8f),
                            keyboardActions = KeyboardActions(onNext = {
                                focusManager.moveFocus(
                                    FocusDirection.Down
                                )
                            }),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_mail),
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            })
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "Password",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Left,
                            fontFamily = FontFamily(Font(R.font.exoregular))
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        var passwordHidden by rememberSaveable { mutableStateOf(true) }
                        OutlinedTextField(
                            placeholder = {
                                Text(
                                    text = "Min 6 character",
                                    fontFamily = FontFamily(Font(R.font.exoregular))
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = {
                                    passwordHidden = !passwordHidden
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_visibility_24),
                                        contentDescription = "",
                                        tint = if (passwordHidden) MaterialTheme.colorScheme.primary else Color.Gray,

                                        )
                                }
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
                            visualTransformation = if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.primary
                            ),

                            modifier = Modifier.fillMaxWidth(0.8f),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done, keyboardType = KeyboardType.Password
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    viewModel.login(onSuccess = {
                                        if (it == "Navigate to onboarding") {
                                            navController.navigate(Screen.OnboardingScreen.route)
                                        } else if (it == "Student") {
                                            navController.navigate(Screen.HomeScreen1.route)
                                        } else if (it == "Teacher") {
                                            navController.navigate(Screen.HomeScreen.route)
                                        }
                                    }, onFailure = {
                                        Toast.makeText(
                                            context,
                                            "Error: $it",
                                            Toast.LENGTH_LONG,
                                        ).show()
                                    })
                                },
                            )
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        Box(
                            modifier = Modifier.fillMaxWidth(0.8f),
                            contentAlignment = Alignment.TopEnd
                        ) {

                            TextButton(onClick = {})

                            {
                                Text(
                                    text = "Forgot Password?",
                                    letterSpacing = 0.5.sp,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontFamily = FontFamily(Font(R.font.exoregular))
                                )
                            }
                        }
                        Spacer(modifier = Modifier.padding(12.dp))

                        var signinEnabled by remember { mutableStateOf(false) }
                        signinEnabled =
                            viewModel.email.value.isNotBlank() && viewModel.password.value.length >= 6

                        if (!loading) {
                            ClickOnceButton(onClick = {
                                viewModel.androidID.value = m_androidId;
                                loading = true;
                                viewModel.login(onSuccess = {
                                    when (it) {
                                        "Onboarding" -> {
                                            navController.navigate(Screen.OnboardingScreen.route)
                                        }
                                        "Student" -> {
                                            navController.navigate(Screen.HomeScreen1.route)
                                        }
                                        "Teacher" -> {
                                            navController.navigate(Screen.HomeScreen.route)
                                        }
                                        "Verification" -> {
                                            Toast.makeText(
                                                context,
                                                "Verfication email sent. Please verify to continue",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                }, onFailure = {
                                    loading = false;
                                    Toast.makeText(context, "$it", Toast.LENGTH_LONG).show()
                                })

                            },
                                shape = RoundedCornerShape(
                                    topEnd = 30.dp,
                                    topStart = 30.dp,
                                    bottomEnd = 30.dp,
                                    bottomStart = 30.dp
                                ),
                                enabled = signinEnabled,
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .align(Alignment.CenterHorizontally)
                                    .height(60.dp),
                                elevation = ButtonDefaults.buttonElevation(3.dp),
                                content = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {


                                        Text(
                                            "Sign In",
                                            fontSize = 16.sp,
                                            fontFamily = FontFamily(Font(R.font.exoregular))
                                        )
                                    }
                                })
                        }
                    }
                    if (loading) {
                        Column(
                            modifier = Modifier.height(60.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center

                        ) {
                            LoadingAnimation2()

                        }
                    }



                    Spacer(modifier = Modifier.padding(10.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth(0.8f), contentAlignment = Alignment.Center
                    ) {

                        TextButton(onClick = {
                            navController.navigate(Screen.LoginScreen2.route) {
                                popUpTo(Screen.LoginScreen.route) {
                                    inclusive = false
                                }
                            }

                        }) {
                            Text(
                                text = "Create An Account",
                                letterSpacing = 1.sp,
                                style = MaterialTheme.typography.labelLarge,
                                fontFamily = FontFamily(Font(R.font.exoregular))
                            )
                        }
                    }

                }
            }
        }

    }
}


@Composable
fun ClickOnceButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier,
    conditional: () -> Boolean = { true },
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape,
    border: BorderStroke? = null,
    elevation: ButtonElevation,
    content: @Composable RowScope.() -> Unit
) {
    var onClickHasExecuted by remember { mutableStateOf(false) }

    Button(
        onClick = {
            if (conditional() && !onClickHasExecuted) {
                onClickHasExecuted = true
                onClick()
            }
        },
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        shape = shape,
        border = border,
        content = content
    )
}


@Composable
fun LoadingAnimation2(
    circleColor: Color = MaterialTheme.colorScheme.primary, animationDelay: Int = 1500
) {

    // 3 circles
    val circles = listOf(remember {
        Animatable(initialValue = 0f)
    }, remember {
        Animatable(initialValue = 0f)
    }, remember {
        Animatable(initialValue = 0f)
    })

    circles.forEachIndexed { index, animatable ->
        LaunchedEffect(Unit) {
            delay(timeMillis = (animationDelay / 3L) * (index + 1))

            animatable.animateTo(
                targetValue = 1f, animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = animationDelay, easing = LinearEasing
                    ), repeatMode = RepeatMode.Restart
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
                        color = circleColor.copy(alpha = (1 - animatable.value))
                    )
            )
        }
    }
}
