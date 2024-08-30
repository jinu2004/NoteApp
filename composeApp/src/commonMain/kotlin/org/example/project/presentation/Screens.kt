package org.example.project.presentation

sealed class Screens(val route: String) {
    data object DetailNote : Screens("notes")
    data object HomeScreen : Screens("home")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {
                append("/$it")
            }
        }
    }
}