package com.adrianczerwinski.faktura.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.adrianczerwinski.faktura.data.models.Seller


@Database(entities = [Seller::class], version =2, exportSchema = false)

abstract class SellerDatabase: RoomDatabase() {

    abstract fun sellerDao(): SellerDao

    companion object {

        @Volatile
        private var INSTANCE: SellerDatabase? = null

        fun getDatabase(context: Context): SellerDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SellerDatabase::class.java,
                    "seller_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }


}