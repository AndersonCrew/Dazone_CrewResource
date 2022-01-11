package com.kunpark.resource.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kunpark.resource.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM USER")
    fun getUser(): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUserData(user: User)

    @Delete
    fun deleteUser(user: User)
}