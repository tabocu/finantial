package sistemas.puc.com.finantialapp.tesouro;

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

import sistemas.puc.com.finantialapp.R;
import sistemas.puc.com.finantialapp.adapters.AbstractCursorAdapter;
import sistemas.puc.com.finantialapp.model.Database;
import sistemas.puc.com.finantialapp.util.DividerItemDecoration;
import sistemas.puc.com.finantialapp.data.FinantialContract.TesouroEntry;

public class TesouroFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final Uri TESOURO_URI = TesouroEntry.CONTENT_URI;

    private static final String[] TESOURO_COLUMNS = new String[]{
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

    private RecyclerView m_recyclerView;
    private AbstractCursorAdapter m_adapter;
    private RecyclerView.LayoutManager m_layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tesouro, container, false);

        // Update database on startup
        Database.update(getContext());

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity());
        itemDecoration.setLastItemIncluded(false);
        itemDecoration.setPaddingLeftDp(32);
        itemDecoration.setPaddingRightDp(32);

        m_recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_tesouro);
        m_recyclerView.addItemDecoration(itemDecoration);
        m_recyclerView.setHasFixedSize(true);

        m_layoutManager = new LinearLayoutManager(getContext());
        m_recyclerView.setLayoutManager(m_layoutManager);

        m_adapter = new TesouroCursorAdapter(getContext(), null);

        m_recyclerView.setAdapter(m_adapter);

        getLoaderManager().initLoader(0, null, this);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                TESOURO_URI,
                TESOURO_COLUMNS,
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
