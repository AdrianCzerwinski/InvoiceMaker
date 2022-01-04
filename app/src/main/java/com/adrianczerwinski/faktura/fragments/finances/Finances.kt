package com.adrianczerwinski.faktura.fragments.finances


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.adrianczerwinski.faktura.R
import com.adrianczerwinski.faktura.data.models.Seller
import com.adrianczerwinski.faktura.data.viemodels.SellerViewModel
import com.adrianczerwinski.faktura.databinding.FragmentFinancesBinding
import kotlinx.coroutines.launch

class Finances : Fragment() {

    private var _binding: FragmentFinancesBinding? = null
    private val binding get() = _binding!!
    private lateinit var mSellerViewModel: SellerViewModel



    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        mSellerViewModel = ViewModelProvider(this)[SellerViewModel::class.java]

        lifecycleScope.launch {
            val myData: Seller? = mSellerViewModel.getMySellerData()
            if (myData != null) {
                binding.viewCompanyName.text = myData.name
                binding.viewCompanyAddress.text = "${myData.city} ${myData.postalCode}, ${myData.streetNumber}"
                binding.viewCompanyPhoneMail.text = "${myData.phone} || ${myData.email}"
                binding.viewCompanyTaxNumber.text = "VAT: ${myData.taxNumber}"
            }
        }




        _binding = FragmentFinancesBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        binding.butCreateFV.setOnClickListener {
            findNavController().navigate(R.id.action_finances_to_newInvoice2)
        }
        binding.butShowFV.setOnClickListener{
            findNavController().navigate(R.id.action_finances_to_invoicesList)
        }

        binding.editMyData.setOnClickListener {
            findNavController().navigate(R.id.action_finances_to_updateSeller2)
        }

        return binding.root

    }


}