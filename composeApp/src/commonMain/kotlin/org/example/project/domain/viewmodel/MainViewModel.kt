package org.example.project.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.data.Note
import org.example.project.data.NoteDatabase
import org.example.project.domain.database.DatabaseService
import org.example.project.domain.uiState.UiEvents
import org.example.project.domain.uiState.UiState

class MainViewModel(private val databaseSource: DatabaseService) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val state = _uiState.asStateFlow()
    private var allNote = arrayListOf<Note>()
    private var category = mutableSetOf<String>()


    init {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { uiState ->
                databaseSource.getAllNote().forEach {
                    allNote.add(
                        Note(
                            heading = it.heading,
                            date = it.date,
                            note = it.note,
                            category = it.category,
                            color = it.color,
                            id = it._id
                        )
                    )
                    category.add(it.category)
                }


                uiState.copy(note = allNote, filterData = category)
            }
        }
    }


    fun onEvent(events: UiEvents) {
        when (events) {
            is UiEvents.FilterAction -> {
                viewModelScope.launch {
                    if (events.string == "All") {
                        _uiState.update {
                            it.copy(note = allNote)
                        }
                    } else {
                        val filteredResult = allNote.filter { it.category == events.string }
                        _uiState.update {
                            it.copy(note = filteredResult)
                        }
                    }
                }


            }

            is UiEvents.SearchAction -> {
                viewModelScope.launch {
                    val searchList = allNote
                    if (events.string.isNotBlank()) {
                        searchList.filter {
                            it.heading.contains(events.string, ignoreCase = true)
                                    || it.note.contains(events.string, ignoreCase = true)
                        }
                        _uiState.update {
                            it.copy(note = searchList)
                        }
                    } else {
                        updateAll()
                    }
                }


            }

            is UiEvents.AddNewNote -> {
                viewModelScope.launch {
                    val date = events.note
                    val newNote = NoteDatabase().apply {
                        note = date.note
                        heading = date.heading
                        category = date.category
                        color = date.color
                    }
                    databaseSource.addNewNote(newNote)
                    updateAll()
                }

            }

            is UiEvents.SelectedItem -> TODO()
            is UiEvents.GetNoteById -> {
                viewModelScope.launch {
                    val item = databaseSource.getNoteById(id = events.id)
                    val selectedItem = item?.let {
                        Note(
                            id = it._id,
                            heading = item.heading,
                            date = item.date,
                            note = item.note,
                            color = item.color,
                            category = item.category
                        )
                    }

                    _uiState.update {
                        it.copy(itemInDetail = selectedItem)
                    }


                }

            }

            is UiEvents.UpdateNote -> {
                viewModelScope.launch {
                    databaseSource.update(notes = events.note)
                    updateAll()
                }
            }

            is UiEvents.DeleteNote -> {
                viewModelScope.launch {
                    databaseSource.deleteNote(id = events.id)
                    updateAll()
                }

            }
        }
    }


    private fun updateAll() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { uiState ->
                allNote.clear()
                databaseSource.getAllNote().forEach {
                    allNote.add(
                        Note(
                            heading = it.heading,
                            date = it.date,
                            note = it.note,
                            category = it.category,
                            color = it.color,
                            id = it._id
                        )
                    )
                    category.add(it.category)
                }


                uiState.copy(note = allNote, filterData = category)
            }
        }
    }


}