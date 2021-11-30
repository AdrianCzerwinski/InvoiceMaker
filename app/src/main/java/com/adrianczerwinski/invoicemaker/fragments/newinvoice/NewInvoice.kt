package com.adrianczerwinski.invoicemaker.fragments.newinvoice

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.room.PrimaryKey
import com.adrianczerwinski.invoicemaker.InvoiceToPdf
import com.adrianczerwinski.invoicemaker.R
import com.adrianczerwinski.invoicemaker.data.models.Client
import com.adrianczerwinski.invoicemaker.data.models.Invoice
import com.adrianczerwinski.invoicemaker.data.models.Job
import com.adrianczerwinski.invoicemaker.data.models.Seller
import com.adrianczerwinski.invoicemaker.data.viemodels.InvoiceViewModel
import com.adrianczerwinski.invoicemaker.data.viemodels.SellerViewModel
import com.adrianczerwinski.invoicemaker.databinding.FragmentNewInvoiceBinding
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize


class NewInvoice : Fragment() {

    private var _binding: FragmentNewInvoiceBinding? = null
    private val binding get() = _binding!!
    var jobsCount = 1
    var sum: Double = 0.00
    var vat = 100
    var job2 = Job(0, "default", "default", 0.0, 0, "default")
    var job3 = Job(0, "default", "default", 0.0, 0, "default")
    var job4 = Job(0, "default", "default", 0.0, 0, "default")
    var job5 = Job(0, "default", "default", 0.0, 0, "default")
    var job6 = Job(0, "default", "default", 0.0, 0, "default")
    var job7 = Job(0, "default", "default", 0.0, 0, "default")
    var job8 = Job(0, "default", "default", 0.0, 0, "default")
    var job9 = Job(0, "default", "default", 0.0, 0, "default")
    var job10 = Job(0, "default", "default", 0.0, 0, "default")
    private lateinit var job1: Job
    private lateinit var mInvoiceViewModel: InvoiceViewModel
    private lateinit var mSellerViewModel: SellerViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewInvoiceBinding.inflate(inflater, container, false)
        mInvoiceViewModel = ViewModelProvider(this)[InvoiceViewModel::class.java]
        mSellerViewModel = ViewModelProvider(this)[SellerViewModel::class.java]
        binding.chooseClientName.text = MyClient.name

