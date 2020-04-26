package com.example.wireless_project.database.dao

import androidx.room.*
import com.example.wireless_project.database.entity.User
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User) : Completable

    @Update
    fun updateUser(user: User): Completable

    @Delete
    fun deleteUser(user: User)

    @Query("SELECT * FROM User WHERE email == :email")
    fun getUserByEmail(email: String): Flowable<List<User>>

}