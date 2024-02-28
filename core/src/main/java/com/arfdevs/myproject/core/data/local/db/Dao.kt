package com.arfdevs.myproject.core.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arfdevs.myproject.core.data.local.db.entity.WishlistEntity

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlistMovie(wishlist: WishlistEntity)

    @Query("SELECT * FROM wishlist WHERE userId = :id")
    fun getWishlistMovie(id: String): LiveData<List<WishlistEntity>>

    @Query("SELECT EXISTS (SELECT * FROM wishlist WHERE movieId = :movieId)")
    suspend fun checkFavorite(movieId: Int): Int

    @Delete
    suspend fun deleteWishlistMovie(wishlist: WishlistEntity)

    @Query("DELETE FROM wishlist")
    suspend fun deleteWishlistTable()

}