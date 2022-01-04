package com.adrianczerwinski.faktura.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.adrianczerwinski.faktura.data.models.Invoice
import com.adrianczerwinski.faktura.data.models.Job

@Database(entities = [Invoice::class,
                     Job::class], version = 1, exportSchema = false)
abstract class InvoicesDatabase: RoomDatabase() {

    abstract fun invoiceDao(): InvoiceDao

    companion object{
        @Volatile
        private var INSTANCE: InvoicesDatabase? = null
        fun getDatabase(context: Context):InvoicesDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    InvoicesDatabase::class.java,
                    "invoices_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}