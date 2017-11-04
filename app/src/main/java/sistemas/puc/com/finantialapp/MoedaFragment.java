package sistemas.puc.com.finantialapp;

import android.content.Intent;
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

import sistemas.puc.com.finantialapp.adapters.MoedaCursorAdapter;
import sistemas.puc.com.finantialapp.adapters.RecyclerItemClickAdapter;
import sistemas.puc.com.finantialapp.data.FinantialContract.MoedaEntry;
import sistemas.puc.com.finantialapp.model.Database;
import sistemas.puc.com.finantialapp.util.DividerItemDecoration;

public class MoedaFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        RecyclerItemClickAdapter.OnItemClickListener {

    private static final String DOLAR = "Dolar";

    private static final Uri MOEDA_URI = MoedaEntry.CONTENT_URI;

    private static final String[] MOEDA_COLUMNS = new String[]{
            MoedaEntry.TABLE_NAME + "." + MoedaEntry._ID,
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_CODE,
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_NAME,
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_RATE,
    };

    private RecyclerView m_recyclerView;
    private MoedaCursorAdapter m_adapter;
    private RecyclerView.LayoutManager m_layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_moeda, container, false);

        // Update database on startup
        Database.update(getContext());

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity());
        itemDecoration.setLastItemIncluded(false);
        // TODO: Fix hardcoded numbers
        itemDecoration.setPaddingLeftDp(72);
        itemDecoration.setPaddingRightDp(16);

        m_recyclerView = (RecyclerView) rootView.findViewById(R.id.listview_moeda);
        m_recyclerView.addItemDecoration(itemDecoration);
        m_recyclerView.setHasFixedSize(true);
        m_recyclerView.addOnItemTouchListener(
                new RecyclerItemClickAdapter(getContext(), m_recyclerView, this));

        m_layoutManager = new LinearLayoutManager(getContext());
        m_recyclerView.setLayoutManager(m_layoutManager);

        m_adapter = new MoedaCursorAdapter(getContext(), null);

        m_recyclerView.setAdapter(m_adapter);

        getLoaderManager().initLoader(0, null, this);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                MOEDA_URI,
                MOEDA_COLUMNS,
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

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getContext(), ConversaoActivity.class);
        intent.putExtra(ConversaoActivity.EXTRA_MOEDA_ESQ, DOLAR);
        intent.putExtra(ConversaoActivity.EXTRA_MOEDA_DIR, DOLAR);
        startActivity(intent);
    }

    @Override
    public void onLongItemClick(View view, int position) {

    }
}
