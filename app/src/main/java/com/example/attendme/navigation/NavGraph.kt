package com.example.attendme.navigation

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel


import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


import androidx.navigation.navArgument
import com.example.attendme.model.StudentModel1
import com.example.attendme.screens.ClassAddScreen
import com.example.attendme.screens.CourseViewScreen
import com.example.attendme.screens.EnrolledStudentsScreen
import com.example.attendme.screens.HomeScreen
import com.example.attendme.screens.HomeScreen1
import com.example.attendme.screens.JoinCourseScreen
import com.example.attendme.screens.LoginScreen
import com.example.attendme.screens.LoginScreen2
import com.example.attendme.screens.OnboardingScreen
import com.example.attendme.screens.QRScannerScreen
import com.example.attendme.screens.SendFeedback
//import com.example.attendme.screens.RegisterPage
import com.example.attendme.screens.SplashScreen
import com.example.attendme.screens.StudentViewAttendanceScreen
import com.example.attendme.screens.ViewAttendance
import com.example.attendme.viewModels.CourseViewModel
import com.example.attendme.viewModels.QRScannerViewModel
import com.example.attendme.viewModels.StudentListViewModel
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetUpNavGraph(
    navController: NavHostController,
    context: Context,
    lifecycleOwner: LifecycleOwner,
    path: String
) {
    NavHost(
        navController = navController, startDestination = Screen.SplashScreen.route

    ) {

        composable(route = Screen.OnboardingScreen.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(350, easing = LinearEasing)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(350, easing = LinearEasing)
                )
            },
            ){
            OnboardingScreen(navHostController = navController)
        }
        composable(route = Screen.SendFeedback.route){
            SendFeedback(navHostController = navController)
        }

        composable(route = Screen.LoginScreen2.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(350, easing = LinearEasing)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(350, easing = LinearEasing)
                )
            },

        ){
            LoginScreen2(navController = navController)
        }
        composable(route = Screen.SplashScreen.route,

            ) {
            SplashScreen(navHostController = navController)
        }
        composable(
            route = Screen.LoginScreen.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(350, easing = LinearEasing)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(350, easing = LinearEasing)
                )
            },

            ) {
            LoginScreen(navController)
        }
        composable(route = Screen.HomeScreen.route,

            ) {
            HomeScreen(navController)
        }
        composable(route = Screen.HomeScreen1.route,

            ) {
            HomeScreen1(navController)
        }
        composable(route = Screen.ClassAddScreen.route) {
            ClassAddScreen(navController)
        }
        composable(
            route = Screen.CourseViewScreen.route,
            arguments = listOf(navArgument("classID") {
                this.type = NavType.StringType
                this.nullable = false
            })
        ) {
            val classID = it.arguments!!.getString("classID")
            val viewModel: CourseViewModel =
                viewModel(initializer = { CourseViewModel(classID!!, path) })
            CourseViewScreen(viewModel, navController, path)
        }
        composable(route = Screen.ViewAttendance.route, arguments = listOf(navArgument("classID") {
            this.type = NavType.StringType
            this.nullable = false
        })) {
            val classID = it.arguments!!.getString("classID")
            val viewModel: CourseViewModel =
                viewModel(initializer = { CourseViewModel(classID!!, path) })
            ViewAttendance(viewModel, navController, path)
        }
        composable(route = Screen.QRScannerScreen.route,

            ) {
            val student =
                navController.previousBackStackEntry?.savedStateHandle?.get<StudentModel1>("studentModel")
            val qrViewModel: QRScannerViewModel =
                viewModel(initializer = { QRScannerViewModel(student!!, context) })
            QRScannerScreen(qrViewModel, navController)
        }
        composable(route = Screen.JoinCourseScreen.route,

            ) {
            JoinCourseScreen(navHostController = navController)
        }
        composable(
            route = Screen.EnrolledStudentScreen.route,
            arguments = listOf(navArgument(name = "classID") {
                type = NavType.StringType
                nullable = false
            })
        ) {
            Log.d("@@NavGraph", "enrolled screen executed again")
            val classID = it.arguments!!.getString("classID")!!
            val viewModel: StudentListViewModel =
                viewModel(initializer = { StudentListViewModel(classID) })
            EnrolledStudentsScreen(viewModel = viewModel, navHostController = navController)
        }

        composable(route = Screen.StudentViewAttendanceScreen.route, arguments = listOf(navArgument("classID") {
            this.type = NavType.StringType
            this.nullable = false
        })) {
            val classID = it.arguments!!.getString("classID")
            val viewModel: CourseViewModel =
                viewModel(initializer = { CourseViewModel(classID!!, path) })
            StudentViewAttendanceScreen(viewModel, navController, path)
        }
    }
}