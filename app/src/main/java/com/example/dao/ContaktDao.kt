package com.example.dao

import androidx.room.*
import com.example.dao.data.Contakt

@Dao
interface ContaktDao {

    @Query("SELECT * FROM contakt WHERE topicId = :curTopicId")
    suspend fun getAll(curTopicId: Int): List<Contakt>

    @Query("SELECT * FROM contakt WHERE id = :curId")
    suspend fun getByID(curId: Int): List<Contakt>

    @Query("DELETE FROM contakt WHERE id = :curId")
    suspend fun deleteByID(curId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contakt: Contakt): Long

    @Delete
    suspend fun delete(user: Contakt)
}