package com.example.attendme.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Card
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
fun LoginScreen2(navController: NavHostController) {

    val context = LocalContext.current
    val backCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // Perform navigation to the NavigateScreen
            navController.navigate(Screen.LoginScreen.route)
        }
    }
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    onBackPressedDispatcher?.addCallback(backCallback)

    // lottie animation for login screen
    val composition1 by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_lkww19t7))
    val progress1 by animateLottieCompositionAsState(
        composition = composition1, iterations = LottieConstants.IterateForever
    )

    // checking internet connection
    val connection by connectivityState()
    if (connection == ConnectionStates.Disconnected) {
        NoConnectionScreen()
    } else {
        val dialogState = remember { mutableStateOf(false) }
        val viewModel: RegisterViewModel = viewModel()
        var loading by remember { mutableStateOf(false) }
        val focusManager = LocalFocusManager.current

        Surface {
            if (dialogState.value) {


                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AlertDialog(
                        onDismissRequest = {
                            dialogState.value = false
                        },
                        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
                        title = {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(imageVector = Icons.Outlined.Email, contentDescription = "Email", modifier = Modifier.size(50.dp))

                            }

                        },
                        text = {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = "Verification Email Sent Successfully. Please Verify Your Email and Login Again", fontFamily = FontFamily(Font(R.font.exoregular)))
                            }
                        },
                        confirmButton = {
                            Column (
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ){
                                TextButton(
                                    onClick = {
                                        dialogState.value = false
                                        navController.navigate(Screen.LoginScreen.route){
                                            popUpTo(Screen.LoginScreen.route){
                                                inclusive = true
                                            }
                                        }
                                    },
                                ) {
                                    Text(text = "OK", fontFamily = FontFamily(Font(R.font.exoregular)))
                                }

                            }
                        },
                        dismissButton = {
                        }
                    )

                }

            }

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.TopCenter
                ) {
                    LottieAnimation(
                        composition = composition1,
                        progress = { progress1 },
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
                    val loginText = "Sign Up for an account"
                    val loginWord = "Sign Up"
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
                        Spacer(modifier = Modifier.padding(3.dp))
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
                                imeAction = ImeAction.Next, keyboardType = KeyboardType.Password
                            ),
                            keyboardActions = KeyboardActions(onNext = {
                                focusManager.moveFocus(
                                    FocusDirection.Down
                                )
                            }),
                        )

                        Spacer(modifier = Modifier.padding(3.dp))


                        Text(
                            text = "Password",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Left,
                            fontFamily = FontFamily(Font(R.font.exoregular))
                        )

                        Spacer(modifier = Modifier.padding(3.dp))


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
                            value = viewModel.rePassword.value,
                            onValueChange = { viewModel.rePassword.value = it },
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
                            isError = viewModel.password.value !=  viewModel.rePassword.value,
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
                                    viewModel.register(onSuccess = {
                                        if (it == "Verification Email Sent") {
                                            dialogState.value = true
                                            loading = !loading
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

                        Spacer(modifier = Modifier.padding(8.dp))

                        var signinEnabled by remember { mutableStateOf(false) }
                        signinEnabled =
                            viewModel.email.value.isNotBlank() && viewModel.password.value.length >= 6 && viewModel.rePassword.value == viewModel.password.value

                        if (!loading) {
                            ClickOnceButton(onClick = {
                                loading = !loading;
                                viewModel.register(onSuccess = {
                                    if (it == "Verification Email Sent") {
                                        dialogState.value = true
                                    }
                                }, onFailure = {
                                    Toast.makeText(context, "$it", Toast.LENGTH_LONG).show()
                                    loading = !loading;
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
                    }

                }
            }
        }

    }
}
