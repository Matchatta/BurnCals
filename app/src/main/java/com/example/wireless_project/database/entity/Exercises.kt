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
data class Exercises(
    @PrimaryKey(autoGenerate = true) var id: Int? =null,
    @ColumnInfo(name = "userEmail") var userEmail: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "type") var type: String,
    @ColumnInfo(name = "Calories") var cals: Double,
    @ColumnInfo(name="date") var addedDate: String,
    @ColumnInfo(name="picture") var pic: String? =null,
    var location: String
)