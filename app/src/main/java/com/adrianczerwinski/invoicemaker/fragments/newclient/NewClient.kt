package com.adrianczerwinski.invoicemaker.fragments.newclient

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.adrianczerwinski.invoicemaker.R
import com.adrianczerwinski.invoicemaker.data.viemodels.ClientViewModel
import com.adrianczerwinski.invoicemaker.data.models.Client
import com.adrianczerwinski.invoicemaker.databinding.FragmentNewClientBinding
import org.w3c.dom.Text

class NewClient : Fragment() {

    private var _binding: FragmentNewClientBinding? = null
    private lateinit var mClientViewModel: ClientViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNewClientBinding.inflate(inflater, container, false)
        mClientViewModel = ViewModelProvider(this)[ClientViewModel::class.java]
        binding.butConfirmNewClient.setOnClickListener {
            insertDataToDatabase()
        }

        return binding.root
    }

    private fun insertDataToDatabase() {
        val name1 = binding.etCompName.text.toString()
        val email1 = binding.etEmail.text.toString()
        val phone1 = binding.etPhone.text.toString()
        val taxNumber1 = binding.etNip.text.toString()
        val streetNumber1 = binding.etUlicainumer.text.toString()
        val postalCode1 = binding.etPostalCode.text.toString()
        val city1 = binding.etCity.text.toString()

        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (email1.isEmpty()) binding.warnEmail.isVisible = true
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.warnEmail.text = "Niepoprawny e-mail"
                for (i: Char in binding.etEmail.text) {
                    if (i == '@') {
                        binding.warnEmail.text = "Poprawny e-mail"
                        binding.warnEmail.isVisible = true
                    }
                }
                binding.warnEmail.visibility = TextView.VISIBLE
            }


        })

        if (inputCheck(name1, email1, taxNumber1, streetNumber1, postalCode1, city1)) {
            // Tworzy firmę:
            val client =
                Client(0, name1, email1, phone1, taxNumber1, streetNumber1, postalCode1, city1)
            // Dodaje firmę do bazy danych na telefonie:
            mClientViewModel.insertClient(client)
            Toast.makeText(requireContext(), "Dodano nowego klienta.", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_newClient_to_clientsList)
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


}


