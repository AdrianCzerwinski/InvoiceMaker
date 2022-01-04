package com.adrianczerwinski.faktura.fragments.delete

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
import com.adrianczerwinski.faktura.R
import com.adrianczerwinski.faktura.data.viemodels.ClientViewModel
import com.adrianczerwinski.faktura.databinding.FragmentDeleteSingleClientBinding


class DeleteSingleClient : Fragment() {

    private var _binding: FragmentDeleteSingleClientBinding? = null
    private val binding get() = _binding!!
    private lateinit var mClientViewModel: ClientViewModel
    private val args by navArgs<DeleteSingleClientArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDeleteSingleClientBinding.inflate(inflater, container, false)
        mClientViewModel = ViewModelProvider(this)[ClientViewModel::class.java]

        deleteClient()

        return binding.root
    }

    private fun deleteClient() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Tak") { _, _ ->
            mClientViewModel.deleteClient(args.currentClient)
            findNavController().navigate(R.id.action_deleteSingleClient_to_clientsList)
            Toast.makeText(
                requireContext(),
                "Pomyślnie usunięto klienta ${args.currentClient.name}.",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("Nie") { _, _ ->
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