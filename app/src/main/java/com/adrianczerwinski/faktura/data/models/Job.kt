package com.adrianczerwinski.faktura.data.models

import android.os.Parcelable
import android.text.Editable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Entity(tableName = "jobs_table")
@Parcelize
data class Job(
    @PrimaryKey(autoGenerate = true)
    val jobId: Int,
    val jobName: String,
    val invoiceNumber: String,
    val price: Double,
    val quantity: Int,
    val unit: String
) :Parcelable