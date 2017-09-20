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
import sistemas.puc.com.finantialapp.entities.IndiceItem;
import sistemas.puc.com.finantialapp.entities.MoedaItem;

public class MoedaAdapter extends ArrayAdapter<MoedaItem> {

    private static final int VIEW_TYPE_COUNT = 2;
    private static final int VIEW_TYPE_MOEDA_PRINCIPAL = 0;
    private static final int VIEW_TYPE_MOEDA = 1;

    public MoedaAdapter(@NonNull Context context, @NonNull List<MoedaItem> objects) {
        super(context, 0, objects);
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_MOEDA_PRINCIPAL : VIEW_TYPE_MOEDA;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = newView(position, parent);
        } else {
            view = convertView;
        }
        bindView(position,view);
        return view;
    }

    private View newView(int position, ViewGroup parent) {
        int viewType = getItemViewType(position);
        int layoutId = -1;
        switch (viewType) {
            case VIEW_TYPE_MOEDA_PRINCIPAL: {
                layoutId = R.layout.list_item_moeda_principal;
                break;
            }
            case VIEW_TYPE_MOEDA: {
                layoutId = R.layout.list_item_moeda;
                break;
            }
        }
        return LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
    }

    private void bindView(int position, View view) {
        ((TextView) view.findViewById(R.id.list_item_nome_moeda_textview)).setText(getItem(position).m_nome);
        ((TextView) view.findViewById(R.id.list_item_cotacao_moeda_textview)).setText(getItem(position).m_cotacao);
        ((TextView) view.findViewById(R.id.list_item_data_moeda_textview)).setText(getItem(position).m_data);
    }
}