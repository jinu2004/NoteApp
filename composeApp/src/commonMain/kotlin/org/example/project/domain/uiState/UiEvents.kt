package org.example.project.domain.uiState

import org.example.project.data.Note

sealed class UiEvents {
    data class FilterAction(val string: String) : UiEvents()
    data class SearchAction(val string: String) : UiEvents()
    data class AddNewNote(val note: Note) : UiEvents()
    data class SelectedItem(val note: Note) : UiEvents()
    data class GetNoteById(val id: String) : UiEvents()
    data class UpdateNote(val note: Note) : UiEvents()
    data class DeleteNote(val id: String) : UiEvents()
}