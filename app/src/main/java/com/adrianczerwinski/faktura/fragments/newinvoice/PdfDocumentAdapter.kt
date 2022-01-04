package com.adrianczerwinski.faktura.fragments.newinvoice

import android.content.Context
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.util.Log
import java.io.*

class PdfDocumentAdapter(context: Context, path: String, fileName: String) : PrintDocumentAdapter() {

    private var context : Context?=null
    private var path = ""
    private var fileName = " x"

    init {
        this.context = context
        this.path = path
        this.fileName = fileName
    }

    override fun onLayout(
        p0: PrintAttributes?,
        p1: PrintAttributes?,
        cancellationSignal: CancellationSignal?,
        layoutResult: LayoutResultCallback?,
        p4: Bundle?
    ) {
        if(cancellationSignal!!.isCanceled)
            layoutResult!!.onLayoutCancelled()
        else{
            val builder = PrintDocumentInfo.Builder(fileName)
            builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                .build()
            layoutResult!!.onLayoutFinished(builder.build(), p1 != p0)
        }
    }

    override fun onWrite(
        pageRanges: Array<out PageRange>?,
        parcelFileDescriptor: ParcelFileDescriptor?,
        cancellationSignal: CancellationSignal?,
        writeResultCallback: WriteResultCallback?
    ) {
        var theIn :InputStream? = null
        var out : OutputStream? = null
        try {
                val file = File(path)
            theIn = FileInputStream(file)
            out = FileOutputStream(parcelFileDescriptor!!.fileDescriptor)

            if(!cancellationSignal!!.isCanceled) {
                theIn.copyTo(out)
                writeResultCallback!!.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
            }
            else
                writeResultCallback!!.onWriteCancelled()
        } catch (e:Exception){
            writeResultCallback!!.onWriteFailed(e.message)
            Log.e("Adik",""+e.message)
        } finally {
            try {
                theIn!!.close()
                out!!.close()
            } catch (e: java.lang.Exception) {
                Log.e("Adik", "" + e.message)
            }
        }


    }

}
