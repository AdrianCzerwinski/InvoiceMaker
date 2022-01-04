package com.adrianczerwinski.faktura


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.adrianczerwinski.faktura.data.models.Job
import com.adrianczerwinski.faktura.data.models.Seller
import com.adrianczerwinski.faktura.data.viemodels.SellerViewModel
import com.adrianczerwinski.faktura.databinding.ActivityInvoiceToPdfBinding
import com.adrianczerwinski.faktura.fragments.newinvoice.Common
import com.adrianczerwinski.faktura.fragments.newinvoice.MyClient
import com.adrianczerwinski.faktura.fragments.newinvoice.PdfDocumentAdapter
import com.github.barteksc.pdfviewer.PDFView
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class InvoiceToPdf : AppCompatActivity() {
    private lateinit var binding: ActivityInvoiceToPdfBinding
    private lateinit var mSellerViewModel: SellerViewModel
    private lateinit var companySellForAlreadyCreatedInvoice: String
    private lateinit var iban: String
    private lateinit var blz: String
    private lateinit var bic: String
    var cellsCount = 10


    var companyBuy = MyClient.name
    var addressBuy = MyClient.streetNumber + ", " + MyClient.postalCode + " " + MyClient.city
    var taxNumberBuy = MyClient.taxNumber
    var emailBuy = arrayOf(MyClient.email)


    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvoiceToPdfBinding.inflate(layoutInflater)
        mSellerViewModel = ViewModelProvider(this)[SellerViewModel::class.java]


        val intent: Intent = intent
        val invoiceNo = intent.getStringExtra("InvoiceNumber").toString()
        val invoiceNoFile = invoiceNameAfterDeletingWrongChars(invoiceNo)
        val fileName = "${invoiceNoFile}.pdf"
        val pdfView: PDFView = binding.pdfView
        val paths = File(Common.getAppPath(this@InvoiceToPdf) + fileName)

        val backpress = this.onBackPressedDispatcher.addCallback(this, true) {

            val intent2 = Intent(this@InvoiceToPdf, MainActivity::class.java)
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            finish()
            startActivity(intent2)
        }
        backpress.isEnabled = true

        GlobalScope.launch {
            val myData: Seller? = mSellerViewModel.getMySellerData()
            if (myData != null) {
                companySellForAlreadyCreatedInvoice = myData.name
                iban = myData.iban
                blz = myData.blz
                bic = myData.bic
            }
        }

        val companySell = intent.getStringExtra("sellerName").toString()
        val projektName = intent.getStringExtra("ProjectName").toString()
        val addressSell = intent.getStringExtra("sellerAddress").toString()
        val taxNumberSell = intent.getStringExtra("sellerTaxNumber").toString()
        val contactSell = intent.getStringExtra("sellerContactData").toString()
        val cityOnInvoice = intent.getStringExtra("city").toString()
        val phone = intent.getStringExtra("phone").toString()


        if (paths.exists()) {

            pdfView.fromFile(paths).load()
            binding.btnCreatePdf.setOnClickListener {
                printPDF(fileName)
            }
            binding.btSendInvoiceViaEmail.setOnClickListener {

                val attachment =
                    File(Common.getAppPath(this@InvoiceToPdf) + fileName).toUri()
                composeEmail(
                    emailBuy,
                    invoiceNo,
                    attachment,
                    companySellForAlreadyCreatedInvoice,
                    projektName,
                    contactSell
                )
            }
        } else {

            Dexter.withActivity(this)
                .withPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        createPDFFile(
                            Common.getAppPath(this@InvoiceToPdf) + fileName,
                            companySell,
                            contactSell,
                            addressSell,
                            taxNumberSell,
                            cityOnInvoice,
                            iban,
                            blz,
                            bic
                        )
                        binding.btnCreatePdf.setOnClickListener {
                            printPDF(fileName)
                        }
                        binding.btSendInvoiceViaEmail.setOnClickListener {
                            val attachment =
                                File(Common.getAppPath(this@InvoiceToPdf) + fileName).toUri()
                            composeEmail(
                                emailBuy,
                                invoiceNo,
                                attachment,
                                companySell,
                                projektName,
                                contactSell
                            )
                        }

                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?
                    ) {

                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {

                    }


                })
                .check()


            Dexter.withActivity(this)
                .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        val path = File(Common.getAppPath(this@InvoiceToPdf) + fileName)
                        pdfView.fromFile(path).load()

                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?
                    ) {

                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {

                    }


                })
                .check()

        }

        val view = binding.root
        setContentView(view)
    }

    private fun createPDFFile(
        path: String,
        companySell: String,
        contactSell: String,
        addressSell: String,
        taxNumberSell: String,
        cityOnInvoice: String,
        iban: String,
        blz: String,
        bic: String
    ) {
        if (File(path).exists())
            File(path).delete()
        try {
            val document = Document()
            // Save
            PdfWriter.getInstance(document, FileOutputStream(path))

            // Open to write
            document.open()

            //Setting
            document.setMargins(40f, 40f, 30f, 30f)
            document.pageSize = PageSize.A4
            document.addCreationDate()
            document.addAuthor(companySell)
            document.addCreator(contactSell)
            document.newPage()

            //Adding title - invoice number
            val fontNormal = BaseFont.createFont(
                "assets/fonts/OpenSansCondensed-Light.ttf",
                BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED
            )
            val fontBold = BaseFont.createFont(
                "assets/fonts/OpenSansCondensed-Bold.ttf",
                BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED
            )

            val titleStyle = Font(fontBold, 30.0f, Font.NORMAL, BaseColor.BLACK)
            val cellStyle = Font(fontNormal, 14f, Font.NORMAL, BaseColor.BLACK)
            val subtitleStyle = Font(fontNormal, 20f, Font.NORMAL, BaseColor.BLACK)

            val intent: Intent = intent
            val list: ArrayList<Job> =
                intent.getParcelableArrayListExtra<Job>("data") as ArrayList<Job>
            val invoiceNo = list.first().invoiceNumber

            // Title
            addNewItem(document, "Rechnung Nr: $invoiceNo", Element.ALIGN_LEFT, titleStyle)
            addLineSeparatorThick(document)
            addLineSpace(document)

            // Data i miejscowość
            val calendarAndTime = LocalDateTime.now()
            val dateToday = calendarAndTime.format(DateTimeFormatter.ofPattern("y-M-d")).toString()

            addLineSpace(document)
            addNewItem(document, "$cityOnInvoice, $dateToday ", Element.ALIGN_RIGHT, cellStyle)
            addLineSpace(document)


            //Project name

            val projectName = intent.getStringExtra("ProjectName")
            addLineSpace(document)
            addNewItem(document, "Projektname:  $projectName", Element.ALIGN_LEFT, subtitleStyle)
            addLineSpace(document)

            //Seller and Buyer

            val columnWidths2 = floatArrayOf(32f, 32f)
            val table3 = PdfPTable(columnWidths2)

            table3.widthPercentage = 100f
            table3.horizontalAlignment = Element.ALIGN_CENTER
            table3.spacingBefore = 20.0f
            table3.defaultCell.border = 0
            table3.defaultCell.setPadding(4f)

            var mainCell = PdfPCell(Phrase("Verkäufer: ", subtitleStyle))
            mainCell.border = PdfPCell.NO_BORDER
            table3.addCell(mainCell)

            mainCell = PdfPCell(Phrase("Käufer: ", subtitleStyle))
            mainCell.border = PdfPCell.NO_BORDER
            table3.addCell(mainCell)

            mainCell = PdfPCell(Phrase(companySell, cellStyle))
            mainCell.border = PdfPCell.NO_BORDER
            table3.addCell(mainCell)

            mainCell = PdfPCell(Phrase(companyBuy, cellStyle))
            mainCell.border = PdfPCell.NO_BORDER
            table3.addCell(mainCell)

            mainCell = PdfPCell(Phrase(addressSell, cellStyle))
            mainCell.border = PdfPCell.NO_BORDER
            table3.addCell(mainCell)

            mainCell = PdfPCell(Phrase(addressBuy, cellStyle))
            mainCell.border = PdfPCell.NO_BORDER
            table3.addCell(mainCell)

            mainCell = PdfPCell(Phrase(taxNumberSell, cellStyle))
            mainCell.border = PdfPCell.NO_BORDER
            table3.addCell(mainCell)

            mainCell = PdfPCell(Phrase("Steuernummer: $taxNumberBuy", cellStyle))
            mainCell.border = PdfPCell.NO_BORDER
            table3.addCell(mainCell)

            mainCell = PdfPCell(Phrase(contactSell, cellStyle))
            mainCell.border = PdfPCell.NO_BORDER
            table3.addCell(mainCell)

            document.add(table3)

            addLineSeparatorThick(document)
            addLineSpace(document)
            addLineSpace(document)

            // Adding column titles with style properties
            // plus setting width of columns

            val columnWidths = floatArrayOf(2f, 10f, 3f, 2f, 4f, 3f, 6f, 3f)
            val table1 = PdfPTable(columnWidths)

            table1.widthPercentage = 100f
            table1.horizontalAlignment = Element.ALIGN_CENTER
            table1.spacingBefore = 20.0f
            table1.defaultCell.border = 0
            table1.defaultCell.setPadding(4f)

            val lp = "Pos."
            val description = "Arbeitsbeschreibung"
            val amount = "Menge"
            val unit = "ME"
            val price = "Einzelpreis"
            val brutto = "Preis inkl. MwSt."
            val currency = "Währung"
            val vatDE = "MwSt."

            mainCell = PdfPCell(Phrase(lp, cellStyle))
            mainCell.border = PdfPCell.NO_BORDER
            table1.addCell(mainCell)

            mainCell = PdfPCell(Phrase(description, cellStyle))
            mainCell.border = PdfPCell.NO_BORDER
            table1.addCell(mainCell)

            mainCell = PdfPCell(Phrase(amount, cellStyle))
            mainCell.border = PdfPCell.NO_BORDER
            table1.addCell(mainCell)

            mainCell = PdfPCell(Phrase(unit, cellStyle))
            mainCell.border = PdfPCell.NO_BORDER
            table1.addCell(mainCell)

            mainCell = PdfPCell(Phrase(price, cellStyle))
            mainCell.border = PdfPCell.NO_BORDER
            table1.addCell(mainCell)


            mainCell = PdfPCell(Phrase(vatDE, cellStyle))
            mainCell.border = PdfPCell.NO_BORDER
            table1.addCell(mainCell)


            mainCell = PdfPCell(Phrase(brutto, cellStyle))
            mainCell.border = PdfPCell.NO_BORDER
            table1.addCell(mainCell)

            mainCell = PdfPCell(Phrase(currency, cellStyle))
            mainCell.border = PdfPCell.NO_BORDER
            table1.addCell(mainCell)


            document.add(table1)

            addLineSeparator(document)

            val table2 = PdfPTable(columnWidths)
            var mainCellVar: PdfPCell

            table2.widthPercentage = 100f
            table2.horizontalAlignment = Element.ALIGN_CENTER
            table2.spacingBefore = 20.0f
            table2.defaultCell.border = 0
            table2.defaultCell.setPadding(4f)

            cellsCount = intent.getIntExtra("jobsCount", 1)
            val sum = intent.getDoubleExtra("theSum", 0.00)
            val taxValue = intent.getIntExtra("VAT", 0)
            val currencyVar = intent.getStringExtra("Currency")
            val payingTime = intent.getStringExtra("Paying")

            for (i in 0 until cellsCount) {

                mainCellVar = PdfPCell(Phrase((i + 1).toString(), cellStyle))
                mainCellVar.border = PdfPCell.NO_BORDER
                table2.addCell(mainCellVar)
                addLineSpace(document)

                mainCellVar = PdfPCell(Phrase(list.getOrNull(i)!!.jobName, cellStyle))
                mainCellVar.border = PdfPCell.NO_BORDER
                table2.addCell(mainCellVar)
                addLineSpace(document)

                mainCellVar = PdfPCell(Phrase(list.getOrNull(i)!!.quantity.toString(), cellStyle))
                mainCellVar.border = PdfPCell.NO_BORDER
                table2.addCell(mainCellVar)
                addLineSpace(document)

                mainCellVar = PdfPCell(Phrase(list.getOrNull(i)!!.unit, cellStyle))
                mainCellVar.border = PdfPCell.NO_BORDER
                table2.addCell(mainCellVar)
                addLineSpace(document)

                mainCellVar = PdfPCell(Phrase(list.getOrNull(i)!!.price.toString(), cellStyle))
                mainCellVar.border = PdfPCell.NO_BORDER
                table2.addCell(mainCellVar)
                addLineSpace(document)

                mainCellVar = PdfPCell(Phrase(taxValue.toString(), cellStyle))
                mainCellVar.border = PdfPCell.NO_BORDER
                table2.addCell(mainCellVar)
                addLineSpace(document)



                mainCellVar = PdfPCell(
                    Phrase(
                        brutto(
                            list.getOrNull(i)!!.price,
                            list.getOrNull(i)!!.quantity.toDouble(),
                            taxValue
                        ).toString(), cellStyle
                    )
                )

                mainCellVar.border = PdfPCell.NO_BORDER
                table2.addCell(mainCellVar)
                addLineSpace(document)

                mainCellVar = PdfPCell(Phrase(currencyVar, cellStyle))
                mainCellVar.border = PdfPCell.NO_BORDER
                table2.addCell(mainCellVar)
                addLineSpace(document)


            }
            addLineSpace(document)
            addLineSpace(document)
            document.add(table2)

            addLineSeparator(document)
            addLineSpace(document)
            addLineSpace(document)

            addNewItem(
                document,
                "Gesamptbetrag: $sum $currencyVar",
                Element.ALIGN_LEFT,
                subtitleStyle
            )
            addLineSpace(document)
            addLineSpace(document)
            addLineSpace(document)
            addLineSpace(document)

            addNewItem(
                document,
                "Zahlungsweise: $payingTime",
                Element.ALIGN_LEFT,
                cellStyle
            )

            addNewItem(
                document,
                "KONTONUMMER: IBAN  $iban, BLZ  $blz, BIC  $bic",
                Element.ALIGN_LEFT,
                cellStyle
            )

            addLineSpace(document)
            addLineSpace(document)

            if (taxValue == 0) {

                addNewItem(
                    document,
                    "Vermerke:",
                    Element.ALIGN_LEFT,
                    subtitleStyle
                )

                addLineSpace(document)

                addNewItem(
                    document,
                    "Hiermit erlaube ich mir folgende Rechnung für Bauleistungen zu stellen. " +
                            "Die Umsatzsteuer für diese Leistung schuldet nach §13b UStG der " +
                            "Leistungsempfänger.",
                    Element.ALIGN_LEFT,
                    cellStyle
                )

            }


            //close
            document.close()


            Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()


        } catch (e: Exception) {
            Log.e("Adik", "" + e.message)

        }


    }

    private fun printPDF(fileNames: String) {
        val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager

        try {
            val printAdapter = PdfDocumentAdapter(
                this@InvoiceToPdf,
                Common.getAppPath(this@InvoiceToPdf) + fileNames,
                fileNames
            )

            printManager.print("Document", printAdapter, PrintAttributes.Builder().build())
        } catch (e: java.lang.Exception) {
            Log.e("Adik", "" + e.message)
        }

    }

    @kotlin.jvm.Throws(DocumentException::class)
    private fun addLineSeparator(document: Document) {
        val lineSeparator = LineSeparator()
        lineSeparator.lineColor = BaseColor(0, 0, 0, 68)
        addLineSpace(document)
        document.add(Chunk(lineSeparator))
        addLineSpace(document)
    }

    @kotlin.jvm.Throws(DocumentException::class)
    private fun addLineSeparatorThick(document: Document) {
        val lineSeparator = LineSeparator()
        lineSeparator.lineColor = BaseColor(0, 0, 0, 68)
        lineSeparator.lineColor = BaseColor(0, 0, 0, 68)
        addLineSpace(document)
        document.add(Chunk(lineSeparator))
        addLineSpace(document)
    }

    @kotlin.jvm.Throws(DocumentException::class)
    private fun addLineSpace(document: Document) {
        document.add(Paragraph(""))

    }

    @kotlin.jvm.Throws(DocumentException::class)
    private fun addNewItem(document: Document, text: String, align: Int, style: Font) {
        val chunk = Chunk(text, style)
        val p = Paragraph(chunk)
        p.alignment = align
        document.add(p)
    }

    fun composeEmail(
        addresses: Array<String>,
        subject: String?,
        attachment: Uri,
        name: String,
        projektName: String,
        phone: String
    ) {
        val intentMail = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, addresses)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            if (MyClient.language == "PL") {
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Dzień dobry,\n W załaczniku przesyłam fakturę.\n Pozdrawiam, \n$name"
                )
            } else putExtra(
                Intent.EXTRA_TEXT,
                "Guten Tag,\n\n Im Anhang sende ich die Rechnung fur die in der " +
                        "$projektName geleistete Arbeit\n\n Pozdrawiam, \n$name \n$phone"
            )
            putExtra(Intent.EXTRA_STREAM, attachment)
        }
        if (intentMail.resolveActivity(packageManager) != null) {
            startActivity(intentMail)
        }
    }

    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(Date())
    }

    private fun brutto(unitPrice: Double, amount: Double, taxValue: Int): Double {
        return (unitPrice * amount) + (unitPrice * amount) * (taxValue * 0.01)
    }

    private fun invoiceNameAfterDeletingWrongChars(name: String): String {
        var x = name.replace('/', '_')
        x = x.replace('"', '_')
        x = x.replace('<', '_')
        x = x.replace('>', '_')
        x = x.replace(':', '_')
        x = x.replace('*', '_')
        x = x.replace('?', '_')
        x = x.replace('!', '_')
        return x
    }


}


