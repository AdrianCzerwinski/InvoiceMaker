package com.adrianczerwinski.invoicemaker.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.adrianczerwinski.invoicemaker.R
import com.adrianczerwinski.invoicemaker.data.models.Client
import com.adrianczerwinski.invoicemaker.data.viemodels.ClientViewModel
import com.adrianczerwinski.invoicemaker.databinding.FragmentUpdaterBinding

class UpdaterFragment : Fragment() {

    private var _binding: FragmentUpdaterBinding? = null
    private val binding get() = _binding!!
    private lateinit var mClientViewModel: ClientViewModel
    private val args by navArgs<UpdaterFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpdaterBinding.inflate(inflater, container, false)

        binding.etUpdateCity.setText(args.currentClient.city)
        binding.etUpdateCompName.setText(args.currentClient.name)
        binding.etUpdateNip.setText(args.currentClient.taxNumber)
        binding.etUpdateEmail.setText(args.currentClient.email)
        binding.etUpdatePostalCode.setText(args.currentClient.postalCode)
        binding.etUpdatePhone.setText(args.currentClient.phone)
        binding.etUpdateUlicainumer.setText(args.currentClient.streetNumber)

        mClientViewModel = ViewModelProvider(this)[ClientViewModel::class.java]

        binding.butUpdateNewClient.setOnClickListener {
            updateItem()
        }

        // Adding Menu option - Delete Single Client
        setHasOptionsMenu(true)

        return binding.root
    }

    private fun updateItem() {
        val name1 = binding.etUpdateCompName.text.toString()
        val email1 = binding.etUpdateEmail.text.toString()
        val phone1 = binding.etUpdatePhone.text.toString()
        val taxNumber1 = binding.etUpdateNip.text.toString()
        val streetNumber1 = binding.etUpdateUlicainumer.text.toString()
        val postalCode1 = binding.etUpdatePostalCode.text.toString()
        val city1 = binding.etUpdateCity.text.toString()

        if (inputCheck(name1, email1, taxNumber1, streetNumber1, postalCode1, city1)) {
            // Updating the Client:
            val updatedclient = Client(
                args.currentClient.id,
                name1,
                email1,
                phone1,
                taxNumber1,
                streetNumber1,
                postalCode1,
                city1
            )
            // Dodaje firmę do bazy danych na telefonie:
            mClientViewModel.updateClient(updatedclient)
            Toast.makeText(requireContext(), "Zaktualizowano pomyślnie.", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updaterFragment_to_clientsList)

        } else
            Toast.makeText(
                requireContext(),
                "Nie wypełniono wszystkich wymaganych pól.",
                Toast.LENGTH_LONG
            ).show()

    }

    private fun inputCheck(
        name1: String,
        email1: String,
        taxNumber1: String,
        streetNumber1: String,
        postalCode1: String,
        city1: String
    ): Boolean {
        return (!TextUtils.isEmpty(name1) && !TextUtils.isEmpty(email1) && !TextUtils.isEmpty(
            taxNumber1
        ) && !TextUtils.isEmpty(
            streetNumber1
        ) && !TextUtils.isEmpty(postalCode1) && !TextUtils.isEmpty(city1))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.bin) {
            deleteClient()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun deleteClient() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mClientViewModel.deleteClient(args.currentClient)
            findNavController().navigate(R.id.action_updaterFragment_to_clientsList)
            Toast.makeText(
                requireContext(),
                "Pomyślnie usunięto firmę ${args.currentClient.name}.",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ -> }

        builder.setTitle("Usuwanie Klienta.")
        builder.setMessage(
            "Czy na pewno chcesz usunąć firmę ${args.currentClient.name} " +
                    "z bazy klientów? "
        )
        builder.create().show()
    }


}