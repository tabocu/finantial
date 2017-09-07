package sistemas.puc.com.finantialapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoedaFragment extends Fragment {

    ArrayAdapter<String> m_moedaAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String[] data = {
                "Dolar - R$3,1133 - 06/09/2017",
                "Libra - R$4,0628 - 06/09/2017",
                "Euro  - R$3,7145 - 06/09/2017",
        };

        List<String> moedaList = new ArrayList<String>(Arrays.asList(data));

        m_moedaAdapter = new ArrayAdapter<String>(
                getActivity(), // The current context (this activity)
                R.layout.list_item_moeda, // The name of the layout ID.
                R.id.list_item_moeda_textview, // The ID of the textview to populate.
                moedaList);

        View rootView = inflater.inflate(R.layout.fragment_moeda, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_moeda);
        listView.setAdapter(m_moedaAdapter);

        return rootView;
    }
}
