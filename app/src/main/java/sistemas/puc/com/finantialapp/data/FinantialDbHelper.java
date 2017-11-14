package sistemas.puc.com.finantialapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import sistemas.puc.com.finantialapp.data.FinantialContract.MoedaEntry;
import sistemas.puc.com.finantialapp.data.FinantialContract.IndiceEntry;
import sistemas.puc.com.finantialapp.data.FinantialContract.TesouroEntry;

public class FinantialDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;

    static final String DATABASE_NAME = "finantial.db";

    public FinantialDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOEDA_TABLE = "CREATE TABLE " + MoedaEntry.TABLE_NAME + " (" +
                MoedaEntry._ID               + " INTEGER PRIMARY KEY," +
                MoedaEntry.COLUMN_MOEDA_CODE + " TEXT UNIQUE NOT NULL, " +
                MoedaEntry.COLUMN_MOEDA_NAME + " TEXT UNIQUE NOT NULL, " +
                MoedaEntry.COLUMN_MOEDA_DATE + " INTEGER NOT NULL, " +
                MoedaEntry.COLUMN_MOEDA_RATE + " REAL NOT NULL " +
                " );";

        final String SQL_CREATE_INDICE_TABLE = "CREATE TABLE " + IndiceEntry.TABLE_NAME + " (" +
                IndiceEntry._ID                      + " INTEGER PRIMARY KEY," +
                IndiceEntry.COLUMN_INDICE_CODE       + " TEXT UNIQUE NOT NULL, " +
                IndiceEntry.COLUMN_INDICE_NAME       + " TEXT UNIQUE NOT NULL, " +
                IndiceEntry.COLUMN_INDICE_DATE       + " INTEGER NOT NULL, " +
                IndiceEntry.COLUMN_INDICE_MONTH_RATE + " REAL NOT NULL, " +
                IndiceEntry.COLUMN_INDICE_YEAR_RATE  + " REAL NOT NULL, " +
                IndiceEntry.COLUMN_INDICE_TYPE       + " INTEGER NOT NULL " +
                " );";

        final String SQL_CREATE_TESOURO_TABLE = "CREATE TABLE " + TesouroEntry.TABLE_NAME + " (" +
                TesouroEntry._ID                             + " INTEGER PRIMARY KEY," +
                TesouroEntry.COLUMN_TESOURO_NAME             + " TEXT UNIQUE NOT NULL, " +
                TesouroEntry.COLUMN_TESOURO_MODE             + " TEXT UNIQUE NOT NULL, " +
                TesouroEntry.COLUMN_TESOURO_YEAR             + " INTEGER NOT NULL, " +
                TesouroEntry.COLUMN_TESOURO_EXPIRATION_DATE  + " INTEGER NOT NULL, " +
                TesouroEntry.COLUMN_TESOURO_BUYING_INCOME    + " REAL, " +
                TesouroEntry.COLUMN_TESOURO_SELLING_INCOME   + " REAL NOT NULL, " +
                TesouroEntry.COLUMN_TESOURO_BUYING_PRICE     + " REAL, " +
                TesouroEntry.COLUMN_TESOURO_SELLING_PRICE    + " REAL NOT NULL, " +
                TesouroEntry.COLUMN_TESOURO_BUYING_MIN_VALUE + " REAL " +
                " );";

        db.execSQL(SQL_CREATE_MOEDA_TABLE);
        db.execSQL(SQL_CREATE_INDICE_TABLE);
        db.execSQL(SQL_CREATE_TESOURO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoedaEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + IndiceEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TesouroEntry.TABLE_NAME);
        onCreate(db);
    }
}
