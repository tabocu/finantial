package sistemas.puc.com.finantialapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import java.util.List;

import sistemas.puc.com.finantialapp.adapters.MoedaAdapter;
import sistemas.puc.com.finantialapp.entities.MoedaItem;
import sistemas.puc.com.finantialapp.model.Database;

public class MoedaFragment extends Fragment {

    private static final String DOLAR = "Dolar";
    MoedaAdapter m_moedaAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        List<MoedaItem> moedaList = Database.getMoedaList();

        m_moedaAdapter = new MoedaAdapter(getActivity(), moedaList);

        View rootView = inflater.inflate(R.layout.fragment_moeda, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_moeda);
        listView.setAdapter(m_moedaAdapter);
        registerForContextMenu(listView);

        return rootView;
    }
}
