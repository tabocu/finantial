package sistemas.puc.com.finantialapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import sistemas.puc.com.finantialapp.R;
import sistemas.puc.com.finantialapp.entities.MoedaItem;

public class MoedaAdapter extends ArrayAdapter<MoedaItem> {

    public MoedaAdapter(@NonNull Context context, @NonNull List<MoedaItem> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_moeda, parent, false);
        } else {
            view = convertView;
        }

        final TextView nome = (TextView) view.findViewById(R.id.list_item_nome_moeda_textview);
        final TextView cotacao = (TextView) view.findViewById(R.id.list_item_cotacao_moeda_textview);
        final TextView data = (TextView) view.findViewById(R.id.list_item_data_moeda_textview);

        final MoedaItem moedaItem = getItem(position);

        nome.setText(moedaItem.m_nome);
        cotacao.setText(moedaItem.m_cotacao);
        data.setText(moedaItem.m_data);

        return view;
    }
}