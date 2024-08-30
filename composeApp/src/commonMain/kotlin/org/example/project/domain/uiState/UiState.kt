package org.example.project.domain.uiState

import org.example.project.data.Note

data class UiState(
    val note: List<Note> = emptyList(),
    val filterData: MutableSet<String> = emptySet<String>().toMutableSet(),
    val itemInDetail:Note? = null
)
