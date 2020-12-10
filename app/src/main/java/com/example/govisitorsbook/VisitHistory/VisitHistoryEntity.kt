package com.example.govisitorsbook.VisitHistory

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// 블록체인에 저장되는 데이터 : 이름, 전화번호, 주소, 위치, 싸인, 일시
// roomdb에 저장되는 데이터 : 유저ID, 주소, 일시
@Entity(tableName = "VisitHistory")
data class VisitHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long ?= null,
    var userId:String,      // 유저ID
    var address:String,     // 장소
    var visitDate:String    // 일시
)
