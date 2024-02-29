package com.arfdevs.myproject.core.data.local.datasource

import androidx.lifecycle.LiveData
import com.arfdevs.myproject.core.data.local.db.Dao
import com.arfdevs.myproject.core.data.local.db.entity.WishlistEntity
import com.arfdevs.myproject.core.data.local.sharedpref.SharedPreferencesHelper
import com.arfdevs.myproject.core.helper.safeDataCall

class LocalDataSource(
    private val dao: Dao,
    private val sharedPreferencesHelper: SharedPreferencesHelper,
) {

    suspend fun insertWishlistMovie(wishlist: WishlistEntity) {
        dao.insertWishlistMovie(wishlist)
    }

    fun gethWishlistMovie(userId: String): LiveData<List<WishlistEntity>> = dao.getWishlistMovie(userId)

    suspend fun checkFavorite(movieId: Int): Int = safeDataCall {
        dao.checkFavorite(movieId)
    }

    suspend fun deleteWishlistMovie(wishlist: WishlistEntity) {
        dao.deleteWishlistMovie(wishlist)
    }

    suspend fun deleteWishlistTable() {
        dao.deleteWishlistTable()
    }

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