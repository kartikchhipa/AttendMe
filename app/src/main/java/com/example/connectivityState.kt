package com.example

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import com.example.attendme.model.ConnectionStates

@Composable
fun connectivityState() : State<ConnectionStates> {
    val context = LocalContext.current
    return produceState(initialValue = context.currentConnectivityState){
        context.observerConnectivityAsFlow().collect{
            value = it
        }
    }
}