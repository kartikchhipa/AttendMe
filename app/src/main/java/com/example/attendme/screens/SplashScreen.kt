package com.example.attendme.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import com.example.attendme.R
import com.example.attendme.navigation.Screen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@SuppressLint("HardwareIds")
@Composable
fun SplashScreen(navHostController: NavHostController) {
    Log.d("CurrentBackStackEntry", navHostController.currentBackStackEntry.toString())
    val image = painterResource(id = R.drawable.register_page_removebg_preview)
    val image1 = painterResource(id = R.drawable.logo)
    val image2 = painterResource(id = R.drawable.college_cse)
    var animationStarted by remember { mutableStateOf(false) }
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        animationStarted = true
        CoroutineScope(Dispatchers.Main).launch {
            val db = Firebase.firestore.collection("User Role")
            // val db3 = Firebase.firestore.collection("Sessions")
            if (Firebase.auth.currentUser != null) {
//                if (Firebase.auth.currentUser?.isEmailVerified == false) {
//                    val docs = db3.whereEqualTo("userid", Firebase.auth.uid).get().await().documents
//                    for (doc in docs) {
//                        db3.document(doc.id).delete()
//                    }
//                    Firebase.auth.signOut()
//                    Toast.makeText(context, "Please verify your email first", Toast.LENGTH_SHORT)
//                        .show()
//                    navHostController.navigate(Screen.LoginScreen.route) {
//                        popUpTo(Screen.SplashScreen.route) {
//                            inclusive = true
//                        }
//                    }
//                } else {
                val email = Firebase.auth.currentUser?.email.toString()
                val query = db.whereEqualTo("email", email).get().await()
                if (query.documents.isEmpty()) {
                    navHostController.navigate(Screen.OnboardingScreen.route) {
                        popUpTo(Screen.SplashScreen.route) {
                            inclusive = true
                        }
                    }
                } else {
                    val userRole = query.documents[0].get("role").toString()
                    if (userRole == "Teacher") {
                        navHostController.navigate(Screen.HomeScreen.route) {
                            popUpTo(Screen.SplashScreen.route) {
                                inclusive = true
                            }
                        }
                    } else if (userRole == "Student") {
                        navHostController.navigate(Screen.HomeScreen1.route) {
                            popUpTo(Screen.SplashScreen.route) {
                                inclusive = true
                            }
                        }
                    }
                }
//                }
            } else {
                navHostController.navigate(Screen.LoginScreen.route) {
                    popUpTo(Screen.SplashScreen.route) {
                        inclusive = true
                    }
                }
            }
        }


    }

    val alpha by animateFloatAsState(
        targetValue = if (animationStarted) 1f else 0f,
        animationSpec = tween(3000), label = ""
    )
    val enterOffset by animateDpAsState(
        targetValue = if (animationStarted) 0.dp else 100.dp,
        animationSpec = tween(3000), label = ""
    )

    Surface() {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = image,
                    tint = Color.Unspecified,
                    contentDescription = "",
                    modifier = Modifier
                        .size(250.dp)
                        .alpha(alpha)
                        .offset(y = enterOffset),
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = image1,
                        tint = Color.Unspecified,
                        contentDescription = "",
                        modifier = Modifier
                            .size(100.dp)
                            .alpha(alpha)
                            .offset(y = enterOffset),
                    )
                    Icon(
                        painter = image2,
                        tint = Color.Unspecified,
                        contentDescription = "",
                        modifier = Modifier
                            .size(150.dp)
                            .alpha(alpha)
                            .offset(y = enterOffset),
                    )
                }

                Text(
                    text = "Attend Me",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.alpha(alpha),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                LoadingAnimation4()
            }

        }
    }
}

@Composable
fun LoadingAnimation4(
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
            .size(size = 100.dp)
            .background(color = Color.Transparent)
            .fillMaxWidth()
    ) {

        circles.forEachIndexed { index, animatable ->
            Box(
                modifier = Modifier
                    .scale(scale = animatable.value)
                    .size(size = 100.dp)
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