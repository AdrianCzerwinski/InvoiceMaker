package com.adrianczerwinski.invoicemaker.fragments.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.adrianczerwinski.invoicemaker.data.viemodels.InvoiceViewModel

import com.adrianczerwinski.invoicemaker.databinding.FragmentInvoicesListBinding

class InvoicesList : Fragment() {

    private var _binding: FragmentInvoicesListBinding? = null
    private val binding get() = _binding!!
    private lateinit var mInvoiceVieModel: InvoiceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInvoicesListBinding.inflate(inflater, container, false)

        // RecyclerView
        val adapter = InvoiceListAdaptor()
        val recyclerView = binding.recyclerInvoicesList
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Client View Model
        mInvoiceVieModel = ViewModelProvider(this)[InvoiceViewModel::class.java]
        mInvoiceVieModel.getAllInvoices.observe(viewLifecycleOwner, { invoice ->
            adapter.setData(invoice)
        })

        // Adding Menu option - Delete All Clients
        setHasOptionsMenu(true)


        return binding.root
    }

}