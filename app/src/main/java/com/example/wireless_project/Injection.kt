package com.example.wireless_project

import android.content.Context
import com.example.wireless_project.database.DatabaseConnector
import com.example.wireless_project.database.dao.ExercisesDao
import com.example.wireless_project.database.dao.FoodDao
import com.example.wireless_project.database.dao.UserDao
import com.example.wireless_project.ui.model.ExercisesModelFactory
import com.example.wireless_project.ui.model.FoodModelFactory
import com.example.wireless_project.ui.model.ViewModelFactory

object Injection {
    //get data source from database
    private fun dataSource(context: Context) : DatabaseConnector{
        return  DatabaseConnector.getInstance(context)
    }
    //user data source
    private fun provideUserDataSource(context: Context): UserDao{
        return dataSource(context).userDao()
    }
    //food data source
    private fun provideFoodDataSource(context: Context): FoodDao{
        return dataSource(context).foodDao()
    }
    //exercises data source
    private fun provideExercisesDataSource(context: Context): ExercisesDao{
        return dataSource(context).exercisesDao()
    }
    //user view Model factory
    fun provideViewModelFactory(context: Context): ViewModelFactory{
        val dataSource = provideUserDataSource(context)
        return ViewModelFactory(dataSource)
    }
    //food view model factory
    fun provideFoodViewModelFactory(context: Context): FoodModelFactory{
        val dataSource = provideFoodDataSource(context)
        return FoodModelFactory(dataSource)
    }
    //exercises view model factory
    fun provideExercisesViewModelFactory(context: Context): ExercisesModelFactory{
        val dataSource = provideExercisesDataSource(context)
        return ExercisesModelFactory(dataSource)
    }
}