package org.example.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.example.project.application.mainModule
import org.example.project.domain.viewmodel.MainViewModel
import org.example.project.presentation.AddNewNote
import org.example.project.presentation.HomeScreen
import org.example.project.presentation.Screens
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.startKoin

@OptIn(KoinExperimentalAPI::class)
@Composable
@Preview
fun App() {
    startKoin { modules(mainModule) }
    MaterialTheme {
        val navController = rememberNavController()
        KoinContext {
            koinViewModel<MainViewModel>()
            NavHost(
                startDestination = Screens.HomeScreen.route,
                navController = navController, modifier = Modifier.fillMaxSize()
            ) {
                composable(route = Screens.HomeScreen.route) {
                    HomeScreen(navController).View()
                }
                composable(
                    route = Screens.DetailNote.route + "/{noteId}",
                    arguments = listOf(navArgument("noteId") {
                        type = NavType.StringType
                        defaultValue = "empty"
                    })
                ) {
                    AddNewNote(navController).View(noteId = it.arguments?.getString("noteId"))
                }
            }
        }
    }
}





