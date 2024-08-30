package org.example.project.domain.database

import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import org.example.project.data.Note
import org.example.project.data.NoteDatabase

class RoomDatabaseSource(db: Realm) : DatabaseService {

    private val realm = db

    override suspend fun addNewNote(notes: NoteDatabase) {
        realm.write {
            copyToRealm(notes)
        }
    }

    override suspend fun getAllNote(): List<NoteDatabase> {
        val allDataQuery = realm.query<NoteDatabase>()
        return allDataQuery.find()
    }

    override suspend fun getNoteById(id: String): NoteDatabase? {
        val data = realm.query<NoteDatabase>("_id == $0", id)
        return try {
            data.find().first()
        } catch (e: Exception) {
            print(data)
            null
        }
    }

    override suspend fun deleteNote(id: String) {
        realm.write {
            val data = query<NoteDatabase>("_id == $0", id).find().first()
            delete(data)
        }

    }

    override suspend fun update(notes: Note) {
        realm.write {
            val data = query<NoteDatabase>("_id == $0", notes.id).find().first()
            data.heading = notes.heading
            data.note = notes.note
            data.category = notes.category

        }
    }
}