package com.example.govisitorsbook.Login

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id:Long? = null,
    var userName: String? = null,         // 이름
    var phoneNumber:String? = null,      // 전화번호
    var signPath:String? = null,     // 사인 이미지 저장 경로
    var autoLogin:Boolean = false
)



