package com.example.dao.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CollectedInfo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val topicId: Int,
    val dateInfo: String,
    val text: String,
    val photo: String,
    val video: String,
    val sound: String,
    val comment: String,
    val markInfo: Int = 0
)
