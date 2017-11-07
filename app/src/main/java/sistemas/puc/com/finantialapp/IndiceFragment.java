package sistemas.puc.com.finantialapp;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sistemas.puc.com.finantialapp.adapters.AbstractCursorAdapter;
import sistemas.puc.com.finantialapp.adapters.IndiceCursorAdapter;
import sistemas.puc.com.finantialapp.data.FinantialContract.IndiceEntry;
import sistemas.puc.com.finantialapp.model.Database;
import sistemas.puc.com.finantialapp.util.DividerItemDecoration;

public class IndiceFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final Uri INDICE_URI = IndiceEntry.CONTENT_URI;

    private static final String[] INDICE_COLUMNS = new String[]{
            IndiceEntry.TABLE_NAME + "." + IndiceEntry._ID,
            IndiceEntry.TABLE_NAME + "." + IndiceEntry.COLUMN_INDICE_CODE,
            IndiceEntry.TABLE_NAME + "." + IndiceEntry.COLUMN_INDICE_NAME,
            IndiceEntry.TABLE_NAME + "." + IndiceEntry.COLUMN_INDICE_DATE,
            IndiceEntry.TABLE_NAME + "." + IndiceEntry.COLUMN_INDICE_MONTH_RATE,
            IndiceEntry.TABLE_NAME + "." + IndiceEntry.COLUMN_INDICE_YEAR_RATE,
            IndiceEntry.TABLE_NAME + "." + IndiceEntry.COLUMN_INDICE_TYPE,
    };

    private RecyclerView m_recyclerView;
    private AbstractCursorAdapter m_adapter;
    private RecyclerView.LayoutManager m_layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_indice, container, false);

        // Update database on startup
        Database.update(getContext());

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity());
        itemDecoration.setLastItemIncluded(false);
        itemDecoration.setPaddingLeftDp(32);
        itemDecoration.setPaddingRightDp(32);

        m_recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_indice);
        m_recyclerView.addItemDecoration(itemDecoration);
        m_recyclerView.setHasFixedSize(true);

        m_layoutManager = new LinearLayoutManager(getContext());
        m_recyclerView.setLayoutManager(m_layoutManager);

        m_adapter = new IndiceCursorAdapter(getContext(), null);

        m_recyclerView.setAdapter(m_adapter);

        getLoaderManager().initLoader(0, null, this);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                INDICE_URI,
                INDICE_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        m_adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        m_adapter.swapCursor(null);
    }
}
