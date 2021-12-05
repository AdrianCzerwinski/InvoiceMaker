package com.adrianczerwinski.invoicemaker.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.adrianczerwinski.invoicemaker.data.models.Invoice
import com.adrianczerwinski.invoicemaker.data.models.Job

data class InvoiceWithJobs (
    @Embedded val invoice: Invoice,
    @Relation(
        parentColumn = "invoiceNumber",
        entityColumn = "invoiceNumber"
    )
    val jobs: List<Job>


    )