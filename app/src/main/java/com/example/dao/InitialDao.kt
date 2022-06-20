package com.example.dao

import androidx.room.*
import com.example.dao.data.CollectedInfo
import com.example.dao.data.InitialInfo

@Dao
interface InitialDao {

    @Query("SELECT * FROM initialInfo WHERE topicId = :curTopicId")
    suspend fun getAll(curTopicId: Int): List<InitialInfo>

    @Query("SELECT * FROM initialInfo WHERE id = :curId")
    suspend fun getByID(curId: Int): List<InitialInfo>

    @Query("DELETE FROM initialinfo WHERE id = :curId")
    suspend fun deleteByID(curId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(initialInfo: InitialInfo): Long

    @Delete
    suspend fun delete(user: InitialInfo)
}