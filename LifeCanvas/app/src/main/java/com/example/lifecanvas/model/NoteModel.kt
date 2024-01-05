package com.example.lifecanvas.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "notes")
data class NoteModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String, // "text", "voice", or "image"
    val title: String,
    val content: String, // Text content or file path/URI for voice and image
    val isPublic: Boolean, // true for public, false for private
    val filePath: String? = null, // File path for images/voice recordings, null for text notes
    val createdDate: Date,
    val modifiedDate: Date
    )