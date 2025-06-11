package com.arfdevs.myproject.core.data.local.datasource

import androidx.lifecycle.LiveData
import com.arfdevs.myproject.core.data.local.db.Dao
import com.arfdevs.myproject.core.data.local.db.entity.CartEntity
import com.arfdevs.myproject.core.data.local.db.entity.WishlistEntity
import com.arfdevs.myproject.core.data.local.sharedpref.SharedPreferencesHelper
import com.arfdevs.myproject.core.helper.safeDataCall
import kotlinx.coroutines.flow.Flow

class LocalDataSource(
    private val dao: Dao,
    private val sharedPreferencesHelper: SharedPreferencesHelper,
) {

    suspend fun insertWishlistMovie(wishlist: WishlistEntity) {
        dao.insertWishlistMovie(wishlist)
    }

    fun getWishlistMovie(userId: String): LiveData<List<WishlistEntity>> = dao.getWishlistMovie(userId)

    suspend fun checkFavorite(movieId: Int): Int = safeDataCall {
        dao.checkFavorite(movieId)
    }

    suspend fun deleteWishlistMovie(wishlist: WishlistEntity) {
        dao.deleteWishlistMovie(wishlist)
    }

    suspend fun deleteAllWishlist(userId: String) {
        dao.deleteAllWishlistItem(userId)
    }

    suspend fun insertCartMovie(cart: CartEntity) {
        dao.insertCart(cart)
    }

    fun getCartList(userId: String): LiveData<List<CartEntity>> {
        return dao.getCartList(userId)
    }

    fun getCartItemById(movieId: Int, userId: String): Flow<CartEntity?> {
        return dao.getCartItemById(movieId, userId)
    }

    suspend fun deleteCartItem(cart: CartEntity) = dao.deleteCartItem(cart)

    suspend fun deleteAllCart() = dao.deleteAllCartItem()

    fun getOnboardingState(): Boolean =
        sharedPreferencesHelper.getOnboardingState()


    fun saveOnboardingState(state: Boolean)  {
        sharedPreferencesHelper.putOnboardingState(state)
    }

    fun getLanguage(): String =
        sharedPreferencesHelper.getLanguage()


    fun saveLanguage(value: String) {
        sharedPreferencesHelper.putLanguage(value)
    }

    fun getUID(): String =
        sharedPreferencesHelper.getUID()


    fun saveUID(value: String) {
        sharedPreferencesHelper.putUID(value)
    }

    fun getTheme(): Boolean =
        sharedPreferencesHelper.getTheme()


    fun saveTheme(value: Boolean) {
        sharedPreferencesHelper.putTheme(value)
    }

}