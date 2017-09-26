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
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

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
        registerForContextMenu(listView);

        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.listview_moeda) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.moeda_context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.conversao:
                Intent intent = new Intent(getContext(), ConversaoActivity.class);
                //intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
