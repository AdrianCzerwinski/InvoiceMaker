package com.adrianczerwinski.invoicemaker.data.models

import android.os.Parcelable
import android.text.Editable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "invoices_table")
@Parcelize
data class Invoice(
    @PrimaryKey(autoGenerate = true)
    var invoiceId: Int,
    var invoiceNumber: String,
    var cells: Int,
    var sum: Double,
    var currency: String,
    var language: String,
    @Embedded
    var clientName: Client,

    ) : Parcelable

