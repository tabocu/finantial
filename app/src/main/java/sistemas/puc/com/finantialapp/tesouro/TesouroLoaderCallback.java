package sistemas.puc.com.finantialapp.tesouro;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import sistemas.puc.com.finantialapp.adapters.AbstractCursorAdapter;
import sistemas.puc.com.finantialapp.data.FinantialContract.TesouroEntry;

public class TesouroLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {

    static final Uri URI = TesouroEntry.CONTENT_URI;

    static final String[] COLUMNS = new String[]{
            TesouroEntry.TABLE_NAME + "." + TesouroEntry._ID,
            TesouroEntry.TABLE_NAME + "." + TesouroEntry.COLUMN_TESOURO_NAME,
            TesouroEntry.TABLE_NAME + "." + TesouroEntry.COLUMN_TESOURO_MODE,
            TesouroEntry.TABLE_NAME + "." + TesouroEntry.COLUMN_TESOURO_YEAR,
            TesouroEntry.TABLE_NAME + "." + TesouroEntry.COLUMN_TESOURO_EXPIRATION_DATE,
            TesouroEntry.TABLE_NAME + "." + TesouroEntry.COLUMN_TESOURO_BUYING_INCOME,
            TesouroEntry.TABLE_NAME + "." + TesouroEntry.COLUMN_TESOURO_SELLING_INCOME,
            TesouroEntry.TABLE_NAME + "." + TesouroEntry.COLUMN_TESOURO_BUYING_PRICE,
            TesouroEntry.TABLE_NAME + "." + TesouroEntry.COLUMN_TESOURO_BUYING_MIN_VALUE,
            TesouroEntry.TABLE_NAME + "." + TesouroEntry.COLUMN_TESOURO_SELLING_PRICE,
    };

    static final String SORT_ORDER =
            TesouroEntry.TABLE_NAME + "." + TesouroEntry.COLUMN_TESOURO_YEAR + " ASC ";

    Context mContext;
    AbstractCursorAdapter mAdapter;

    public TesouroLoaderCallback(Context context, AbstractCursorAdapter adapter) {
        mContext = context;
        mAdapter = adapter;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                mContext,
                URI,
                COLUMNS,
                null,
                null,
                SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
