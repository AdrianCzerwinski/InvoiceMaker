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
import com.adrianczerwinski.invoicemaker.databinding.FragmentDeleteSingleClientBinding


class DeleteSingleClient : Fragment() {

    private var _binding: FragmentDeleteSingleClientBinding? = null
    private val binding get() = _binding!!
    private lateinit var mClientViewModel: ClientViewModel
    private val args by navArgs<com.adrianczerwinski.invoicemaker.fragments.delete.DeleteSingleClientArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDeleteSingleClientBinding.inflate(inflater, container, false)
        mClientViewModel = ViewModelProvider(this).get(ClientViewModel::class.java)

        deleteClient()

        return binding.root
    }

    private fun deleteClient() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mClientViewModel.deleteClient(args.currentClient)
            findNavController().navigate(R.id.action_deleteSingleClient_to_clientsList)
            Toast.makeText(
                requireContext(),
                "Pomyślnie usunięto firmę ${args.currentClient.name}.",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ ->
            findNavController().navigate((R.id.action_deleteSingleClient_to_clientsList))
        }

        builder.setTitle("Usuwanie Klienta.")
        builder.setMessage(
            "Czy na pewno chcesz usunąć firmę ${args.currentClient.name} " +
                    "z bazy klientów? "
        )
        builder.create().show()
    }
}