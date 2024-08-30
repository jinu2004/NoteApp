package org.example.project.domain.database

import org.example.project.data.Note
import org.example.project.data.NoteDatabase

interface DatabaseService {
    suspend fun addNewNote(notes: NoteDatabase)
    suspend fun getAllNote(): List<NoteDatabase>
    suspend fun getNoteById(id: String): NoteDatabase?
    suspend fun deleteNote(id: String)
    suspend fun update(notes: Note)
}