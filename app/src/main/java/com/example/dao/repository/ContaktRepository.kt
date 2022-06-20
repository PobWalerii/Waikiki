package com.example.dao.repository

import com.example.dao.ContaktDao
import com.example.dao.data.Contakt

class ContaktRepository(val contaktDao: ContaktDao) {

    suspend fun getAll(curTopicId: Int): List<Contakt> {
        val allContakt: List<Contakt> = contaktDao.getAll(curTopicId)
        return allContakt
    }

}