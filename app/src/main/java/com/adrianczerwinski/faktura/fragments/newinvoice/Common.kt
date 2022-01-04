package com.adrianczerwinski.faktura.fragments.newinvoice

import com.adrianczerwinski.faktura.InvoiceToPdf
import com.adrianczerwinski.faktura.R
import java.io.File

object Common {
    fun getAppPath(context: InvoiceToPdf):String {
        val dir = File(context.getExternalFilesDir(null).toString()
        +File.separator
        +context.resources.getString(R.string.app_name)
        +File.separator)
        if(!dir.exists())
            dir.mkdir()
        return dir.path+ File.separator

    }
}