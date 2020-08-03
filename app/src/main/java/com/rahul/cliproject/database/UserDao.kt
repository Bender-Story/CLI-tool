package com.rahul.cliproject.database

import androidx.room.*
import com.rahul.cliproject.data.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE userName IN (:userName)")
    fun getUser(userName: String): User

    @Update
    fun updateUser(user: User)

    @Insert
    fun insert(vararg users: User)

    @Delete
    fun delete(user: User)
}
