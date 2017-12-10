package sistemas.puc.com.finantialapp.conversao;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import sistemas.puc.com.finantialapp.data.FinantialContract.MoedaEntry;

public class ConversaoLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {

    static final int COLUMN_INDEX_CODE = 1;

    static final Uri URI = MoedaEntry.CONTENT_URI;

    static final String[] COLUMNS = new String[]{
            MoedaEntry.TABLE_NAME + "." + MoedaEntry._ID,
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_CODE,
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_NAME,
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_RATE,
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_FAVORITE,
    };

    static final String SORT_ORDER =
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_FAVORITE + " DESC, " +
                    MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_NAME + " ASC ";

    Context mContext;
    Spinner mSpinner;
    String mCode;

    public ConversaoLoaderCallback(Context context,
                                   Spinner spinner,
                                   String code) {
        mContext = context;
        mSpinner = spinner;
        mCode = code;
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
        ((SimpleCursorAdapter) mSpinner.getAdapter()).swapCursor(data);

        int position = getPositionByCode(mCode);
        if (position == -1)
            return;

        mSpinner.setSelection(position);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((SimpleCursorAdapter) mSpinner.getAdapter()).swapCursor(null);
    }

    private int getPositionByCode(String code) {
        Cursor cursor = ((SimpleCursorAdapter) mSpinner.getAdapter()).getCursor();

        if (cursor == null)
            return -1;

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            if (cursor.getString(COLUMN_INDEX_CODE).equals(code)) {
                return cursor.getPosition();
            }
            cursor.moveToNext();
        }

        return -1;
    }
}
