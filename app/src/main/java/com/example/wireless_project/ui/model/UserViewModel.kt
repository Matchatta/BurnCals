package com.example.wireless_project.ui.model

import androidx.lifecycle.ViewModel
import com.example.wireless_project.database.dao.UserDao
import com.example.wireless_project.database.entity.User
import io.reactivex.Completable
import io.reactivex.Flowable

class UserViewModel(private val dataSource: UserDao) :ViewModel(){
    fun getUser(email: String): Flowable<List<User>>{
        return dataSource.getUserByEmail(email)
    }

    fun insertUser(user: User): Completable{
        return dataSource.insertUser(user)
    }
}