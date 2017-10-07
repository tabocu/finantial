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
import sistemas.puc.com.finantialapp.entities.MoedaItem;
import sistemas.puc.com.finantialapp.model.Database;

public class MoedaFragment extends Fragment {

    private RecyclerView m_recyclerView;
    private RecyclerView.Adapter m_adapter;
    private RecyclerView.LayoutManager m_layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_moeda, container, false);

        m_recyclerView = (RecyclerView) rootView.findViewById(R.id.listview_moeda);
        m_recyclerView.setHasFixedSize(true);

        m_layoutManager = new LinearLayoutManager(getContext());
        m_recyclerView.setLayoutManager(m_layoutManager);

        // get data set
        List<MoedaItem> moedaList = Database.getMoedaList();

        m_adapter = new MoedaAdapter(moedaList);

        m_recyclerView.setAdapter(m_adapter);

        return rootView;
    }
}
