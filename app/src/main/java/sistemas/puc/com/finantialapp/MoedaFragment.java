package sistemas.puc.com.finantialapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sistemas.puc.com.finantialapp.adapters.MoedaAdapter;
import sistemas.puc.com.finantialapp.entities.MoedaItem;

public class MoedaFragment extends Fragment {

    MoedaAdapter m_moedaAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MoedaItem[] data = {
            new MoedaItem("Dolar", "R$3,1133", "06/09/2017"),
            new MoedaItem("Libra", "R$4,0628", "06/09/2017"),
            new MoedaItem("Euro", "R$3,7145", "06/09/2017")
        };

        List<MoedaItem> moedaList = new ArrayList<>(Arrays.asList(data));

        m_moedaAdapter = new MoedaAdapter(getActivity(), moedaList);

        View rootView = inflater.inflate(R.layout.fragment_moeda, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_moeda);
        listView.setAdapter(m_moedaAdapter);

        return rootView;
    }
}
