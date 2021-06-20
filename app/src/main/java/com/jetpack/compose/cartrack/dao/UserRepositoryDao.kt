package com.jetpack.compose.cartrack.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jetpack.compose.cartrack.model.Repository

@Dao
interface UserRepositoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(artist : Repository)

    @Query("SELECT * FROM repository")
    suspend fun getUsers(): Repository

    @Query("DELETE FROM repository")
    suspend fun deleteAllUsers()
}