package com.example.attendme

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.attendme.model.ConnectionStates
import com.example.attendme.navigation.SetUpNavGraph
import com.example.attendme.ui.theme.AttendmeTheme
import com.example.attendme.viewModels.HomeScreenViewModel
import com.example.connectivityState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : ComponentActivity() {

    val viewModel = HomeScreenViewModel()
    @OptIn(ExperimentalPermissionsApi::class, ExperimentalAnimationApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AttendmeTheme {

                /*
                val permissionsState = rememberMultiplePermissionsState(
                    permissions = listOf(
                        Manifest.permission.READ_SMS,
                        Manifest.permission.READ_PHONE_NUMBERS,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE

                    )
                )

                 */


                val lifecycleOwner = LocalLifecycleOwner.current
                /*
                DisposableEffect(
                    key1 = lifecycleOwner,
                    effect = {
                        val observer = LifecycleEventObserver { _, event ->
                            if (event == Lifecycle.Event.ON_START) {
                                permissionsState.launchMultiplePermissionRequest()
                            }
                        }
                        lifecycleOwner.lifecycle.addObserver(observer)

                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(observer)
                        }
                    }
                )
                var count = 0
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 35.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    var read_sms by remember { mutableStateOf(false) }
                    var read_phone_numbers by remember { mutableStateOf(false) }
                    var read_phone_state by remember { mutableStateOf(false) }
                    var read_sms_perm by remember { mutableStateOf(false) }
                    var read_phone_numbers_perm by remember { mutableStateOf(false) }
                    var read_phone_state_perm by remember { mutableStateOf(false) }

                    permissionsState.permissions.forEach { perm ->
                        when (perm.permission) {
                            Manifest.permission.READ_SMS -> {
                                when {
                                    perm.shouldShowRationale -> {
                                        read_sms = true
                                    }

                                    perm.isPermanentlyDenied() -> {
                                        count += 1
                                        read_sms_perm = true
                                    }
                                }
                            }

                            Manifest.permission.READ_PHONE_NUMBERS -> {
                                when {
                                    perm.shouldShowRationale -> {
                                        read_phone_numbers = true
                                    }

                                    perm.isPermanentlyDenied() -> {
                                        count += 1
                                        read_phone_numbers_perm = true
                                    }
                                }
                            }

                            Manifest.permission.READ_PHONE_STATE -> {
                                when {
                                    perm.shouldShowRationale -> {
                                        read_phone_state = true
                                    }

                                    perm.isPermanentlyDenied() -> {
                                        count += 1
                                        read_phone_state_perm = true
                                    }
                                }
                            }
                            Manifest.permission.READ_EXTERNAL_STORAGE -> {
                                when {
                                    perm.shouldShowRationale -> {
                                        read_phone_state = true
                                    }

                                    perm.isPermanentlyDenied() -> {
                                        count += 1
                                        read_phone_state_perm = true
                                    }
                                }
                            }
                        }
                    }
                    if (count >= 1) {
                        Text(
                            text = "One or More Settings Permanently Declined. Please enable the permissions from settings",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Button(onClick = {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            startActivity(intent)
                        }) {2
                            Text(text = "Open App Settings")
                        }
                    } else {
                        if (read_sms || read_phone_numbers || read_phone_state) {
                            Text(
                                text = "Please allow the all the permissions to continue",
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            Button(onClick = { permissionsState.launchMultiplePermissionRequest() }) {
                                Text(text = "Allow")
                            }
                        }
                    }

                }

 */

                    val navController = rememberNavController()
                    val context = LocalContext.current
                    val path = externalCacheDir?.absolutePath + "/attendance"
                    SetUpNavGraph(
                        navController = navController,
                        context = context,
                        lifecycleOwner,
                        path
                    )


            }
        }

    }
}
