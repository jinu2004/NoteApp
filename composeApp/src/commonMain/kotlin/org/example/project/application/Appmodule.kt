package org.example.project.application

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.example.project.data.NoteDatabase
import org.example.project.domain.database.DatabaseService
import org.example.project.domain.database.RoomDatabaseSource
import org.example.project.domain.viewmodel.MainViewModel
import org.koin.dsl.module


val mainModule = module {
    single { Realm.open(RealmConfiguration.create(schema = setOf(NoteDatabase::class))) }
    single<DatabaseService> { RoomDatabaseSource(get()) }
    single { MainViewModel(get()) }
}