package com.example.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dao.data.CollectedInfo
import com.example.dao.data.Contakt
import com.example.dao.data.InitialInfo
import com.example.dao.data.Topic

@Database(entities = [Topic::class, Contakt::class, InitialInfo::class, CollectedInfo::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun topicDao(): TopicDao
    abstract fun contaktDao(): ContaktDao
    abstract fun initialDao(): InitialDao
    abstract fun collectedDao(): CollectedDao
}