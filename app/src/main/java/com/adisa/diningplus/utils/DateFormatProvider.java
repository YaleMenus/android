package com.adisa.diningplus.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateFormatProvider {
    public static final SimpleDateFormat full = new SimpleDateFormat("MMMMM, dd yyyy HH:mm:ss", Locale.US);
    public static final SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    public static final SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss", Locale.US);
    public static final SimpleDateFormat hour = new SimpleDateFormat("h:mm a", Locale.US);
    public static final SimpleDateFormat hour24 = new SimpleDateFormat("HH:mm", Locale.US);
}
