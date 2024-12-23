package com.jetpack.compose.cartrack.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jetpack.compose.cartrack.model.Repository

@Database(entities = [Repository::class], version = 1, exportSchema = false)
abstract class UserDB : RoomDatabase() {
    abstract fun userRepositoryDao(): UserRepositoryDao

    companion object {
        @Volatile
        private var instance: UserDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            UserDB::class.java,
            "userdatabase"
        ).build()
    }
}