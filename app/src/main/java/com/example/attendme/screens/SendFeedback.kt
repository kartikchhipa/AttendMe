package com.example.attendme.screens

import android.annotation.SuppressLint
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.attendme.R
import com.example.attendme.model.ConnectionStates
import com.example.connectivityState

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SendFeedback(navHostController: NavHostController) {
    val backCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            navHostController.popBackStack()
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
                            "Send Feedback",
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
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues = paddingValues)

                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        elevation = CardDefaults.cardElevation(3.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Feedback",
                                modifier = Modifier.padding(16.dp),
                                fontSize = 22.sp,
                                fontFamily = FontFamily(Font(R.font.exosemibold)),
                            )
                            Text(
                                text = "Presented by Ubisys Research Lab, IIT-Jodhpur",
                                modifier = Modifier.padding(16.dp),
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.exoregular)),
                            )
                            Text(
                                text = "In case of any queries, please contact us at: ",
                                modifier = Modifier.padding(16.dp),
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.exoregular)),
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.Bottom,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.logo),
                                        contentDescription = "Logo",
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .size(70.dp)

                                    )
                                    Icon(
                                        painter = painterResource(id = R.drawable.college_cse),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .size(120.dp)
                                    )

                                }

                            }

                        }

                    }
                }
            }
        )
    }
}