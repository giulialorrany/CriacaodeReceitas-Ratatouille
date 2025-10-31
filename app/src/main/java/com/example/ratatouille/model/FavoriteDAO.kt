package com.example.ratatouille.model

import androidx.room.*

@Dao
interface FavoriteDAO {
    @Insert
    suspend fun insert(favorite: Favorite)
    @Update
    suspend fun update(favorite: Favorite)
    @Delete
    suspend fun delete(favorite: Favorite)
    @Query("SELECT * FROM favoritos")
    suspend fun selectAll(): List<Favorite>
    @Query("SELECT * FROM favoritos WHERE id = :id")
    suspend fun selectById(id: Int): Favorite?
}