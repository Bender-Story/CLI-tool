package com.rahul.cliproject.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rahul.cliproject.data.User

@Database(entities = arrayOf(User::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}