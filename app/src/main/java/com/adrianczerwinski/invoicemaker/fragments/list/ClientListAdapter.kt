package com.adrianczerwinski.invoicemaker.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.adrianczerwinski.invoicemaker.data.models.Client
import com.adrianczerwinski.invoicemaker.databinding.RowClientBinding
import com.adrianczerwinski.invoicemaker.fragments.newinvoice.MyClient

class ClientListAdapter :
    RecyclerView.Adapter<ClientListAdapter.MyViewHolder>() {

    private var clientList = emptyList<Client>()

    inner class MyViewHolder(val binding: RowClientBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            RowClientBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return clientList.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = clientList[position]

        holder.binding.listName.text = clientList[position].name
        holder.binding.listNip.text = clientList[position].taxNumber

        holder.binding.btEdit.setOnClickListener {
            val action = ClientsListDirections.actionClientsListToUpdaterFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }

        holder.binding.btDeleteSingle.setOnClickListener {
            val action = ClientsListDirections.actionClientsListToDeleteSingleClient(currentItem)
            holder.itemView.findNavController().navigate(action)

        }

        holder.binding.listName.setOnClickListener {
            val action = ClientsListDirections.actionClientsList2ToNewInvoice()
            holder.itemView.findNavController().navigate(action)
            MyClient.id = currentItem.id
            MyClient.name = currentItem.name
            MyClient.city = currentItem.city
            MyClient.postalCode = currentItem.postalCode
            MyClient.streetNumber = currentItem.streetNumber
            MyClient.taxNumber = currentItem.taxNumber
            MyClient.phone = currentItem.phone
            MyClient.email = currentItem.email
        }

        holder.binding.listNip.setOnClickListener {
            val action = ClientsListDirections.actionClientsList2ToNewInvoice()
            holder.itemView.findNavController().navigate(action)
            MyClient.name = currentItem.name
            MyClient.city = currentItem.city
            MyClient.postalCode = currentItem.postalCode
            MyClient.streetNumber = currentItem.streetNumber
            MyClient.taxNumber = currentItem.taxNumber
            MyClient.phone = currentItem.phone
            MyClient.email = currentItem.email


        }
    }

    fun setData(client: List<Client>) {
        this.clientList = client
        notifyDataSetChanged()
    }

}