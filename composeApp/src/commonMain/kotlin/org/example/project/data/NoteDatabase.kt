package org.example.project.data

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class NoteDatabase : RealmObject {
    @PrimaryKey
    var _id: String = ObjectId().toHexString()
    var heading: String = ""
    var date: String = ""
    var note: String = ""
    var category: String = ""
    var color: Int = 0
}