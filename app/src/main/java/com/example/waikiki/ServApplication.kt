package com.example.waikiki

import android.app.Application
import androidx.room.Room
import com.example.constants.KeyConstants
import com.example.dao.AppDatabase
import com.example.dao.repository.ContaktRepository
import com.example.dao.repository.InfoRepository
import com.example.dao.repository.InitRepository
import com.example.dao.repository.TopicRepository

class ServApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            KeyConstants.DATABASE_NAME
        ).build()           //.fallbackToDestructiveMigration()  // очищаем при изменениях,
    }
    val repository by lazy { TopicRepository(database.topicDao()) }
    val repository1 by lazy { ContaktRepository(database.contaktDao()) }
    val repository2 by lazy { InfoRepository(database.collectedDao()) }
    val repository3 by lazy { InitRepository(database.initialDao()) }

    companion object {
        lateinit var database: AppDatabase
    }

}