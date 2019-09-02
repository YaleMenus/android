package com.adisa.diningplus;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateFormatProvider {
    static SimpleDateFormat full = new SimpleDateFormat("MMMMM, dd yyyy HH:mm:ss",Locale.US),
                            date = new SimpleDateFormat("yyyy-MM-dd", Locale.US),
                            time = new SimpleDateFormat("HH:mm:ss", Locale.US),
                            hour = new SimpleDateFormat("h:mm a", Locale.US);
}
