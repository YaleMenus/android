package com.adisa.diningplus.utils

import java.text.SimpleDateFormat
import java.util.*

object DateFormatProvider {
    val full = SimpleDateFormat("MMMMM, dd yyyy HH:mm:ss", Locale.US)
    val date = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val dateFull = SimpleDateFormat("MMMM d", Locale.US)
    val time = SimpleDateFormat("HH:mm:ss", Locale.US)
    val hour = SimpleDateFormat("h:mm a", Locale.US)
    val hour24 = SimpleDateFormat("HH:mm", Locale.US)
}