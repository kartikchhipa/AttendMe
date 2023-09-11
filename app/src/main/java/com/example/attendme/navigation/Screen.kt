package com.example.attendme.navigation

sealed class Screen(val route : String) {
    object LoginScreen : Screen("login")
    object HomeScreen : Screen("screen_home")
    object ClassAddScreen : Screen("class_add_screen")
    object CourseViewScreen : Screen("course_view_screen/{classID}")
    object EnrolledStudentScreen : Screen("enrolled_student/{classID}")
    object SplashScreen : Screen("splash_screen")
    object HomeScreen1 : Screen("home_screen")
    object QRScannerScreen : Screen("qr_scanner_screen")
    object LoginAsScreen : Screen("login_as_screen")
    object ViewAttendance : Screen("view_attendance/{classID}")
    object RegisterPage : Screen("register_page")
    object JoinCourseScreen : Screen("join_course_screen")
    object StudentViewAttendanceScreen : Screen("student_view_attendance_screen/{classID}")

    object LoginScreen2 : Screen("login_screen")
    object OnboardingScreen : Screen("onboarding")
    object SendFeedback : Screen("send_feedback")
}