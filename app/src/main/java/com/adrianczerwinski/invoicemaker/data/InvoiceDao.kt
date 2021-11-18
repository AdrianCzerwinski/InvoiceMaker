package com.adrianczerwinski.invoicemaker.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.adrianczerwinski.invoicemaker.data.models.Client
import com.adrianczerwinski.invoicemaker.data.models.Invoice
import com.adrianczerwinski.invoicemaker.data.models.Job
import com.adrianczerwinski.invoicemaker.data.relations.InvoiceWithJobs

@Dao
interface InvoiceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addInvoice(invoice: Invoice)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addJob(job: Job)

    @Query("SELECT * FROM invoices_table ORDER BY id ASC")
    fun readAllInvoices(): LiveData<List<Invoice>>

    @Update()
    fun updateInvoice(invoice: Invoice)

    @Delete
    suspend fun deleteInvoice(invoice: Invoice)

    @Transaction
    @Query("SELECT * FROM invoices_table WHERE invoiceNumber = :invoiceNumber ORDER BY id ASC" )
    suspend fun getInvoiceWithJobs(invoiceNumber: String): List<InvoiceWithJobs>

//    @Transaction
//    @Query("SELECT * FROM jobs_table WHERE invoiceNumber = :invoiceNumber ORDER BY jobId ASC" )
//    suspend fun getJobsWithInvoice(invoiceNumber: String): List<InvoiceWithJobs>




}