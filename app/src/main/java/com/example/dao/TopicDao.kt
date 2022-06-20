package com.example.dao

import androidx.room.*
import com.example.dao.data.Topic

@Dao
interface TopicDao {

    @Query("SELECT * FROM topic ORDER BY 1-actualization, topicDate DESC")
    suspend fun getAll(): List<Topic>

    @Query("SELECT * FROM topic WHERE id = :curId")
    suspend fun getByID(curId: Int): List<Topic>

    @Query("DELETE FROM topic WHERE id = :curId")
    suspend fun deleteByID(curId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(topic: Topic): Long

    @Delete
    suspend fun delete(user: Topic)
}