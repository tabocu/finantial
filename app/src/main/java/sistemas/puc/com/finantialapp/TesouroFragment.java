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

import sistemas.puc.com.finantialapp.adapters.TesouroAdapter;

public class TesouroFragment extends Fragment {

    TesouroAdapter m_tesouroAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TesouroItem[] data = {
                new TesouroItem(
                        "Tesouro IPCA+ 2024",
                        "15/08/2024",
                        "4,66","4,78",
                        "2.194,90","2.177,62"),
                new TesouroItem(
                        "Tesouro IPCA+ 2035",
                        "15/05/2035",
                        "5,01","5,13",
                        "1.270,65","1.245,36"),
                new TesouroItem(
                        "Tesouro IPCA+ 2045",
                        "15/05/2045",
                        "5,01","5,13",
                        "780,38","756,19"),
                new TesouroItem(
                        "Tesouro SELIC 2023",
                        "15/05/2045",
                        "0,01","0,05",
                        "9.077,87","9.058,13"),
        };

        List<TesouroItem> tesouroList = new ArrayList<>(Arrays.asList(data));

        m_tesouroAdapter = new TesouroAdapter(getActivity(), tesouroList);

        View rootView = inflater.inflate(R.layout.fragment_tesouro, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_tesouro);
        listView.setAdapter(m_tesouroAdapter);

        return rootView;
    }
}
