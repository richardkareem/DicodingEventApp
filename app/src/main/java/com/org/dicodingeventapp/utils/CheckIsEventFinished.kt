package com.org.dicodingeventapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object CheckIsEventFinished {
    fun isFinished(dateString: String): Int {
        // Format tanggal dan waktu yang diharapkan
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        // Parsing input tanggal dan waktu
        val dateToCheck: Date = dateFormat.parse(dateString)!!

        // Tanggal dan waktu saat ini
        val currentDate = Date()

        // Cek apakah tanggal sudah kadaluarsa
        if(dateToCheck.before(currentDate)){
            return 1
        }
        return 0
    }

}