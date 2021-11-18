package com.adrianczerwinski.invoicemaker.data.repository

import androidx.lifecycle.LiveData
import com.adrianczerwinski.invoicemaker.data.ClientsDao
import com.adrianczerwinski.invoicemaker.data.SellerDao
import com.adrianczerwinski.invoicemaker.data.models.Client
import com.adrianczerwinski.invoicemaker.data.models.Seller


class SellerRepository(private val sellerDao: SellerDao) {
    val getMyData: LiveData<List<Seller>> = sellerDao.getMyData()

    suspend fun putMyData(seller: Seller){
        sellerDao.putMyData(seller)
    }

    fun updateMyData(seller: Seller){
        sellerDao.updateMyData(seller)
    }

    suspend fun getMySellerData() :Seller? {
        return sellerDao.getMyDataSpec()
    }



}