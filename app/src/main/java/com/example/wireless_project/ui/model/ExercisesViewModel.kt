package com.example.wireless_project.ui.model

import androidx.lifecycle.ViewModel
import com.example.wireless_project.database.dao.ExercisesDao
import com.example.wireless_project.database.entity.Exercises
import io.reactivex.Completable
import io.reactivex.Flowable

class ExercisesViewModel(private val dataSource: ExercisesDao): ViewModel() {
    fun getExercises(email: String): Flowable<List<Exercises>> {
        return dataSource.getExercises(email)
    }
    fun insertExercises(exercises: Exercises): Completable {
        return dataSource.insertExercises(exercises)
    }
    fun updateExercises(exercises: Exercises): Completable{
        return  dataSource.updateExercises(exercises)
    }
    fun deleteExercises(exercises: Exercises): Completable{
        return dataSource.deleteExercises(exercises)
    }
}
