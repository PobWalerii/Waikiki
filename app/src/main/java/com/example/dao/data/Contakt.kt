package com.example.dao.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Contakt(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val topicId: Int,
    val contaktName: String,
    val contaktPhone: String,
    val contaktComment: String
)

