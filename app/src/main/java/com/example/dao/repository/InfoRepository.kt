package com.example.dao.repository

import com.example.dao.CollectedDao
import com.example.dao.data.CollectedInfo

class InfoRepository(val collectedDao: CollectedDao) {

    suspend fun getAll(curTopicId: Int): List<CollectedInfo> {
        val allInfo: List<CollectedInfo> = collectedDao.getAll(curTopicId)
        return allInfo
    }

}