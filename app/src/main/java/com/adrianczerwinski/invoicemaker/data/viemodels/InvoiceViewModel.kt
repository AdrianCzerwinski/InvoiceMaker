package com.adrianczerwinski.invoicemaker.data.viemodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.adrianczerwinski.invoicemaker.data.InvoicesDatabase
import com.adrianczerwinski.invoicemaker.data.repository.ClientRepository
import com.adrianczerwinski.invoicemaker.data.models.Client
import com.adrianczerwinski.invoicemaker.data.models.Invoice
import com.adrianczerwinski.invoicemaker.data.models.Job
import com.adrianczerwinski.invoicemaker.data.repository.InvoiceRepository
import com.adrianczerwinski.invoicemaker.data.ClientDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InvoiceViewModel(application: Application): AndroidViewModel(application) {
    val getAllInvoices: LiveData<List<Invoice>>
    private val repository: InvoiceRepository

    init {
        val invoiceDao = InvoicesDatabase.getDatabase(application).invoiceDao()
        repository = InvoiceRepository(invoiceDao)
        getAllInvoices = repository.getAllInvoices

    }

    fun insertInvoice(invoice: Invoice){
        viewModelScope.launch(Dispatchers.IO){
            repository.insertInvoice(invoice)
        }
    }

    fun insertJob(job: Job){
        viewModelScope.launch(Dispatchers.IO){
            repository.insertJob(job)
        }
    }

    fun updateInvoice(invoice: Invoice) {
        viewModelScope.launch(Dispatchers.IO){
            repository.updateInvoice(invoice)
        }
    }

    fun deleteInvoice(invoice: Invoice){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteInvoice(invoice)
        }
    }

    fun getInvoiceWithJobs(invoice: Invoice){
        viewModelScope.launch(Dispatchers.IO){
            repository.getInvoiceWithJobs(invoice)
        }
    }




}