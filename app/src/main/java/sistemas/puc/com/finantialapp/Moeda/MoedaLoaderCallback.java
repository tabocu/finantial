package sistemas.puc.com.finantialapp.Moeda;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import sistemas.puc.com.finantialapp.adapters.AbstractCursorAdapter;
import sistemas.puc.com.finantialapp.data.FinantialContract.MoedaEntry;

public class MoedaLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {

    static final Uri URI = MoedaEntry.CONTENT_URI;

    static final String[] COLUMNS = new String[]{
            MoedaEntry.TABLE_NAME + "." + MoedaEntry._ID,
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_CODE,
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_NAME,
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_RATE,
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_FAVORITE,
    };

    static final String SELECTION =
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_CODE + " <> ?";

    static final String SORT_ORDER =
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_FAVORITE + " DESC, " +
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_NAME + " ASC ";

    Context mContext;
    AbstractCursorAdapter mAdapter;
    String mMoedaBase;

    public MoedaLoaderCallback(Context context,
                               AbstractCursorAdapter adapter,
                               String moedaBase) {
        mContext = context;
        mAdapter = adapter;
        mMoedaBase = moedaBase;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                mContext,
                URI,
                COLUMNS,
                SELECTION,
                new String[] { mMoedaBase },
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
