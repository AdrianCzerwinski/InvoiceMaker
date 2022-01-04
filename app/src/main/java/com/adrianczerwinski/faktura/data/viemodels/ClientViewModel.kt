package com.adrianczerwinski.faktura.data.viemodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.adrianczerwinski.faktura.data.repository.ClientRepository
import com.adrianczerwinski.faktura.data.models.Client
import com.adrianczerwinski.faktura.data.ClientDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClientViewModel(application: Application): AndroidViewModel(application) {
    val getAllClients: LiveData<List<Client>>
    private val repository: ClientRepository

    init {
        val clientsDao = ClientDatabase.getDatabase(application).clientsDao()
        repository = ClientRepository(clientsDao)
        getAllClients = repository.getAllClients

    }

    fun insertClient(client: Client){
        viewModelScope.launch(Dispatchers.IO){
            repository.insertClient(client)
        }
    }

    fun updateClient(client: Client) {
        viewModelScope.launch(Dispatchers.IO){
            repository.updateClient(client)
        }
    }

    fun deleteClient(client: Client){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteClient(client)
        }
    }

    fun deleteAllClients(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllClients()
        }
    }


}