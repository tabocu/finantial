package sistemas.puc.com.finantialapp.data;

import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

public class FinantialContract {

    public static final String CONTENT_AUTHORITY = "sistemas.puc.com.finantialapp.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOEDA   = "moeda";
    public static final String PATH_TESOURO = "tesouro";
    public static final String PATH_INDICE  = "indice";

    // TODO: Find a replacement for Time. Time is deprecated
    public static long normalizeDate(long startDate) {
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    public static final class MoedaEntry implements BaseColumns {

    }

    public static final class TesouroEntry implements BaseColumns {

    }

    public static final class IndiceEntry implements BaseColumns {

    }
}
