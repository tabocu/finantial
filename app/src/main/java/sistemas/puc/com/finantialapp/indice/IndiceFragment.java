package sistemas.puc.com.finantialapp.indice;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sistemas.puc.com.finantialapp.R;
import sistemas.puc.com.finantialapp.adapters.AbstractCursorAdapter;
import sistemas.puc.com.finantialapp.model.Database;
import sistemas.puc.com.finantialapp.util.DividerItemDecoration;

public class IndiceFragment extends Fragment {

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

        getLoaderManager().initLoader(
                0,
                null,
                new IndiceLoaderCallback(getActivity(), m_adapter));

        return rootView;
    }
}
