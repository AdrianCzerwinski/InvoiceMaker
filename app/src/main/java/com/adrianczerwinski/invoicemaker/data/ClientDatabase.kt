package com.adrianczerwinski.invoicemaker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.adrianczerwinski.invoicemaker.data.ClientsDao
import com.adrianczerwinski.invoicemaker.data.models.Client
import com.adrianczerwinski.invoicemaker.data.models.Seller


@Database(entities = [Client::class], version =1, exportSchema = false)

abstract class ClientDatabase: RoomDatabase() {

    abstract fun clientsDao(): ClientsDao

    companion object {

        @Volatile
        private var INSTANCE: ClientDatabase? = null

        fun getDatabase(context: Context): ClientDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClientDatabase::class.java,
                    "invoiceMaker_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }


}