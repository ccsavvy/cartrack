package com.jetpack.compose.cartrack.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Repository(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    
    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "password")
    val password: String
)