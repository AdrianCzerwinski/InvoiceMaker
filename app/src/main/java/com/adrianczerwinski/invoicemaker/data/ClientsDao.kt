package com.adrianczerwinski.invoicemaker.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.adrianczerwinski.invoicemaker.data.models.Client


@Dao
interface ClientsDao {

    @Query("SELECT * FROM client_table ORDER BY id ASC")
    fun getAllClients(): LiveData<List<Client>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertClient(ClientData: Client)

    @Update()
    fun updateClient(clients: Client)

    @Delete
    suspend fun deleteClient(clients: Client)

    @Query("DELETE FROM client_table")
    suspend fun deleteAllClients()


}


