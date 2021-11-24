package com.adrianczerwinski.invoicemaker


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.adrianczerwinski.invoicemaker.data.models.Job
import com.adrianczerwinski.invoicemaker.databinding.ActivityInvoiceToPdfBinding
import com.adrianczerwinski.invoicemaker.fragments.newinvoice.Common
import com.adrianczerwinski.invoicemaker.fragments.newinvoice.MyClient
import com.adrianczerwinski.invoicemaker.fragments.newinvoice.PdfDocumentAdapter
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
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class InvoiceToPdf : AppCompatActivity() {
    private lateinit var binding: ActivityInvoiceToPdfBinding
    var cellsCount = 10

    // Sprzedający - do zmiany na klasę z ROOM Db

    var companySell = "Piotr Czerwinski"
    var addressSell = "Rugwagstrasse 52"
    var taxNumberSell = "6166668896"
    var phoneSell = "606248951"
    var emailSell = "blabla@gmail.com"
    var contactSell = "Piotr Czerwinski"

    // Buyer:

    var companyBuy = MyClient.name
    var addressBuy = MyClient.streetNumber + ", " + MyClient.postalCode + " " + MyClient.city
    var taxNumberBuy = MyClient.taxNumber
    var emailBuy = arrayOf(MyClient.email)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvoiceToPdfBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val date = getCurrentDate()

        val intent: Intent = intent
        var list: ArrayList<Job> = intent.getParcelableArrayListExtra<Job>("data") as ArrayList<Job>
        val invoiceNo = list.first().invoiceNumber
        val fileName = "${list.first().invoiceNumber}.pdf"
        val pdfView: PDFView = binding.pdfView

        Dexter.withActivity(this)
            .withPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    createPDFFile(Common.getAppPath(this@InvoiceToPdf) + fileName)
                    binding.btnCreatePdf.setOnClickListener {
                        printPDF()
                    }
                    binding.btSendInvoiceViaEmail.setOnClickListener {
                        val attachment =
                            File(Common.getAppPath(this@InvoiceToPdf) + fileName).toUri()
                        composeEmail(emailBuy, invoiceNo, attachment)
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

    private fun createPDFFile(path: String) {
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
            val subtitleStyle = Font(fontNormal, 14f, Font.NORMAL, BaseColor.BLACK)
            val descriptionStyle = Font(fontNormal, 8f, Font.NORMAL, BaseColor.BLACK)

//            val bundle = intent.getBundleExtra("myBundle")
//            var job1  = bundle!!.getParcelable<Job>("job1") as Job
//            val invoiceNo = job1.invoiceNumber

            val intent: Intent = intent
            var list: ArrayList<Job> =
                intent.getParcelableArrayListExtra<Job>("data") as ArrayList<Job>
            val invoiceNo = list.first().invoiceNumber

            // Title
            addNewItem(document, invoiceNo, Element.ALIGN_LEFT, titleStyle)
            addLineSeparator(document)

            //Seller and Buyer
            addLineSpace(document)
            addNewItem(document, "Sprzedający", Element.ALIGN_LEFT, subtitleStyle)
            addLineSpace(document)
            addLineSpace(document)

            addNewItem(document, companySell, Element.ALIGN_LEFT, descriptionStyle)
            addNewItem(document, addressSell, Element.ALIGN_LEFT, descriptionStyle)
            addNewItem(document, taxNumberSell, Element.ALIGN_LEFT, descriptionStyle)
            addLineSpace(document)
            addNewItem(document, "Kupujący", Element.ALIGN_LEFT, subtitleStyle)
            addNewItem(document, companyBuy, Element.ALIGN_LEFT, descriptionStyle)
            addNewItem(document, addressBuy, Element.ALIGN_LEFT, descriptionStyle)
            addNewItem(document, taxNumberBuy, Element.ALIGN_LEFT, descriptionStyle)
            addLineSpace(document)
            addLineSpace(document)

            // Adding column titles with style properties
            // plus setting width of columns

            val columnWidths = floatArrayOf(2f, 10f, 3f, 5f, 5f, 3f, 3f, 3f, 3f)
            val table1 = PdfPTable(columnWidths)

            table1.widthPercentage = 100f
            table1.horizontalAlignment = Element.ALIGN_CENTER
            table1.spacingBefore = 20.0f
            table1.defaultCell.border = 0
            table1.defaultCell.setPadding(4f)

            val lp = "Lp."
            val description = "Nazwa"
            val amount = "Ilość"
            val unit = "Jednostka"
            val price = "Cena"
            val cost = "Netto"
            val taxRate = "VAT"
            val brutto = "Brutto"
            val currency = "Waluta"

            var mainCell = PdfPCell(Phrase(lp, cellStyle))
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

            mainCell = PdfPCell(Phrase(cost, cellStyle))
            mainCell.border = PdfPCell.NO_BORDER
            table1.addCell(mainCell)

            mainCell = PdfPCell(Phrase(taxRate, cellStyle))
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

            val currencyVar = "PLN"
            var sum = 0.0
            val table2 = PdfPTable(columnWidths)
            var mainCellVar: PdfPCell

            table2.widthPercentage = 100f
            table2.horizontalAlignment = Element.ALIGN_CENTER
            table2.spacingBefore = 20.0f
            table2.defaultCell.border = 0
            table2.defaultCell.setPadding(4f)

            cellsCount = intent.getIntExtra("jobsCount", 1)


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

                var nettoValue = netto(
                    list.getOrNull(i)!!.price,
                    list.getOrNull(i)!!.quantity
                )
                mainCellVar = PdfPCell(
                    Phrase(
                        nettoValue.toString(), cellStyle
                    )
                )
                mainCellVar.border = PdfPCell.NO_BORDER
                table2.addCell(mainCellVar)
                addLineSpace(document)

                var tax = list.getOrNull(i)!!.vat

                mainCellVar = PdfPCell(Phrase("$tax %", cellStyle))
                mainCellVar.border = PdfPCell.NO_BORDER
                table2.addCell(mainCellVar)
                addLineSpace(document)


                sum += brutto(nettoValue, tax)

                mainCellVar = PdfPCell(Phrase(brutto(nettoValue, tax).toString(), cellStyle))
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

            mainCellVar = PdfPCell(Phrase(" ", cellStyle))
            mainCellVar.border = PdfPCell.NO_BORDER
            mainCellVar.colspan = 6
            table2.addCell(mainCellVar)

            mainCellVar = PdfPCell(Phrase("Suma", cellStyle))
            mainCellVar.border = PdfPCell.NO_BORDER
            mainCellVar.colspan = 1
            table2.addCell(mainCellVar)

            mainCellVar = PdfPCell(Phrase(sum.toString(), cellStyle))
            mainCellVar.border = PdfPCell.NO_BORDER
            mainCellVar.colspan = 1
            table2.addCell(mainCellVar)

            mainCellVar = PdfPCell(Phrase(currencyVar, cellStyle))
            mainCellVar.border = PdfPCell.NO_BORDER
            table2.addCell(mainCellVar)



            document.add(table2)


            //close
            document.close()


            Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()


        } catch (e: Exception) {
            Log.e("Adik", "" + e.message)

        }


    }

    private fun printPDF() {
        val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager
        val intents: Intent = intent
        val lists: ArrayList<Job> =
            intents.getParcelableArrayListExtra<Job>("data") as ArrayList<Job>
        val fileNames = lists.first().invoiceNumber

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

    fun composeEmail(addresses: Array<String>, subject: String, attachment: Uri) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, addresses)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, "Dzień dobry, w załaczniku faktura.")
            putExtra(Intent.EXTRA_STREAM, attachment)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(Date())
    }

    fun netto(price: Double, quantity: Int): Double {
        return price * quantity
    }

    fun brutto(netto: Double, taxValue: Int): Double {
        return netto * taxValue
    }


}


