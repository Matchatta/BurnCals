package com.example.wireless_project.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey val email : String,
    @ColumnInfo(name = "first_name") val first_name : String?,
    @ColumnInfo(name = "last_name") val last_name : String?,
    @ColumnInfo(name = "weight") var weight : Double?,
    @ColumnInfo(name = "height") var height : Double?,
    @ColumnInfo(name = "image") val image : String?,
    @ColumnInfo(name = "gender") var gender : Int?,
    @ColumnInfo(name = "date_of_birth") var dob : String?
)