package com.example.mynote

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey    // this cannot be omitted
    @NonNull
    val id: String,

    @ColumnInfo(name = "noteText")    // this line is removable as it will be named after the variable name
    var noteText: String?,

    @ColumnInfo(name = "source")
    var source: String?,

    @ColumnInfo(name = "dateAdded")
    var dateAdded: LocalDateTime,   // changed from LocalDateTime to remove the error from contentValues.put(KEY_DATE, note.date) in DatabaseHandler.addNote()

    @ColumnInfo(name = "tag")
    var tag: String?,
)
