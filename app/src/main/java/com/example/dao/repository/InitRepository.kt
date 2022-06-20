package com.example.dao.repository

import com.example.dao.InitialDao
import com.example.dao.data.InitialInfo

class InitRepository(val initialDao: InitialDao) {

    suspend fun getAll(curTopicId: Int): List<InitialInfo> {
        val allInfo: List<InitialInfo> = initialDao.getAll(curTopicId)
        return allInfo
    }

}