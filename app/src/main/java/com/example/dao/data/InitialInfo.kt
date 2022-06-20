package com.example.dao.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class InitialInfo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val topicId: Int,
    val text: String,
    val photo: String,
    val video: String,
    val sound: String,
    val comment: String
)
