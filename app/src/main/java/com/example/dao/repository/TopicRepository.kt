package com.example.dao.repository

import com.example.dao.TopicDao
import com.example.dao.data.Topic

class TopicRepository(val topicDao: TopicDao) {

    suspend fun getAll(): List<Topic> {
        val allTopic: List<Topic> = topicDao.getAll()
        return allTopic
    }
}