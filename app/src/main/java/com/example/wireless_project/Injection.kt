package com.example.wireless_project

import android.content.Context
import com.example.wireless_project.database.DatabaseConnector
import com.example.wireless_project.database.dao.FoodDao
import com.example.wireless_project.database.dao.UserDao
import com.example.wireless_project.ui.model.FoodModelFactory
import com.example.wireless_project.ui.model.ViewModelFactory

object Injection {
    private fun dataSource(context: Context) : DatabaseConnector{
        return  DatabaseConnector.getInstance(context)
    }
    private fun provideUserDataSource(context: Context): UserDao{
        return dataSource(context).userDao()
    }
    private fun provideFoodDataSource(context: Context): FoodDao{
        return dataSource(context).foodDao()
    }
    fun provideViewModelFactory(context: Context): ViewModelFactory{
        val dataSource = provideUserDataSource(context)
        return ViewModelFactory(dataSource)
    }
    fun provideFoodViewModelFactory(context: Context): FoodModelFactory{
        val dataSource = provideFoodDataSource(context)
        return FoodModelFactory(dataSource)
    }
}