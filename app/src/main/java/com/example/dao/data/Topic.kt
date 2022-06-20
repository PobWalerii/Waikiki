package com.example.dao.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Topic(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val topicName: String,
    val topicDate: String,
    val topicComment: String,
    val icon: Int =0,
    val icoColor: Int = 0,
    val actualization: Int = 0
)
