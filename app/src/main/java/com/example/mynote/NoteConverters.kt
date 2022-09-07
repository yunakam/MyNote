package com.example.mynote

import androidx.room.TypeConverter
import java.time.LocalDateTime

// from https://torikatsu923.hatenablog.com/entry/2019/11/14/154251
class LocalDateConverter {
    /**
     * フォーマットはyyyy-MM-dd(デフォルト)
     * */
    @TypeConverter
    fun toLocalDate(stringDate: String): LocalDateTime {
        return LocalDateTime.parse(stringDate)
    }

    /**
     * フォーマットはyyyy-MM-dd(デフォルト)
     * */
    @TypeConverter
    fun fromLocalDate(localDate: LocalDateTime): String {
        return localDate.toString()
    }
}

// from https://stackoverflow.com/questions/54927913/room-localdatetime-typeconverter
class LocalDateConverter2 {
    @TypeConverter
    fun toLocalDate(timestamp: String?): LocalDateTime? {
        return if (timestamp == null){
            null
        } else {
            LocalDateTime.parse(timestamp)
        }
    }

    @TypeConverter
    fun fromLocalDate(date: LocalDateTime?): String? {
        return if (date == null){
            null
        } else {
            date.toString()
        }
    }
}