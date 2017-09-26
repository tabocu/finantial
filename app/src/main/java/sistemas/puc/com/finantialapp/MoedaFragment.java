package sistemas.puc.com.finantialapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sistemas.puc.com.finantialapp.adapters.MoedaAdapter;
import sistemas.puc.com.finantialapp.entities.MoedaItem;
import sistemas.puc.com.finantialapp.model.Database;

public class MoedaFragment extends Fragment {

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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.listview_moeda) {
            MenuInflater inflater = getActivity().getMenuInflater();
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
            if (info.position == 0) {
                inflater.inflate(R.menu.moeda_principal_context_menu, menu);
            } else {
                inflater.inflate(R.menu.moeda_context_menu, menu);
            }
            menu.setHeaderTitle(m_moedaAdapter.getItem(info.position).m_nome);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.conversao:
                Intent intent = new Intent(getContext(), ConversaoActivity.class);
                //intent.putExtra("position", info.position);
                startActivity(intent);
                return true;
            case R.id.principal:
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
