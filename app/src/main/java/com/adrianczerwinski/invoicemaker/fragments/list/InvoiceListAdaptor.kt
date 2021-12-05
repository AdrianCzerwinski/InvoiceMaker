package com.adrianczerwinski.invoicemaker.fragments.list

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.adrianczerwinski.invoicemaker.InvoiceToPdf
import com.adrianczerwinski.invoicemaker.data.models.Invoice
import com.adrianczerwinski.invoicemaker.databinding.RowInvoiceBinding
import com.adrianczerwinski.invoicemaker.fragments.newinvoice.MyClient

class InvoiceListAdaptor: RecyclerView.Adapter<InvoiceListAdaptor.MyViewHolder>() {

    private var invoiceList = listOf<Invoice>()

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
        holder.binding.listTotalPrice.text = currentItem.sum.toString()
        holder.binding.listCurrency.text = currentItem.currency
        holder.binding.btDeleteSingle.setOnClickListener{
            val action = InvoicesListDirections.actionInvoicesListToDeleteSingleInvoice(currentItem)
            holder.itemView.findNavController().navigate(action)
        }
        holder.binding.rowBackground.setOnClickListener {

            val activity = holder.itemView.context as Activity
            val intent = Intent(activity, InvoiceToPdf::class.java).apply {
                putExtra("InvoiceNumber", currentItem.invoiceNumber)
                putExtra("ProjectName", currentItem.projectName)
            }
            MyClient.email = currentItem.clientName.email
            activity.startActivity(intent)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(invoice: List<Invoice>) {
        this.invoiceList = invoice
        notifyDataSetChanged()
    }


}

