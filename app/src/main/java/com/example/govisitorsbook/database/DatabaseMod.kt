package com.example.govisitorsbook.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.govisitorsbook.Login.User
import com.example.govisitorsbook.Login.UserDao
import com.example.govisitorsbook.VisitHistory.VisitHistoryDao
import com.example.govisitorsbook.VisitHistory.VisitHistoryEntity


@Database(entities = arrayOf(User::class, VisitHistoryEntity::class), version = 1)
abstract class DatabaseMod : RoomDatabase() {

    /* Query 문에 사용하는 Dao가져오기. */

    abstract fun userDao(): UserDao
    abstract fun visitHistoryDao(): VisitHistoryDao

    companion object {
        // database 변수 선언
        private var database: DatabaseMod? = null

        //database 이름 상수 선언
        private const val ROOM_DB = "database.db"

        /* 정의한 Database 객체를 가져오는 함수 선언 */
        fun getDatabase(context: Context): DatabaseMod {
            if (database == null) {
                database = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseMod::class.java, ROOM_DB
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
            }
            /* 안전한 강제 캐스팅 */
            return database!!
        }
    }
}