package com.example.ratatouille.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Favorite::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteDAO(): FavoriteDAO

    companion object {
        // Instância única do banco de dados (singleton?? algo assim)
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Obtém a instância do banco de dados
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "favoritos.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
