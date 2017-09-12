package sistemas.puc.com.finantialapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IndiceFragment extends Fragment {

    ArrayAdapter<String> m_indiceAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String[] data = {
                "CDI       -  9,14% - 06/09/2017",
                "SELIC     -  9,15% - 06/09/2017",
                "IPCA      -  0,24% - 06/09/2017",
                "INPC      - -0,03% - 06/09/2017",
                "IGP-M     -  0,10% - 06/09/2017",
                "Poupança  -  0,50% - 06/09/2017",
        };

        List<String> indiceList = new ArrayList<String>(Arrays.asList(data));

        m_indiceAdapter = new ArrayAdapter<String>(
                getActivity(), // The current context (this activity)
                R.layout.list_item_indice, // The name of the layout ID.
                R.id.list_item_indice_textview, // The ID of the textview to populate.
                indiceList);

        View rootView = inflater.inflate(R.layout.fragment_indice, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_indice);
        listView.setAdapter(m_indiceAdapter);

        return rootView;
    }
}
