package com.adrianczerwinski.invoicemaker.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
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
        _binding = FragmentUpdaterBinding.inflate(inflater, container, false)

        binding.etCity2.setText(args.currentClient.city)
        binding.etCompName.setText(args.currentClient.name)
        binding.etNip2.setText(args.currentClient.taxNumber)
        binding.etEmail2.setText(args.currentClient.email)
        binding.etPostalCode2.setText(args.currentClient.postalCode)
        binding.etPhone2.setText(args.currentClient.phone)
        binding.etUlicainumer2.setText(args.currentClient.streetNumber)

        mClientViewModel = ViewModelProvider(this)[ClientViewModel::class.java]

        binding.butConfirmNewClient.setOnClickListener {
            updateItem()
        }

        return binding.root
    }

    private fun updateItem() {
        val name1 = binding.etCompName.text.toString()
        val email1 = binding.etEmail2.text.toString()
        val phone1 = binding.etPhone2.text.toString()
        val taxNumber1 = binding.etNip2.text.toString()
        val streetNumber1 = binding.etUlicainumer2.text.toString()
        val postalCode1 = binding.etPostalCode2.text.toString()
        val city1 = binding.etCity2.text.toString()
        val language1 = "DE"

        if (inputCheck(name1, email1, taxNumber1, streetNumber1, postalCode1, city1, language1)) {
            // Updating the Client:
            val updatedclient = Client(
                args.currentClient.id,
                name1,
                email1,
                phone1,
                taxNumber1,
                streetNumber1,
                postalCode1,
                city1,
                language1
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
        city1: String,
        language1: String
    ): Boolean {
        return (!TextUtils.isEmpty(name1) && !TextUtils.isEmpty(email1) && !TextUtils.isEmpty(
            taxNumber1
        ) && !TextUtils.isEmpty(
            streetNumber1
        ) && !TextUtils.isEmpty(postalCode1) && !TextUtils.isEmpty(city1) && !TextUtils.isEmpty(
            language1
        ))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }



}