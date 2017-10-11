package sistemas.puc.com.finantialapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import sistemas.puc.com.finantialapp.adapters.MoedaAdapter;
import sistemas.puc.com.finantialapp.adapters.MoedaCursorAdapter;
import sistemas.puc.com.finantialapp.data.FinantialContract;
import sistemas.puc.com.finantialapp.data.FinantialProvider;
import sistemas.puc.com.finantialapp.entities.MoedaItem;
import sistemas.puc.com.finantialapp.model.Database;
import sistemas.puc.com.finantialapp.util.DividerItemDecoration;

public class MoedaFragment extends Fragment {

    private RecyclerView m_recyclerView;
    private RecyclerView.Adapter m_adapter;
    private RecyclerView.LayoutManager m_layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_moeda, container, false);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity());
        itemDecoration.setLastItemIncluded(false);
        itemDecoration.setPaddingLeftDp(72);
        itemDecoration.setPaddingRightDp(16);

        m_recyclerView = (RecyclerView) rootView.findViewById(R.id.listview_moeda);
        m_recyclerView.addItemDecoration(itemDecoration);
        m_recyclerView.setHasFixedSize(true);

        m_layoutManager = new LinearLayoutManager(getContext());
        m_recyclerView.setLayoutManager(m_layoutManager);

        m_adapter = new MoedaCursorAdapter(getContext(),
                getContext().getContentResolver().query(
                        FinantialContract.MoedaEntry.CONTENT_URI,
                        new String[]{
                                FinantialContract.MoedaEntry.COLUMN_MOEDA_CODE,
                                FinantialContract.MoedaEntry.COLUMN_MOEDA_DATE,
                                FinantialContract.MoedaEntry.COLUMN_MOEDA_NAME,
                                FinantialContract.MoedaEntry.COLUMN_MOEDA_RATE,
                        },
                        null,null,null));

        m_recyclerView.setAdapter(m_adapter);

        return rootView;
    }
}
