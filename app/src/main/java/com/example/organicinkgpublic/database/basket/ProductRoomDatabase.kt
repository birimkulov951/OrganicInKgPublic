package com.example.organicinkgpublic.database.basket

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ProductEntity::class], version = 1/*, exportSchema = false*/)
abstract class ProductRoomDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: ProductRoomDatabase? = null

        fun getDatabase(context: Context): ProductRoomDatabase {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, ProductRoomDatabase::class.java, "basket_database").build()
                INSTANCE = instance
                return instance
            }
        }
    }

}
