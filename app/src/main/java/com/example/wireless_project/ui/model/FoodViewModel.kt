package com.example.wireless_project.ui.model

import androidx.lifecycle.ViewModel
import com.example.wireless_project.database.dao.FoodDao
import com.example.wireless_project.database.entity.Food
import io.reactivex.Completable
import io.reactivex.Flowable

class FoodViewModel(private val dataSource: FoodDao): ViewModel() {
    fun getFood(email: String): Flowable<List<Food>> {
        return dataSource.getFood(email)
    }
    fun insertFood(food: Food): Completable {
        return dataSource.insertFood(food)
    }
    fun updateFood(food: Food): Completable{
        return  dataSource.updateFood(food)
    }
    fun deleteFood(food: Food): Completable{
        return dataSource.deleteFood(food)
    }
}