package com.example.govisitorsbook.Login

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/* UserDatabase 선언 - RoomDatabase() 상속
   - entities 어노테이션으로 데이터베이스에 포함된 엔티티 목록을 가져온다.
   - 데이터베이스의 구조가 바뀌면 Version이 바뀌어야합니다.
*/
@Database(entities = arrayOf(UserEntity::class), version = 2)
abstract class UserDatabase : RoomDatabase() {

    /* 데이터베이스에 access하는데 사용하는 Dao(Database Access, Object)들을 가져옵니다. */
    abstract fun dao(): UserDao

    /* 전역으로 사용하기 위해 final static 속성, 메서드 선언 */
    companion object {
        // database 변수 선언
        private var database: UserDatabase? = null

        //database 이름 상수 선언
        private const val ROOM_DB = "room.db"

        /* 정의한 Database 객체를 가져오는 함수 선언 */
        fun getDatabase(context: Context?): UserDatabase {
            if (context != null) {
                if (database == null) {
                    database = Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java, ROOM_DB
                        /*원칙적으로 메인쓰레드에서는 Room을 사용할 수 없지만 실습을 위해 허가합니다.*/
                    ).build()
                }
            }
            /* 안전한 강제 캐스팅 */
            return database!!

        }
    }
}