package com.example.wireless_project.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = User::class,
    parentColumns = arrayOf("email"),
    childColumns = arrayOf("userEmail"),
    onDelete = ForeignKey.CASCADE)]
)
data class Food (
    @PrimaryKey(autoGenerate = true) var id : Int? =null,
    @ColumnInfo(name = "userEmail") var userEmail: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "carbohydrate") var carb: Double,
    var protein: Double,
    var fat: Double,
    var location: String,
    @ColumnInfo(name="picture") var pic: String? =null,
    @ColumnInfo(name="date") var addedDate: String
)