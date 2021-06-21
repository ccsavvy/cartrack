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

    @Query("SELECT * FROM repository WHERE repository.username = :username AND repository.password = :password")
    suspend fun findUsernameByUsername(username: String, password: String): List<Repository>

    @Query("SELECT * FROM repository")
    suspend fun getUsers(): List<Repository>

    @Query("DELETE FROM repository")
    suspend fun deleteAllUsers()
}