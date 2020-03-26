package com.example.wireless_project.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wireless_project.database.dao.FoodDao
import com.example.wireless_project.database.dao.UserDao
import java.lang.IllegalArgumentException

class ViewModelFactory(private val dataSource: UserDao): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)){
            return UserViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}