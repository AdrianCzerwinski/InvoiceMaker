package com.adrianczerwinski.faktura.fragments.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrianczerwinski.faktura.R

import com.adrianczerwinski.faktura.data.viemodels.InvoiceViewModel

import com.adrianczerwinski.faktura.databinding.FragmentInvoicesListBinding

class InvoicesList : Fragment() {

    private var _binding: FragmentInvoicesListBinding? = null
    private val binding get() = _binding!!
    private lateinit var mInvoiceVieModel: InvoiceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInvoicesListBinding.inflate(inflater, container, false)

        val backpress = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, true) {
            findNavController().navigate(R.id.action_invoicesList_to_finances)
        }
        backpress.isEnabled = true

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