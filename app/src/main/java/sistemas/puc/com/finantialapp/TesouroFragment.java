package sistemas.puc.com.finantialapp;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sistemas.puc.com.finantialapp.adapters.TesouroAdapter;
import sistemas.puc.com.finantialapp.entities.TesouroItem;
import sistemas.puc.com.finantialapp.model.Database;

public class TesouroFragment extends Fragment {

    TesouroAdapter m_tesouroAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        List<TesouroItem> tesouroList = Database.getTesouroList();

        m_tesouroAdapter = new TesouroAdapter(getActivity(), tesouroList);

        View rootView = inflater.inflate(R.layout.fragment_tesouro, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_tesouro);
        listView.setAdapter(m_tesouroAdapter);

        return rootView;
    }
}
