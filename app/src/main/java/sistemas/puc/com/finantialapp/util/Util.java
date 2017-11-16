package sistemas.puc.com.finantialapp.util;

import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.util.Log;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public final class Util {

    private static String LOG_TAG = "Util";
    public final static String DATE_TEMPLATE_FORMAT_1 = "yyyy-MM-dd";
    public final static String DATE_TEMPLATE_FORMAT_2 = "dd/MM/yyyy";
    public final static String DATE_TEMPLATE_FORMAT_3 = "yyyyMM";
    public final static String DATE_TEMPLATE_FORMAT_4 = "MMM/yy";

    private Util() {}

    public static String getRealStringFromDouble(@FloatRange(from=0) double value,
                                                 @IntRange(from=0) int fractionDigits) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        numberFormat.setCurrency(Currency.getInstance("BRL"));
        numberFormat.setMaximumFractionDigits(fractionDigits);
        numberFormat.setMinimumFractionDigits(fractionDigits);
        return numberFormat.format(value);
    }

    public static String getBRStringFromDouble(double value, int fractionDigits) {
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("pt","BR"));
        numberFormat.setMinimumFractionDigits(fractionDigits);
        numberFormat.setMaximumFractionDigits(fractionDigits);
        return numberFormat.format(value);
    }

    public static String getPercentStringFromDouble(@FloatRange(from=0) double value,
                                                 @IntRange(from=0) int fractionDigits) {
        NumberFormat numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(fractionDigits);
        return numberFormat.format(value);
    }

    public static String getDateStringFromLong(@IntRange(from=0) long value) {
        Date date = new Date(value);
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
        return formater.format(date);
    }

    public static String getDayStringFromLong(@IntRange(from=0) long value) {
        Date date = new Date(value);
        SimpleDateFormat formater = new SimpleDateFormat("dd");
        return formater.format(date);
    }

    public static String getMonthStringFromLong(@IntRange(from=0) long value) {
        Date date = new Date(value);
        SimpleDateFormat formater = new SimpleDateFormat("MMMM",new Locale("pt", "BR"));

        return capitalize(formater.format(date));
    }

    public static String getYearStringFromLong(@IntRange(from=0) long value) {
        Date date = new Date(value);
        SimpleDateFormat formater = new SimpleDateFormat("yyyy",new Locale("pt", "BR"));
        return formater.format(date);
    }

    public static int getYearIntFromLong(@IntRange(from=0) long value) throws NumberFormatException{
        String year = getYearStringFromLong(value);
        return Integer.parseInt(year);
    }

    public static long getTimeFromDate(@IntRange(from=1, to=31) int day,
                                       @IntRange(from=1, to=12) int month,
                                       @IntRange(from=1980) int year) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(year, month-1, day);
        return calendar.getTime().getTime();
    }

    public static long getTimeFromDateString(@NonNull String dateString, @NonNull String template) {
        DateFormat formatter = new SimpleDateFormat(template, new Locale("pt", "BR"));
        Date date = null;
        long time = 0;
        try {
            date = formatter.parse(dateString);
            time = date.getTime();
        } catch (ParseException e) {
            Log.e(LOG_TAG, e.toString());
            e.printStackTrace();
        }
        return time;
    }

    public static String capitalize(@NonNull @Size(min=2) String target) {
        return target.substring(0,1).toUpperCase()
                +target.substring(1, target.length()).toLowerCase();
    }
}
