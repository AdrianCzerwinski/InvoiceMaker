package com.adrianczerwinski.faktura.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.adrianczerwinski.faktura.data.models.Invoice
import com.adrianczerwinski.faktura.data.models.Job

@Dao
interface InvoiceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addInvoice(invoice: Invoice)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addJob(job: Job)

    @Query("SELECT * FROM invoices_table ORDER BY date, invoiceNumber DESC ")
    fun readAllInvoices(): LiveData<List<Invoice>>

    @Update()
    fun updateInvoice(invoice: Invoice)

    @Delete
    suspend fun deleteInvoice(invoice: Invoice)

//
//    @Transaction
//    @Query("SELECT * FROM jobs_table WHERE invoiceNumber = :invoiceNumber " )
//    suspend fun getJobsWithInvoice(invoiceNumber: String): List<InvoiceWithJobs>




}