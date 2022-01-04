package com.adrianczerwinski.faktura.data.repository

import androidx.lifecycle.LiveData
import com.adrianczerwinski.faktura.data.InvoiceDao
import com.adrianczerwinski.faktura.data.models.Invoice
import com.adrianczerwinski.faktura.data.models.Job

class InvoiceRepository(private val invoiceDao: InvoiceDao) {

    val getAllInvoices: LiveData<List<Invoice>> = invoiceDao.readAllInvoices()

    suspend fun insertInvoice(invoice: Invoice){
        invoiceDao.addInvoice(invoice)
    }

    suspend fun insertJob(job: Job){
        invoiceDao.addJob(job)
    }

    fun updateInvoice(invoice: Invoice){
        invoiceDao.updateInvoice(invoice)
    }

    suspend fun deleteInvoice(invoice: Invoice){
        invoiceDao.deleteInvoice(invoice)
    }

//    suspend fun getJobsWithInvoice(invoice: Invoice){
//        invoiceDao.getJobsWithInvoice(invoice.invoiceNumber)
//    }

}