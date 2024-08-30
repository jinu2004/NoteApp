package org.example.project.data

import org.example.project.util.getRandomColor

data class Note(
    var heading: String = "",
    val date: String = "",
    var note: String = "",
    var category: String = "select one",
    var color: Int = getRandomColor(),
    var id: String = ""
)
