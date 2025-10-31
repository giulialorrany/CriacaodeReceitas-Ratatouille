package com.example.ratatouille.model

import androidx.room.RoomDatabase

@androidx.room.Database(entities = [Favorite::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDAO(): FavoriteDAO
}