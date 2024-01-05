package com.example.lifecanvas.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.lifecanvas.dao.NoteDao
import com.example.lifecanvas.helper.Converter
import com.example.lifecanvas.model.NoteModel

@Database(entities = [NoteModel::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}