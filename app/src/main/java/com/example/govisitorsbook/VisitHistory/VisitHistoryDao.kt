package com.example.govisitorsbook.VisitHistory

import androidx.lifecycle.LiveData
import androidx.room.*

// 방문기록 DAO
@Dao
interface VisitHistoryDao {

    // key 중복시 strategy 설정
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVisitHistory(vararg entities: VisitHistoryEntity)

    // 기록 전체
    @Query("SELECT * FROM VisitHistory WHERE userId = :userId ORDER BY userId DESC")
    fun selectVisitHistory(userId: String): LiveData<List<VisitHistoryEntity>>

    /*@Query("SELECT * FROM VisitHistory WHERE userId = :userId")
    fun selectLiveVisitHistory(userId: Long): LiveData<VisitHistoryEntity>*/


    // 2주 지난거 삭제하기
    /*
    @Query("DELETE FROM VisitHistory WHERE ???")
    fun deleteVisitHisotry(???)
    */
}

