package com.adrianczerwinski.faktura.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.adrianczerwinski.faktura.data.models.Invoice
import com.adrianczerwinski.faktura.data.models.Job

data class InvoiceWithJobs (
    @Embedded val invoice: Invoice,
    @Relation(
        parentColumn = "invoiceNumber",
        entityColumn = "invoiceNumber"
    )
    val jobs: List<Job>


    )