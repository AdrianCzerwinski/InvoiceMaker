package com.adrianczerwinski.faktura

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.adrianczerwinski.faktura.data.models.Seller
import com.adrianczerwinski.faktura.data.viemodels.SellerViewModel
import com.adrianczerwinski.faktura.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var mSellerViewModel: SellerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mSellerViewModel = ViewModelProvider(this)[SellerViewModel::class.java]

        val preferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
        var firstTime = preferences.getString("FirstTimeInstall", "")
        var name = preferences.getString("Name", "")

        val backpress = this.onBackPressedDispatcher.addCallback(this, true) {
            finish()
        }
        backpress.isEnabled = true



        if (firstTime == "YES" && name != "") {
            binding.welcome.isVisible = false
            binding.saveMyData.isVisible = false
            binding.navHostFragment.isVisible = true
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.navHostFragment) as NavHostFragment
            navController = navHostFragment.navController


        } else {
            val editor = preferences.edit()
            editor.putString("FirstTimeInstall", "YES")
            editor.apply()

            binding.saveMyData.setOnClickListener {
                insertSellerToDatabase()
                editor.putString("Name", binding.myCompName.toString())
                editor.apply()
                binding.welcome.isVisible = false
                binding.navHostFragment.isVisible = true
                binding.saveMyData.isVisible = false
            }

        }


    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun insertSellerToDatabase() {
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
            val seller =
                Seller(1, name1, email1, phone1, taxNumber1, streetNumber1, postalCode1, city1, iban, blz, bic)
            // Dodaje firm?? do bazy danych na telefonie:
            mSellerViewModel.putMyData(seller)
            Toast.makeText(this, "Pomy??lnie dodano Twoje dane.", Toast.LENGTH_LONG).show()
        } else
            Toast.makeText(
                this,
                "Nie wype??niono wszystkich wymaganych p??l.",
                Toast.LENGTH_LONG
            ).show()


    }


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

}