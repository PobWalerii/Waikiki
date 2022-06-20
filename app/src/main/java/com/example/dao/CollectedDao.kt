package com.example.dao

import androidx.room.*
import com.example.dao.data.CollectedInfo
import com.example.dao.data.Contakt

@Dao
interface CollectedDao {

    @Query("SELECT * FROM collectedInfo WHERE topicId = :curTopicId ORDER BY dateInfo")
    suspend fun getAll(curTopicId: Int): List<CollectedInfo>

//    @Query("SELECT * FROM collectedInfo WHERE topicId = :curTopicId AND markInfo= :curMark ORDER BY dateInfo DESC")
//    suspend fun getOnlyMark(curTopicId: Int, curMark: Int): List<CollectedInfo>

    @Query("SELECT * FROM collectedInfo WHERE id = :curId")
    suspend fun getByID(curId: Int): List<CollectedInfo>

    @Query("DELETE FROM collectedInfo WHERE id = :curId")
    suspend fun deleteByID(curId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(collectedInfo: CollectedInfo): Long

    @Delete
    suspend fun delete(user: CollectedInfo)

}