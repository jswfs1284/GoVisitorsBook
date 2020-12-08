package com.example.govisitorsbook.Login

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.govisitorsbook.Login.UserEntity

/* DAO(Database Access Object) 정의
   - 데이터베이스에 엑세스하는데 사용되는 메소드(insert, query, update, delete) 선언
*/
@Dao
interface UserDao {

    /* Insert Annotation으로 Insert를 정의
      - key 중복시 Strategy 설정
    */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(userEntity: UserEntity)

    /* Query Annotation으로 쿼리를 정의   */
    @Query("SELECT * FROM UserEntity WHERE userID = :userID and userPass = :userPass")
    fun selectUser(userID: String, userPass: String): UserEntity?
}
