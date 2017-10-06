package sistemas.puc.com.finantialapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import sistemas.puc.com.finantialapp.data.FinantialContract.MoedaEntry;

public class FinantialDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "finantial.db";

    public FinantialDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOEDA_TABLE = "CREATE TABLE " + MoedaEntry.TABLE_NAME + " (" +
                MoedaEntry._ID                   + " INTEGER PRIMARY KEY," +
                MoedaEntry.COLUMN_MOEDA_INITIALS + " TEXT UNIQUE NOT NULL, " +
                MoedaEntry.COLUMN_MOEDA_NAME     + " TEXT UNIQUE NOT NULL, " +
                MoedaEntry.COLUMN_MOEDA_DATE     + " INTEGER NOT NULL, " +
                MoedaEntry.COLUMN_MOEDA_RATE     + " REAL NOT NULL, " +
                " );";

        db.execSQL(SQL_CREATE_MOEDA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoedaEntry.TABLE_NAME);
        onCreate(db);
    }
}
