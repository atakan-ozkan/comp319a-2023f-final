package com.example.lifecanvas.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.lifecanvas.dao.NoteDao
import com.example.lifecanvas.dao.SketchDao
import com.example.lifecanvas.helper.Converter
import com.example.lifecanvas.model.NoteModel
import com.example.lifecanvas.model.SketchModel

@Database(entities = [NoteModel::class, SketchModel::class], version = 2, exportSchema = true)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun sketchDao(): SketchDao
}