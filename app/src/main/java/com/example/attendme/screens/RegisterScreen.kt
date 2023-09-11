package com.example.attendme.screens

/*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.attendme.R
import com.example.attendme.navigation.Screen
import com.example.attendme.viewModels.RegisterScreenViewModel

@Composable
fun RegisterScreen(navHostController: NavHostController) {
    val backCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // Perform navigation to the NavigateScreen
            navHostController.navigate(Screen.LoginScreen.route)
        }
    }
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    onBackPressedDispatcher?.addCallback(backCallback)
    val context = LocalContext.current
    val viewModel: RegisterScreenViewModel = viewModel()
    var passwordVisible by remember { mutableStateOf(false) }
    val passwordVisible1 by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val image = painterResource(id = R.drawable.register_page_removebg_preview)


    Surface {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.TopCenter
            ) {
                Image(image, "", Modifier.size(220.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f)
                    .clip(
                        RoundedCornerShape(
                            topStart = 40.dp,
                            topEnd = 40.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 0.dp
                        )
                    )
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val loginText = "Sign Up for an account"
                val loginWord = "Sign Up"
                val loginAnnotatedString = buildAnnotatedString {
                    append(loginText)
                    addStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                        start = 0,
                        end = loginText.length
                    )
                    addStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                        ),
                        start = 0,
                        end = loginWord.length
                    )
                }

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 0.dp, bottom = 20.dp),
                    text = loginAnnotatedString,
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.exoregular, FontWeight.Bold))
                )
                Spacer(modifier = Modifier.padding(7.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    OutlinedTextField(
                        value = viewModel.name.value,
                        label = { Text(text = "Name") },
                        onValueChange = {
                            viewModel.name.value = it
                        },
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .fillMaxWidth(0.8f),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Text
                        ),
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(
                                FocusDirection.Down
                            )
                        })
                    )
                    OutlinedTextField(
                        value = viewModel.email.value,
                        label = { Text(text = "Email") },
                        onValueChange = {
                            viewModel.email.value = it
                        },
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .fillMaxWidth(0.8f)
                            .focusRequester(focusRequester = focusRequester),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Email
                        )
                    )
                    /*
                                        ExposedDropdownMenuBox(
                                            modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(0.8f),
                                                    expanded = expanded,
                                            onExpandedChange = {
                                                expanded = !expanded
                                            }
                                        ) {
                                            OutlinedTextField(
                                                readOnly = true,
                                                value = selectedOptionText,
                                                onValueChange = { },
                                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                                colors = ExposedDropdownMenuDefaults.textFieldColors()
                                            )
                                            ExposedDropdownMenu(
                                                expanded = expanded,
                                                onDismissRequest = {
                                                    expanded = false
                                                }
                                            ) {
                                                Department.values().forEach { selectionOption ->
                                                    DropdownMenuItem(
                                                        onClick = {
                                                            viewModel.department.value = selectionOption
                                                            selectedOptionText = selectionOption.name
                                                            expanded = false
                                                        },
                                                        text = {
                                                            Text(text = selectionOption.name)
                                                        },
                                                    )
                                                }
                                            }
                                        }
                    */
                    OutlinedTextField(
                        value = viewModel.password.value,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .fillMaxWidth(0.8f),
                        label = { Text(text = "Password") },
                        placeholder = { Text(text = "Minimum 6 chars") },
                        isError = viewModel.password.value.length < 6,
                        onValueChange = {
                            viewModel.password.value = it
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Password
                        )
                    )
                    OutlinedTextField(
                        value = viewModel.rePassword.value,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .fillMaxWidth(0.8f),
                        label = { Text(text = "Re-Password") },
                        onValueChange = {
                            viewModel.rePassword.value = it
                        },
                        isError = viewModel.password.value != viewModel.rePassword.value,
                        visualTransformation = if (passwordVisible1) VisualTransformation.None else PasswordVisualTransformation(),
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
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Password
                        ),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                    )
                    ElevatedButton(
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(0.8f)
                            .weight(1f, false),
                        enabled = viewModel.email.value.isNotBlank() && viewModel.password.value.length >= 6 && viewModel.password.value == viewModel.rePassword.value,
                        onClick = {
                            viewModel.register(
                                onSuccess = {
                                    if (it == "Teacher") {
                                        navHostController.navigate(Screen.LoginScreen.route) {
                                            popUpTo(Screen.LoginScreen.route) {
                                                inclusive = true
                                            }
                                        }
                                    } else {
                                        navHostController.navigate(Screen.LoginScreen.route) {
                                            popUpTo(Screen.LoginScreen.route) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                    Toast.makeText(
                                        context,
                                        "Verification Email Sent",
                                        Toast.LENGTH_LONG
                                    ).show()
                                },
                            ) {
                                Toast.makeText(
                                    context,
                                    "Error: $it",
                                    Toast.LENGTH_LONG,
                                ).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(Color.Gray)
                    ) {
                        Text(text = "Sign Up", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Login Instead",
                        modifier = Modifier.clickable(onClick = {
                            navHostController.navigate(Screen.LoginScreen.route) {
                                popUpTo(Screen.LoginScreen.route) {
                                    inclusive = true
                                }
                            }
                        })
                    )
                }

            }
        }
    }
}

 */