        val units = resources.getStringArray(R.array.units)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdonw_item, units)
        val currencies = resources.getStringArray(R.array.currencies)
        val languages = resources.getStringArray(R.array.languages)
        val arrayAdapterCurrency =
            ArrayAdapter(requireContext(), R.layout.dropdonw_item_currency, currencies)
        val arrayAdapterLanguage =
            ArrayAdapter(requireContext(), R.layout.dropdonw_item_languages, languages)
        binding.btVat.setAdapter(arrayAdapterLanguage)
        binding.btChooseCurrency.setAdapter(arrayAdapterCurrency)
        binding.unitset1.setAdapter(arrayAdapter)
        binding.unitset2.setAdapter(arrayAdapter)
        binding.unitset3.setAdapter(arrayAdapter)
        binding.unitset4.setAdapter(arrayAdapter)
        binding.unitset5.setAdapter(arrayAdapter)
        binding.unitset6.setAdapter(arrayAdapter)
        binding.unitset7.setAdapter(arrayAdapter)
        binding.unitset8.setAdapter(arrayAdapter)
        binding.unitset9.setAdapter(arrayAdapter)
        binding.unitset10.setAdapter(arrayAdapter)

        lateinit var companySell: String
        lateinit var addressSell: String
        lateinit var contactSell: String
        lateinit var taxSell: String

        lifecycleScope.launch {
            val myData: Seller? = mSellerViewModel.getMySellerData()
            if (myData != null) {
                companySell = myData.name
                addressSell = "${myData.city} ${myData.postalCode}, ${myData.streetNumber}"
                contactSell = "${myData.phone} || ${myData.email}"
                taxSell = "Stuernummer: ${myData.taxNumber}"
            }
        }

        binding.chooseClientName.setOnClickListener {
            findNavController().navigate(R.id.action_newInvoice_to_clientsList)
        }


        binding.buttonGenerateInvoice.setOnClickListener {
            if (binding.invoiceNumber.text.toString() != "Numer Faktury" &&
                binding.invoiceNumber.text.toString().isNotEmpty()
            ) {
                vat = binding.btVat.text.toString().toInt()

                if (vat == 100) {
                    Toast.makeText(context, "Wybierz wartość podatku.", Toast.LENGTH_SHORT).show()
                } else
                {


                    assignValues()

                if (MyClient.name != "Clients Name" &&
                    binding.jobname1.text!!.isNotEmpty() &&
                    binding.price1.text.isNotEmpty() &&
                    binding.amount1.text.isNotEmpty() &&
                    binding.unitset1.text.isNotEmpty()
                ) {
                    val intent = Intent(activity, InvoiceToPdf::class.java)


                    val data: MutableList<Job> = ArrayList()

                    data.add(job1)
                    if (job2.jobName.isNotEmpty()) {
                        data.add(job2)
                    }
                    if (job3.jobName.isNotEmpty()) {
                        data.add(job3)
                    }
                    if (job4.jobName.isNotEmpty()) {
                        data.add(job4)
                    }
                    if (job5.jobName.isNotEmpty()) {
                        data.add(job5)
                    }
                    if (job6.jobName.isNotEmpty()) {
                        data.add(job6)
                    }
                    if (job7.jobName.isNotEmpty()) {
                        data.add(job7)
                    }
                    if (job8.jobName.isNotEmpty()) {
                        data.add(job8)
                    }
                    if (job9.jobName.isNotEmpty()) {
                        data.add(job9)
                    }
                    if (job10.jobName.isNotEmpty()) {
                        data.add(job10)
                    }

                    intent.putExtra("jobsCount", jobsCount)
                    intent.putExtra("sellerName", companySell)
                    intent.putExtra("sellerAddress", addressSell)
                    intent.putExtra("sellerTaxNumber", taxSell)
                    intent.putExtra("sellerContactData", contactSell)
                    intent.putExtra("theSum", sum)
                    intent.putExtra("VAT", binding.btVat.text.toString().toInt())
                    intent.putExtra("Currency", binding.btChooseCurrency.text.toString())
                    intent.putParcelableArrayListExtra(
                        "data",
                        data as java.util.ArrayList<out Parcelable>
                    )

                    startActivity(intent)

                }
            }
            } else Toast.makeText(context, "Wpisz nr faktury.", Toast.LENGTH_LONG).show()


        }

        binding.addLine.setOnClickListener {
            createLine()
        }


        return binding.root
    }

    private fun createLine() {
        jobsCount++

        when (jobsCount) {
            2 -> binding.job2.isVisible = true
            3 -> binding.job3.isVisible = true
            4 -> binding.job4.isVisible = true
            5 -> binding.job5.isVisible = true
            6 -> binding.job6.isVisible = true
            7 -> binding.job7.isVisible = true
            8 -> binding.job8.isVisible = true
            9 -> binding.job9.isVisible = true
            10 -> binding.job10.isVisible = true
            else -> Toast.makeText(context, "Maksymalnie 10 pozycji.", Toast.LENGTH_LONG).show()

        }
    }

    private fun assignValues() {
        vat = binding.btVat.text.toString().toInt()

        if (binding.jobname1.text!!.isNotEmpty()) {
            if (
                binding.price1.text.isNotEmpty() &&
                binding.amount1.text.isNotEmpty() &&
                binding.unitset1.text.isNotEmpty()
            ) {

                job1 = Job(
                    0,
                    binding.jobname1.text.toString(),
                    binding.invoiceNumber.text.toString(),
                    binding.price1.text.toString().toDouble(),
                    binding.amount1.text.toString().toInt(),
                    binding.unitset1.text.toString(),
                )

                sum += (job1.price * job1.quantity) + vat * (job1.price * job1.quantity)


                mInvoiceViewModel.insertJob(job1)

            } else
                Toast.makeText(
                    context,
                    "Nie wypełniono wszystkich wymaganych pól dla ${binding.jobname1.text}",
                    Toast.LENGTH_SHORT
                ).show()
        } else Toast.makeText(context, "Nie wpisano żadnej usługi.", Toast.LENGTH_LONG).show()

        if (binding.job2.isVisible) {
            if (
                binding.price2.text.isNotEmpty() &&
                binding.amount2.text.isNotEmpty() &&
                binding.unitset2.text.isNotEmpty()
            ) {

                job2 = Job(
                    0,
                    binding.jobname2.text.toString(),
                    binding.invoiceNumber.text.toString(),
                    binding.price2.text.toString().toDouble(),
                    binding.amount2.text.toString().toInt(),
                    binding.unitset2.text.toString()
                )

                sum += (job2.price * job2.quantity) + vat * (job2.price * job2.quantity)

                mInvoiceViewModel.insertJob(job2)

            } else
                Toast.makeText(
                    context,
                    "Nie wypełniono wszystkich wymaganych pól dla ${binding.jobname2.text}",
                    Toast.LENGTH_SHORT
                ).show()
        }

        if (binding.job3.isVisible) {
            if (
                binding.price3.text.isNotEmpty() &&
                binding.amount3.text.isNotEmpty() &&
                binding.unitset3.text.isNotEmpty()
            ) {

                job3 = Job(
                    0,
                    binding.jobname3.text.toString(),
                    binding.invoiceNumber.text.toString(),
                    binding.price3.text.toString().toDouble(),
                    binding.amount3.text.toString().toInt(),
                    binding.unitset3.text.toString()
                )

                sum += (job3.price * job3.quantity) + vat * (job3.price * job3.quantity)
                mInvoiceViewModel.insertJob(job3)

            } else
                Toast.makeText(
                    context,
                    "Nie wypełniono wszystkich wymaganych pól dla ${binding.jobname3.text}",
                    Toast.LENGTH_SHORT
                ).show()
        }

        if (binding.job4.isVisible) {
            if (
                binding.price4.text.isNotEmpty() &&
                binding.amount4.text.isNotEmpty() &&
                binding.unitset4.text.isNotEmpty()
            ) {

                job4 = Job(
                    0,
                    binding.jobname4.text.toString(),
                    binding.invoiceNumber.text.toString(),
                    binding.price4.text.toString().toDouble(),
                    binding.amount4.text.toString().toInt(),
                    binding.unitset4.text.toString()
                )

                sum += (job4.price * job4.quantity) + vat * (job4.price * job4.quantity)
                mInvoiceViewModel.insertJob(job4)

            } else
                Toast.makeText(
                    context,
                    "Nie wypełniono wszystkich wymaganych pól dla ${binding.jobname4.text}",
                    Toast.LENGTH_SHORT
                ).show()
        }

        if (binding.job5.isVisible) {
            if (
                binding.price5.text.isNotEmpty() &&
                binding.amount5.text.isNotEmpty() &&
                binding.unitset5.text.isNotEmpty()
            ) {

                job5 = Job(
                    0,
                    binding.jobname5.text.toString(),
                    binding.invoiceNumber.text.toString(),
                    binding.price5.text.toString().toDouble(),
                    binding.amount5.text.toString().toInt(),
                    binding.unitset5.text.toString()
                )

                sum += (job5.price * job5.quantity) + vat * (job5.price * job5.quantity)
                mInvoiceViewModel.insertJob(job5)

            } else
                Toast.makeText(
                    context,
                    "Nie wypełniono wszystkich wymaganych pól dla ${binding.jobname5.text}",
                    Toast.LENGTH_SHORT
                ).show()
        }

        if (binding.job7.isVisible) {
            if (
                binding.price7.text.isNotEmpty() &&
                binding.amount7.text.isNotEmpty() &&
                binding.unitset7.text.isNotEmpty()
            ) {

                job7 = Job(
                    0,
                    binding.jobname7.text.toString(),
                    binding.invoiceNumber.text.toString(),
                    binding.price7.text.toString().toDouble(),
                    binding.amount7.text.toString().toInt(),
                    binding.unitset7.text.toString()
                )

                sum += (job7.price * job7.quantity) + vat * (job7.price * job7.quantity)

                mInvoiceViewModel.insertJob(job7)

            } else
                Toast.makeText(
                    context,
                    "Nie wypełniono wszystkich wymaganych pól dla ${binding.jobname7.text}",
                    Toast.LENGTH_SHORT
                ).show()
        }

        if (binding.job6.isVisible) {
            if (
                binding.price6.text.isNotEmpty() &&
                binding.amount6.text.isNotEmpty() &&
                binding.unitset6.text.isNotEmpty()
            ) {

                job6 = Job(
                    0,
                    binding.jobname6.text.toString(),
                    binding.invoiceNumber.text.toString(),
                    binding.price6.text.toString().toDouble(),
                    binding.amount6.text.toString().toInt(),
                    binding.unitset6.text.toString()
                )
                sum += (job6.price * job6.quantity) + vat * (job6.price * job6.quantity)

                mInvoiceViewModel.insertJob(job6)

            } else
                Toast.makeText(
                    context,
                    "Nie wypełniono wszystkich wymaganych pól dla ${binding.jobname6.text}",
                    Toast.LENGTH_SHORT
                ).show()
        }

        if (binding.job8.isVisible) {
            if (
                binding.price8.text.isNotEmpty() &&
                binding.amount8.text.isNotEmpty() &&
                binding.unitset8.text.isNotEmpty()
            ) {

                job8 = Job(
                    0,
                    binding.jobname8.text.toString(),
                    binding.invoiceNumber.text.toString(),
                    binding.price8.text.toString().toDouble(),
                    binding.amount8.text.toString().toInt(),
                    binding.unitset8.text.toString()
                )
                sum += (job8.price * job8.quantity) + vat * (job8.price * job8.quantity)

                mInvoiceViewModel.insertJob(job8)

            } else
                Toast.makeText(
                    context,
                    "Nie wypełniono wszystkich wymaganych pól dla ${binding.jobname8.text}",
                    Toast.LENGTH_SHORT
                ).show()
        }

        if (binding.job9.isVisible) {
            if (
                binding.price9.text.isNotEmpty() &&
                binding.amount9.text.isNotEmpty() &&
                binding.unitset9.text.isNotEmpty()
            ) {

                job9 = Job(
                    0,
                    binding.jobname9.text.toString(),
                    binding.invoiceNumber.text.toString(),
                    binding.price9.text.toString().toDouble(),
                    binding.amount9.text.toString().toInt(),
                    binding.unitset9.text.toString()
                )
                sum += (job9.price * job9.quantity) + vat * (job9.price * job9.quantity)

                mInvoiceViewModel.insertJob(job9)

            } else
                Toast.makeText(
                    context,
                    "Nie wypełniono wszystkich wymaganych pól dla ${binding.jobname9.text}",
                    Toast.LENGTH_SHORT
                ).show()
        }

        if (binding.job10.isVisible) {
            if (
                binding.price10.text.isNotEmpty() &&
                binding.amount10.text.isNotEmpty() &&
                binding.unitset10.text.isNotEmpty()
            ) {

                job10 = Job(
                    0,
                    binding.jobname10.text.toString(),
                    binding.invoiceNumber.text.toString(),
                    binding.price10.text.toString().toDouble(),
                    binding.amount10.text.toString().toInt(),
                    binding.unitset10.text.toString()
                )
                sum += (job10.price * job10.quantity) + vat * (job10.price * job10.quantity)

                mInvoiceViewModel.insertJob(job10)

            } else
                Toast.makeText(
                    context,
                    "Nie wypełniono wszystkich wymaganych pól dla - ${binding.jobname10.text}",
                    Toast.LENGTH_SHORT
                ).show()
        }



        if (MyClient.name != "Clients Name") {
            val newInvoice =
                Invoice(
                    0,
                    binding.invoiceNumber.text.toString(),
                    jobsCount,
                    sum,
                    binding.btChooseLanguage.toString(),
                    binding.btVat.text.toString().toInt(),
                    MyClient
                )
            mInvoiceViewModel.insertInvoice(newInvoice)
        } else
            Toast.makeText(
                context,
                "Nie wybrano klienta!",
                Toast.LENGTH_SHORT
            ).show()


    }


}

var MyClient = Client(0, "Wybierz Klienta", "x", "x", "x", "x", "x", "x")

// hope to delete it someday
@Parcelize
data class InvoicingClient(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String,
    var email: String,
    var phone: String,
    var taxNumber: String,
    var streetNumber: String,
    var postalCode: String,
    var city: String
) : Parcelable





