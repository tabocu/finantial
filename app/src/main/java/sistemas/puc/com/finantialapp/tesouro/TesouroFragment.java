package sistemas.puc.com.finantialapp.tesouro;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class TesouroFragment extends Fragment {

    private RecyclerView m_recyclerView;
    private AbstractCursorAdapter m_adapter;
    private RecyclerView.LayoutManager m_layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tesouro, container, false);

        // Update database on startup
        Database.update(getContext());
        
        m_adapter = new TesouroCursorAdapter(getContext());
        m_layoutManager = new LinearLayoutManager(getContext());

        m_recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_tesouro);
        m_recyclerView.setAdapter(m_adapter);
        m_recyclerView.setLayoutManager(m_layoutManager);
        m_recyclerView.setHasFixedSize(true);
        m_recyclerView.addItemDecoration(getItemDecoration());

        getLoaderManager().initLoader(
                0,
                null,
                new TesouroLoaderCallback(getActivity(), m_adapter));

        return rootView;
    }

    @NonNull
    private DividerItemDecoration getItemDecoration() {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity());
        itemDecoration.setLastItemIncluded(false);
        // TODO: Fix hardcoded numbers
        itemDecoration.setPaddingLeftDp(32);
        itemDecoration.setPaddingRightDp(32);
        return itemDecoration;
    }
}
