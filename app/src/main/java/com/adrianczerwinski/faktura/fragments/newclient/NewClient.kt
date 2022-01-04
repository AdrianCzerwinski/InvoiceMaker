package com.adrianczerwinski.faktura.fragments.newclient

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
import com.adrianczerwinski.faktura.R
import com.adrianczerwinski.faktura.data.viemodels.ClientViewModel
import com.adrianczerwinski.faktura.data.models.Client
import com.adrianczerwinski.faktura.databinding.FragmentNewClientBinding

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
        val email1 = binding.etEmail2.text.toString()
        val phone1 = binding.etPhone2.text.toString()
        val taxNumber1 = binding.etNip2.text.toString()
        val streetNumber1 = binding.etUlicainumer2.text.toString()
        val postalCode1 = binding.etPostalCode2.text.toString()
        val city1 = binding.etCity2.text.toString()

        binding.etEmail2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (email1.isEmpty()) {binding.warnEmail.isVisible = true}
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.warnEmail.text = "Niepoprawny e-mail"
                binding.etEmail2.text!!.forEach { i: Char ->
                    if (i == '@') {
                        binding.warnEmail.text = "Poprawny e-mail"
                        binding.warnEmail.isVisible = true
                    }
                }
                binding.warnEmail.visibility = TextView.VISIBLE
            }


        })

        if (inputCheck(name1, email1, taxNumber1, streetNumber1, postalCode1, city1)) {

            if (emailCheck(binding.etEmail2.text.toString())) {
                // Tworzy firmę:
                val client =
                    Client(0, name1, email1, phone1, taxNumber1, streetNumber1, postalCode1, city1, "DE")
                // Dodaje firmę do bazy danych na telefonie:
                mClientViewModel.insertClient(client)
                Toast.makeText(requireContext(), "Dodano nowego klienta.", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_newClient_to_clientsList)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Niepoprawny email!",
                    Toast.LENGTH_SHORT
                ).show()

            }

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

    private fun emailCheck (email: String): Boolean {
        var wrongEmail = false
        email.forEach { i: Char ->
            if (i == '@') {
                wrongEmail = true
            }
        }
        return wrongEmail

    }


}


