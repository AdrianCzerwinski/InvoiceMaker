package com.adrianczerwinski.invoicemaker.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrianczerwinski.invoicemaker.R
import com.adrianczerwinski.invoicemaker.data.viemodels.ClientViewModel
import com.adrianczerwinski.invoicemaker.databinding.FragmentClientsListBinding

class ClientsList : Fragment() {

    private var _binding: FragmentClientsListBinding? = null
    private val binding get() = _binding!!
    private lateinit var mClientViewModel: ClientViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflating the layout:
        _binding = FragmentClientsListBinding.inflate(inflater, container, false)

        val backpress = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, true) {
            findNavController().navigate(R.id.action_clientsList_to_newInvoice)
        }
        backpress.isEnabled = true

        binding.buttonNewClient.setOnClickListener {
            findNavController().navigate(R.id.action_clientsList_to_newClient)
        }

//        val onBackPressedCallback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                findNavController().navigate(R.id.action_clientsList2_to_newInvoice)
//            }
//        }
//        requireActivity().getOnBackPressedDispatcher().addCallback(viewLifecycleOwner, onBackPressedCallback)

        // Setting RecyclerView
        val adapter = ClientListAdapter()
        val recyclerView = binding.recyclerClientsList
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Client View Model
        mClientViewModel = ViewModelProvider(this)[ClientViewModel::class.java]
        mClientViewModel.getAllClients.observe(viewLifecycleOwner, { client ->
            adapter.setData(client)
        })

        // Adding Menu option - Delete All Clients
        setHasOptionsMenu(true)


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.bin) {
            deleteAll()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun deleteAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mClientViewModel.deleteAllClients()
            Toast.makeText(
                requireContext(),
                "Pomyślnie usunięto wszystkich klientów.",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Czyszczenie bazy firm.")
        builder.setMessage("Czy na pewno chcesz usunąć wszystkich Klientów?")
        builder.create().show()

    }

}