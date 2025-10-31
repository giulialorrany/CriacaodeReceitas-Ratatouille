package com.example.ratatouille.model

import androidx.room.*

@Entity(tableName = "favoritos")
class Favorite(
    @PrimaryKey
    var id: Int,
    @ColumnInfo
    var name: String,
    @ColumnInfo
    var ingredients: String,
    @ColumnInfo
    var steps: String
)