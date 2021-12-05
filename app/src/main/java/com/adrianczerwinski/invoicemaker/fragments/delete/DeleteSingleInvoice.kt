package com.adrianczerwinski.invoicemaker.fragments.delete

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.adrianczerwinski.invoicemaker.R
import com.adrianczerwinski.invoicemaker.data.viemodels.ClientViewModel
import com.adrianczerwinski.invoicemaker.data.viemodels.InvoiceViewModel
import com.adrianczerwinski.invoicemaker.databinding.FragmentDeleteSingleClientBinding


class DeleteSingleInvoice : Fragment() {

    private var _binding: FragmentDeleteSingleClientBinding? = null
    private val binding get() = _binding!!
    private lateinit var mInvoiceViewModel: InvoiceViewModel
    private val args by navArgs<DeleteSingleInvoiceArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDeleteSingleClientBinding.inflate(inflater, container, false)
        mInvoiceViewModel = ViewModelProvider(this)[InvoiceViewModel::class.java]

        deleteInvoice()

        return binding.root
    }

    private fun deleteInvoice() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Tak") { _, _ ->
            mInvoiceViewModel.deleteInvoice(args.currentInvoice)
            findNavController().navigate(R.id.action_deleteSingleInvoice_to_invoicesList)
            Toast.makeText(
                requireContext(),
                "Pomyślnie usunięto fakturę ${args.currentInvoice.invoiceNumber}.",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("Nie") { _, _ ->
            findNavController().navigate(R.id.action_deleteSingleInvoice_to_invoicesList)
        }

        builder.setTitle("Usuwanie Faktury.")
        builder.setMessage(
            "Czy na pewno chcesz usunąć fakturę ${args.currentInvoice.invoiceNumber} ?"
        )
        builder.create().show()
    }
}