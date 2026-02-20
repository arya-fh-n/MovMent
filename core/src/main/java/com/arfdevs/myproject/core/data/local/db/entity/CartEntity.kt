package com.arfdevs.myproject.core.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arfdevs.myproject.core.helper.Constants.CART_TABLE

@Entity(tableName = CART_TABLE)
data class CartEntity(

    @PrimaryKey
    @ColumnInfo(name = "movieId")
    val movieId: Int = 0,

    @ColumnInfo(name = "userId")
    val userId: String = "",

    @ColumnInfo(name = "originalTitle")
    val originalTitle: String = "",

    @ColumnInfo(name = "posterPath")
    val posterPath: String = "",

    @ColumnInfo(name = "voteAverage")
    val voteAverage: Double = 0.0,

    @ColumnInfo(name = "price")
    val price: Int = 0,

)