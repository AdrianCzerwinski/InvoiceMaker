package com.adrianczerwinski.faktura.data


import androidx.lifecycle.LiveData
import androidx.room.*
import com.adrianczerwinski.faktura.data.models.Invoice
import com.adrianczerwinski.faktura.data.models.Seller





@Dao
interface SellerDao {

    @Query("SELECT * FROM seller_table ORDER BY id ASC")
    fun getMyData(): LiveData<List<Seller>>

    @Query("SELECT * FROM seller_table WHERE id=1 ")
    suspend fun getMyDataSpec(): Seller?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun putMyData(SellerData: Seller)

    @Update()
    fun updateMyData(seller: Seller)


}


