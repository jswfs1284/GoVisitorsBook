package com.example.govisitorsbook.Login

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity @Parcelize
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long ?= null,
    val userID: String,     // 회원가입 시 ID
    //val userPass: String,
    val name: String,
    val tel: String
) : Parcelable
