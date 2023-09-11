package com.example

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.attendme.model.ConnectionStates
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

val Context.currentConnectivityState: ConnectionStates
    get() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return getcurrentConnectivityState(connectivityManager)
    }
fun Context.observerConnectivityAsFlow() = callbackFlow {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val callback = NetworkCallback{ state ->
        trySend(state)
    }
    val networkRequest = android.net.NetworkRequest.Builder()
        .addCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()
    connectivityManager.registerNetworkCallback(networkRequest, callback)
    val currentState = getcurrentConnectivityState(connectivityManager)
    trySend(currentState)

    awaitClose{
        connectivityManager.unregisterNetworkCallback(callback)
    }
}

private fun getcurrentConnectivityState(connectivityManager: ConnectivityManager): ConnectionStates {
    val connected = connectivityManager.allNetworks.any{ network ->
        connectivityManager.getNetworkCapabilities(network)?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)?:false
    }
    return if(connected) ConnectionStates.Connected else ConnectionStates.Disconnected
}

fun NetworkCallback(callback: (ConnectionStates)->Unit): ConnectivityManager.NetworkCallback{
    return object: ConnectivityManager.NetworkCallback(){
        override fun onAvailable(network: android.net.Network) {
            callback(ConnectionStates.Connected)
        }

        override fun onLost(network: android.net.Network) {
            callback(ConnectionStates.Disconnected)
        }
    }
}