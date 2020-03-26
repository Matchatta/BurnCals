package com.example.wireless_project.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey val email : String,
    @ColumnInfo(name = "first_name") val first_name : String?,
    @ColumnInfo(name = "last_name") val last_name : String?,
    @ColumnInfo(name = "weight") val weight : Double?,
    @ColumnInfo(name = "height") val height : Double?,
    @ColumnInfo(name = "image") val image : String?,
    @ColumnInfo(name = "gender") val gender : String?,
    @ColumnInfo(name = "date_of_birth") val dob : String?
)