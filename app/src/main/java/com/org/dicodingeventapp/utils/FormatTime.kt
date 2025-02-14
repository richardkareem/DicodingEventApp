package com.org.dicodingeventapp.utils

import java.text.SimpleDateFormat
import java.util.Locale

object FormatTime {
     fun formatTimeRange(beginTime: String, endTime: String): String {
        val local = Locale("ID", "id")
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", local)
        val outputDateFormat = SimpleDateFormat("d MMMM yyyy", local)
        val outputTimeFormat = SimpleDateFormat("h a", local)

        val startDate = inputFormat.parse(beginTime)!!
        val endDate = inputFormat.parse(endTime)!!

        return "${outputDateFormat.format(startDate)} jam ${outputTimeFormat.format(startDate)} - ${outputDateFormat.format(endDate)} jam ${outputTimeFormat.format(endDate)}"
    }
}