package com.example.wireless_project.database.dao

import androidx.room.*
import com.example.wireless_project.database.entity.Exercises
import com.example.wireless_project.database.entity.Food
import io.reactivex.Completable
import io.reactivex.Flowable
@Dao
interface ExercisesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExercises(food: Exercises) : Completable

    @Update
    fun updateExercises(food: Exercises) : Completable

    @Delete
    fun deleteExercises(food: Exercises) : Completable

    @Query("SELECT * FROM Exercises WHERE userEmail == :email")
    fun getExercises(email: String): Flowable<List<Exercises>>
}