package com.adrianczerwinski.invoicemaker.fragments.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adrianczerwinski.invoicemaker.data.models.Invoice
import com.adrianczerwinski.invoicemaker.databinding.RowClientBinding
import com.adrianczerwinski.invoicemaker.databinding.RowInvoiceBinding

class InvoiceListAdaptor: RecyclerView.Adapter<InvoiceListAdaptor.MyViewHolder>() {

    private var invoiceList = emptyList<Invoice>()

    class MyViewHolder(val binding: RowInvoiceBinding) : RecyclerView.ViewHolder(binding.root) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            RowInvoiceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return invoiceList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = invoiceList[position]
        holder.binding.listClientInvoice.text = currentItem.clientName.name
        holder.binding.listInvoiceNumber.text = currentItem.invoiceNumber
        holder.binding.listTotalPrice.text = "${currentItem.sum}  ${currentItem.currency}"
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(invoice: List<Invoice>) {
        this.invoiceList = invoice
        notifyDataSetChanged()
    }


}