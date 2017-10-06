package sistemas.puc.com.finantialapp.data;

import android.content.ContentResolver;
import android.content.ContentUris;
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

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOEDA).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOEDA;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOEDA;

        public static final String TABLE_NAME = "moeda";

        // TODO: Add moeda columns definitions
        public static final String COLUMN_MOEDA_KEY       = "moeda_id";
        public static final String COLUMN_MOEDA_INITIALS  = "moeda_initials";
        public static final String COLUMN_MOEDA_NAME      = "moeda_name";
        public static final String COLUMN_MOEDA_DATE      = "moeda_date";
        public static final String COLUMN_MOEDA_RATE      = "moeda_rate";

        public static Uri buildMoedaUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class TesouroEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TESOURO).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TESOURO;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TESOURO;

        public static final String TABLE_NAME = "tesouro";

        // TODO: Add tesouro columns definitions

        public static Uri buildTesouroUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class IndiceEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INDICE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INDICE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INDICE;

        public static final String TABLE_NAME = "indice";

        // TODO: Add indice columns definitions

        public static Uri buildIndiceUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
