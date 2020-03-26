package com.example.wireless_project.database.entity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = arrayOf(ForeignKey(entity = User::class,
    parentColumns = arrayOf("email"),
    childColumns = arrayOf("userEmail"),
    onDelete = ForeignKey.CASCADE)))
data class Food (
    @PrimaryKey(autoGenerate = true) val id : Int? =null,
    @ColumnInfo(name = "userEmail") val userEmail: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "carbohydrate") val carb: Double,
    val protein: Double,
    val fat: Double,
    val location: String,
    @ColumnInfo(name="picture") val pic: ByteArray? =null,
    @ColumnInfo(name="date") val addedDate: String
)