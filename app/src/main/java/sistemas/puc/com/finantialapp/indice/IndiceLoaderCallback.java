package sistemas.puc.com.finantialapp.indice;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import sistemas.puc.com.finantialapp.adapters.AbstractCursorAdapter;
import sistemas.puc.com.finantialapp.data.FinantialContract.IndiceEntry;

public class IndiceLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {

    static final Uri URI = IndiceEntry.CONTENT_URI;

    static final String[] COLUMNS = new String[] {
            IndiceEntry.TABLE_NAME + "." + IndiceEntry._ID,
            IndiceEntry.TABLE_NAME + "." + IndiceEntry.COLUMN_INDICE_CODE,
            IndiceEntry.TABLE_NAME + "." + IndiceEntry.COLUMN_INDICE_NAME,
            IndiceEntry.TABLE_NAME + "." + IndiceEntry.COLUMN_INDICE_DATE,
            IndiceEntry.TABLE_NAME + "." + IndiceEntry.COLUMN_INDICE_MONTH_RATE,
            IndiceEntry.TABLE_NAME + "." + IndiceEntry.COLUMN_INDICE_YEAR_RATE,
            IndiceEntry.TABLE_NAME + "." + IndiceEntry.COLUMN_INDICE_TYPE,
    };

    Context mContext;
    AbstractCursorAdapter mAdapter;

    public IndiceLoaderCallback(Context context,
                                AbstractCursorAdapter adapter) {
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
                null);
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
