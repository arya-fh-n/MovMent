package com.arfdevs.myproject.core.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arfdevs.myproject.core.data.local.db.entity.CartEntity
import com.arfdevs.myproject.core.data.local.db.entity.WishlistEntity
import kotlinx.coroutines.flow.Flow

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cart: CartEntity)

    @Query("SELECT * FROM cart WHERE userId = :userId")
    fun getCartList(userId: String): LiveData<List<CartEntity?>>

    @Query("SELECT * FROM cart WHERE movieId = :movieId AND userId = :userId")
    fun getCartItemById(movieId: Int, userId: String): Flow<CartEntity?>

    @Delete
    suspend fun deleteCartItem(cart: CartEntity)

    @Query("DELETE FROM cart")
    suspend fun deleteAllCartItem()

}