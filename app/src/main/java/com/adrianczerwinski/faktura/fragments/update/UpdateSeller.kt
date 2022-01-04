package com.adrianczerwinski.faktura.fragments.update

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.adrianczerwinski.faktura.R
import com.adrianczerwinski.faktura.data.models.Seller
import com.adrianczerwinski.faktura.data.viemodels.SellerViewModel
import com.adrianczerwinski.faktura.databinding.FragmentUpdateSellerBinding
import kotlinx.coroutines.launch


class UpdateSeller : Fragment() {

    private var _binding: FragmentUpdateSellerBinding? = null
    private val binding get() = _binding!!
    private lateinit var mSellerViewModel: SellerViewModel
    private lateinit var currentSellerData: Seller


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mSellerViewModel = ViewModelProvider(this)[SellerViewModel::class.java]
        _binding = FragmentUpdateSellerBinding.inflate(inflater, container, false)



        fun inputCheck(
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

        lifecycleScope.launch {
            val myData: Seller? = mSellerViewModel.getMySellerData()
            if (myData != null) {
                binding.myEtCompname.setText(myData.name)
                binding.myEmailTxt.setText(myData.email)
                binding.myEtAddress2TxtStreet.setText(myData.streetNumber)
                binding.myEtAddressTxtStreet.setText(myData.city)
                binding.myEtPostalTxt.setText(myData.postalCode)
                binding.myPhoneTxt.setText(myData.phone)
                binding.myTaxTxt.setText(myData.taxNumber)
                binding.myIbanEt.setText(myData.iban)
                binding.myBlzEt.setText(myData.blz)
                binding.myBicEt.setText(myData.bic)

            }
        }


        fun updateSellerToDatabase() {
            val name1 = binding.myEtCompname.text.toString()
            val email1 = binding.myEmailTxt.text.toString()
            val phone1 = binding.myPhoneTxt.text.toString()
            val taxNumber1 = binding.myTaxTxt.text.toString()
            val streetNumber1 = binding.myEtAddress2TxtStreet.text.toString()
            val postalCode1 = binding.myEtPostalTxt.text.toString()
            val city1 = binding.myEtAddressTxtStreet.text.toString()
            var iban = binding.myIbanEt.text.toString()
            var blz = binding.myBlzEt.text.toString()
            var bic = binding.myBicEt.text.toString()


            if (inputCheck(name1, email1, taxNumber1, streetNumber1, postalCode1, city1)) {
                if (iban.isEmpty()){
                    iban = ""
                }
                if (blz.isEmpty()){
                    blz = ""
                }
                if (bic.isEmpty()){
                    bic = ""
                }
                // Tworzy firmę:
                val seller =
                    Seller(1, name1, email1, phone1, taxNumber1, streetNumber1, postalCode1, city1, iban, blz, bic)
                // Dodaje firmę do bazy danych na telefonie:
                mSellerViewModel.updateClient(seller)
                findNavController().navigate(R.id.action_updateSeller2_to_finances)
                Toast.makeText(requireContext(), "Uaktualniono Twoje dane.", Toast.LENGTH_LONG).show()
            } else
                Toast.makeText(
                    requireContext(),
                    "Nie wypełniono wszystkich wymaganych pól.",
                    Toast.LENGTH_LONG
                ).show()
        }

        binding.saveMyData.setOnClickListener{

            updateSellerToDatabase()

        }




        return binding.root
    }




}


