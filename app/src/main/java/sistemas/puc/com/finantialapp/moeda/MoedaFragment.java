package sistemas.puc.com.finantialapp.moeda;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sistemas.puc.com.finantialapp.R;
import sistemas.puc.com.finantialapp.adapters.RecyclerItemClickAdapter;
import sistemas.puc.com.finantialapp.model.Database;
import sistemas.puc.com.finantialapp.util.DividerItemDecoration;

public class MoedaFragment extends Fragment {

    public static final String MOEDA_BASE = "BRL";

    private RecyclerView m_recyclerView;
    private MoedaCursorAdapter m_adapter;
    private RecyclerView.LayoutManager m_layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_moeda, container, false);

        // Update database on startup
        Database.update(getContext());

        m_adapter = new MoedaCursorAdapter(getContext(), null);
        m_layoutManager = new LinearLayoutManager(getContext());

        m_recyclerView = (RecyclerView) rootView.findViewById(R.id.listview_moeda);
        m_recyclerView.setAdapter(m_adapter);
        m_recyclerView.setLayoutManager(m_layoutManager);
        m_recyclerView.setHasFixedSize(true);
        m_recyclerView.addItemDecoration(getItemDecoration());
        m_recyclerView.addOnItemTouchListener(getTouchListener());

        getLoaderManager().initLoader(0, null, getLoaderCallback());

        return rootView;
    }

    private DividerItemDecoration getItemDecoration() {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity());
        itemDecoration.setLastItemIncluded(false);
        // TODO: Fix hardcoded numbers
        itemDecoration.setPaddingLeftDp(72);
        itemDecoration.setPaddingRightDp(16);
        return itemDecoration;
    }

    private RecyclerItemClickAdapter getTouchListener() {
        return new RecyclerItemClickAdapter(
                getContext(),
                m_recyclerView,
                new MoedaActionCallback(
                        getActivity(),
                        m_adapter,
                        MOEDA_BASE));
    }

    private LoaderManager.LoaderCallbacks<Cursor> getLoaderCallback() {
        return new MoedaLoaderCallback(
                getActivity(),
                m_adapter,
                MOEDA_BASE);
    }
}
