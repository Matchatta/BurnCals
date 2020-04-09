package com.example.wireless_project.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wireless_project.database.dao.ExercisesDao
import java.lang.IllegalArgumentException

class ExercisesModelFactory(private val dataSource:ExercisesDao): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExercisesViewModel::class.java)){
            return ExercisesViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}