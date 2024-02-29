package com.arfdevs.myproject.core.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arfdevs.myproject.core.data.local.db.entity.WishlistEntity

@Database(entities = [WishlistEntity::class], version = 2)
abstract class Database: RoomDatabase() {

    abstract fun Dao(): Dao

}