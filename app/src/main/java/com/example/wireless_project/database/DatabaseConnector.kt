package com.example.wireless_project.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.wireless_project.database.dao.FoodDao
import com.example.wireless_project.database.dao.UserDao
import com.example.wireless_project.database.entity.Food
import com.example.wireless_project.database.entity.User

@Database(entities = [User::class, Food::class], version = 1)

abstract class DatabaseConnector: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun foodDao(): FoodDao
    companion object {
        @Volatile private var INSTANCE: DatabaseConnector? = null
        fun getInstance(context: Context): DatabaseConnector =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: buildDatabase(context).also { INSTANCE=it }
            }

        private fun buildDatabase(context: Context) =
            databaseBuilder(context.applicationContext,
                DatabaseConnector::class.java, "Sample.db")
                .build()
    }
}