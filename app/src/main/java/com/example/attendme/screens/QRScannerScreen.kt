package com.example.attendme.screens

import android.os.Build
import android.util.Log
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.attendme.R
import com.example.attendme.model.ConnectionStates
import com.example.attendme.navigation.Screen
import com.example.attendme.viewModels.QRScannerViewModel
import com.example.connectivityState
import com.google.android.gms.common.moduleinstall.InstallStatusListener
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate
import kotlinx.coroutines.Dispatchers
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QRScannerScreen(viewModel: QRScannerViewModel, navHostController: NavHostController) {

    val backCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // Perform navigation to the NavigateScreen
            navHostController.popBackStack()
        }
    }

    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    onBackPressedDispatcher?.addCallback(backCallback)

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val connection by connectivityState()
    if (connection == ConnectionStates.Disconnected) {
        NoConnectionScreen()
    } else {

        val optionalModuleApi = GmsBarcodeScanning.getClient(context)
        val moduleInstallClient =
            ModuleInstall.getClient(context).areModulesAvailable(optionalModuleApi)
                .addOnSuccessListener {
                    if (it.areModulesAvailable()) {
                        Log.d("@@qrVM", "module already installed")
                    } else {
                        val moduleInstallRequest =
                            ModuleInstallRequest.newBuilder().addApi(optionalModuleApi).build()
                        var moduleInstallClient =
                            ModuleInstall.getClient(context).installModules(moduleInstallRequest)
                                .addOnSuccessListener {
                                    Log.d("@@qrVM", "module install success")
                                }.addOnFailureListener {
                                    Log.d("@@qrVM", "module install failed")
                                }
                    }
                }
                .addOnFailureListener {
                    // Handle failure...
                }

        val showText = viewModel.errorMessage.value
        Surface(modifier = Modifier) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                ElevatedCard(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 32.dp)
                        .weight(3f),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Scan QR Code",
                            style = MaterialTheme.typography.titleLarge.copy(
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp
                            )
                        )
                        Text(
                            text = showText,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center)
                        )
                    }
                }
                Image(
                    painter = painterResource(id = R.drawable.qr_code),
                    contentDescription = "qr",
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(0.7f)
                        .weight(6f)
                )
                Button(
                    onClick = {
                        scope.launch(Dispatchers.Main) {
                            viewModel.startScan(
                                onSuccess = {},
                                onFailure = {},
                            )
                        }
                    },
                    modifier = Modifier
                ) {
                    Text(
                        text = "SCAN",
                        modifier = Modifier.widthIn(min = 18.dp),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}
