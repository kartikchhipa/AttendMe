package com.example.attendme.viewModels

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.attendme.model.DateAndTimeModel
import com.example.attendme.model.AttendanceModel1
import com.example.attendme.model.StudentModel1
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class QRScannerViewModel @Inject constructor(
    private val studentModel: StudentModel1,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val classId = mutableStateOf("")
    private val date = mutableStateOf("")

    //0 -> no error
    //1 -> invalid qr
    //2 -> expired qr
    //3 -> null/empty QR
    //5 -> network failure at firebase
    //6 -> not register for the class
    //10 -> attendance marked success
    private val errorCode = mutableStateOf(0)
    val errorMessage: MutableState<String>
        get() {
            return when (errorCode.value) {
                0 -> mutableStateOf("Scan the QR to mark attendance")
                1 -> mutableStateOf("Scanned QR is invalid")
                2 -> mutableStateOf("Scanned QR is expired")
                3 -> mutableStateOf("Scanned QR is null/empty")
                5 -> mutableStateOf("Network failure. Check internet and scan again")
                6 -> mutableStateOf("Your are not registered for this class. You cannot mark your attendance")
                10 -> mutableStateOf("Your attendance is marked for today's ${classId.value}")

                else -> mutableStateOf("unkown error code ${errorCode.value} class")
            }
        }

    private val generationTime = mutableStateOf("")
    private val expirationTime = mutableStateOf("")
    private val classDb = Firebase.firestore.collection("Classes")
    private val auth = FirebaseAuth.getInstance()

    private val options = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE
        )
        .build()
    private val scanner = GmsBarcodeScanning.getClient(context, options)
    private val barCodeResults = MutableStateFlow<String?>(null)

    suspend fun startScan(onSuccess: () -> Unit, onFailure: () -> Unit) {
        try {
            val result = scanner.startScan().await()
            barCodeResults.value = result.rawValue
            Log.d("@@qrVM", barCodeResults.value ?: "no value")
        } catch (e: Exception) {

            Log.d("@@qrVM", "scan error: $e")
            e.printStackTrace()
        }
        if (barCodeResults.value.isNullOrEmpty()) {
            Log.d("@abc", barCodeResults.value ?: "no value")
            errorCode.value = 3
            onFailure()
        } else
            dataFromQR(barCodeResults.value!!, onSuccess, onFailure)
    }

    private fun dataFromQR(qrCodeHex: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        val qrCodeASCII: String
        try {
            qrCodeASCII = hexToASCII(qrCodeHex)
            if (parseQRCode(qrCodeASCII)) {
                addAttendance(onSuccess)
            } else {
                onFailure()
            }
        } catch (e: Exception) {
            errorCode.value = 1
            e.printStackTrace()
        }
    }

    private fun parseQRCode(qrCode: String): Boolean {
        if (qrCode.length != 44) {
            errorCode.value = 1
            return false
        }
        date.value = qrCode.substring(5, 15)
        classId.value = qrCode.substring(15, 21)
        generationTime.value = qrCode.substring(21, 29)
        expirationTime.value = qrCode.substring(31, 39)

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val currentDate = LocalDate.now().format(formatter)

        if (date.value != currentDate) {
            errorCode.value = 2
            return false
        }

        val currentTime = LocalTime.now()
        val startTime = LocalTime.parse(generationTime.value)
        val endTime = LocalTime.parse(expirationTime.value)
        if (startTime == null || endTime == null) {
            errorCode.value = 1
            return false
        }

        if (classId.value !in studentModel.classes) {
            errorCode.value = 6
            return false
        }

        Log.d("@Qr", "startTime $startTime , endTime $endTime")
        Log.d("@Qr", "class id ${classId.value} , date $currentDate")

        // check the time difference between current time and end time
        val diff = Duration.between(currentTime, endTime)


//        return if (currentTime.isAfter(startTime) && currentTime.isBefore(endTime))
//            true
        return if (diff.seconds <= 6) {
            true
        } else {
            errorCode.value = 2
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addAttendance(onSuccess: () -> Unit) = CoroutineScope(Dispatchers.IO).launch {
        if (classId.value.isNotEmpty()) {
            val classQuery = classDb.whereEqualTo("classId", classId.value).get().await()
            if (classQuery.documents.isNotEmpty()) {
                val doc = classQuery.documents[0]
                val attendanceDb =
                    Firebase.firestore.collection("Classes/${doc.id}/Attendance")
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val current = LocalDate.now().format(formatter)
                val currStudent = AttendanceModel1(
                    auth.uid!!,
                    studentModel.name,
                    LocalDateTime.now().format(formatter2),
                    studentModel.rollNo
                )
                val attendanceQuery =
                    attendanceDb.whereEqualTo("date", current).get().await()
                if (attendanceQuery.documents.isNotEmpty()) {
                    val docs = attendanceQuery.documents[0]
                    // for (docs in attendanceQuery) {
                    attendanceDb.document(docs.id)
                        .update("studentList", FieldValue.arrayUnion(currStudent))
                        .addOnSuccessListener {
                            errorCode.value = 10
                            onSuccess()
                        }.addOnFailureListener {
                            errorCode.value = 5
                            Log.d("@@Attendance", it.message.toString())
                        }
                    // }
                } else {
                    val studentList = mutableListOf<AttendanceModel1>()
                    studentList.add(currStudent)
                    val dateAndTime = DateAndTimeModel(current, studentList)
                    attendanceDb.add(dateAndTime).addOnSuccessListener {
                        Log.d("@@attendance", "marked")
                        errorCode.value = 10
                        onSuccess()
                    }
                }
            }
        }
    }

    private fun hexToASCII(hexValue: String): String {
        val output = StringBuilder("")
        var i = 0
        while (i < hexValue.length) {
            val str = hexValue.substring(i, i + 2)
            output.append(str.toInt(16).toChar())
            i += 2
        }
        return output.toString()
    }

}