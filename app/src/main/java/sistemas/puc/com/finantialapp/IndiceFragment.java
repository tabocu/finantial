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

public class IndiceFragment extends Fragment {

    IndiceAdapter m_indiceAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        IndiceItem[] data = {
                new IndiceItem.Simples("CDI", "11/09/2017", "8,14%"),
                new IndiceItem.Simples("SELIC", "11/09/2017", "8,15%"),
                new IndiceItem.Duplo("Poupança", "Setembro", "0,50%", "2017", "5,22%"),
                new IndiceItem.Duplo("IPCA", "Agosto", "0,19%", "2017", "1,62%"),
                new IndiceItem.Duplo("INPC", "Agosto", "-0,03%", "2017", "1,27%"),
                new IndiceItem.Duplo("IGP-M", "Agosto", "0,10%", "2017", "-2,57%"),
        };

        List<IndiceItem> indiceList = new ArrayList<>(Arrays.asList(data));

        m_indiceAdapter = new IndiceAdapter(getActivity(), indiceList);

        View rootView = inflater.inflate(R.layout.fragment_indice, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_indice);
        listView.setAdapter(m_indiceAdapter);

        return rootView;
    }
}
