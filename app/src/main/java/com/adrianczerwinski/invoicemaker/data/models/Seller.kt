package com.adrianczerwinski.invoicemaker.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "seller_table")
@Parcelize
data class Seller (
    @PrimaryKey
    var id: Int,
    var name: String,
    var email: String,
    var phone: String,
    var taxNumber: String,
    var streetNumber: String,
    var postalCode: String,
    var city: String,
    var iban: String,
    var blz: String,
    var bic: String
) : Parcelable