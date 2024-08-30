package org.example.project.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

val colorViolet = Color(0xFFD4B2FF)
val colorLightGreen = Color(0xFF91FC8F)
val colorLightBlue = Color(0xFFB2FFED)
val colorLightOrang = Color(0xFFFFDCB2)
val colorLightRed = Color(0xFFFFB2C0)
val colorlightYellow = Color(0xFFEBFFB2)


fun getRandomColor(): Int {
    val listOfColors = listOf(
        colorViolet, colorLightRed, colorLightGreen, colorLightBlue,
        colorLightOrang, colorlightYellow
    )

    return listOfColors.random().toArgb()


}