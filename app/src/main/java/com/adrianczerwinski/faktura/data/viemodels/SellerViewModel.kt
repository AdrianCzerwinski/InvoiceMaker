package com.adrianczerwinski.faktura.data.viemodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.adrianczerwinski.faktura.data.SellerDatabase
import com.adrianczerwinski.faktura.data.models.Seller
import com.adrianczerwinski.faktura.data.repository.SellerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SellerViewModel(application: Application) : AndroidViewModel(application) {
    val getMyData: LiveData<List<Seller>>
    private val repository: SellerRepository

    init {
        val sellerDao = SellerDatabase.getDatabase(application).sellerDao()
        repository = SellerRepository(sellerDao)
        getMyData = repository.getMyData

    }

    fun putMyData(seller: Seller) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.putMyData(seller)
        }
    }

    suspend fun getMySellerData(): Seller? {
        return repository.getMySellerData()
    }


    fun updateClient(seller: Seller) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMyData(seller)
        }
    }


}