package com.example.attendme.model


sealed class ConnectionStates {
    object Connected : ConnectionStates()
    object Disconnected : ConnectionStates()
}
