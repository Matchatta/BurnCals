package com.example.wireless_project.database.dao

import androidx.room.*
import com.example.wireless_project.database.entity.Food
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface FoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFood(food: Food) : Completable

    @Update
    fun updateFood(food: Food)

    @Delete
    fun deleteFood(food: Food)

    @Query("SELECT * FROM Food WHERE userEmail == :email")
    fun getFoodByDate(email: String): Flowable<List<Food>>

}