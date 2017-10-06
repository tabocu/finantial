package sistemas.puc.com.finantialapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sistemas.puc.com.finantialapp.adapters.IndiceAdapter;
import sistemas.puc.com.finantialapp.entities.IndiceItem;
import sistemas.puc.com.finantialapp.model.Database;

public class IndiceFragment extends Fragment {

    IndiceAdapter m_indiceAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        List<IndiceItem> indiceList = Database.getIndiceList();

        m_indiceAdapter = new IndiceAdapter(getActivity(), indiceList);

        View rootView = inflater.inflate(R.layout.fragment_indice, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_indice);
        listView.setAdapter(m_indiceAdapter);

        return rootView;
    }
}
