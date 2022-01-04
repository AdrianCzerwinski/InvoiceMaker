package com.adrianczerwinski.faktura.data.models

import android.os.Parcelable
import android.text.Editable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "invoices_table")
@Parcelize
data class Invoice(
    @PrimaryKey(autoGenerate = false)
    var invoiceNumber: String,
    var cells: Int,
    var sum: Double,
    var currency: String,
    val vat: Int,
    @Embedded
    var clientName: Client,
    var projectName: String,
    var payingTime: String,
    var date: String

    ) : Parcelable

