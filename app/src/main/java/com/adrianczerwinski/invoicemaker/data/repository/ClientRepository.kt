package com.adrianczerwinski.invoicemaker.data.repository

import androidx.lifecycle.LiveData
import com.adrianczerwinski.invoicemaker.data.ClientsDao
import com.adrianczerwinski.invoicemaker.data.models.Client


class ClientRepository(private val clientsDao: ClientsDao) {
    val getAllClients: LiveData<List<Client>> = clientsDao.getAllClients()

    suspend fun insertClient(client: Client){
        clientsDao.insertClient(client)
    }

    fun updateClient(client: Client){
        clientsDao.updateClient(client)
    }

    suspend fun deleteClient(client: Client){
        clientsDao.deleteClient(client)
    }

    suspend fun deleteAllClients(){
        clientsDao.deleteAllClients()
    }


}