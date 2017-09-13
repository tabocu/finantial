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

public class IndiceAdapter extends ArrayAdapter<IndiceItem> {

    private static final int VIEW_TYPE_COUNT = 2;
    private static final int VIEW_TYPE_INDICE_SIMPLES = 0;
    private static final int VIEW_TYPE_INDICE_DUPLO = 1;

    public IndiceAdapter(@NonNull Context context, @NonNull List<IndiceItem> objects) {
        super(context, 0, objects);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position) instanceof IndiceItem.Simples
                ? VIEW_TYPE_INDICE_SIMPLES : VIEW_TYPE_INDICE_DUPLO;
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
            case VIEW_TYPE_INDICE_SIMPLES: {
                layoutId = R.layout.list_item_indice_simples;
                break;
            }
            case VIEW_TYPE_INDICE_DUPLO: {
                layoutId = R.layout.list_item_indice_duplo;
                break;
            }
        }
        return LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
    }

    private void bindView(int position, View view) {
        ((TextView) view.findViewById(R.id.list_item_nome_indice_textview))
                .setText(getItem(position).m_nome);
        int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_INDICE_SIMPLES: {
                IndiceItem.Simples indiceItem = (IndiceItem.Simples) getItem(position);
                ((TextView) view.findViewById(R.id.list_item_taxa_indice_textview))
                        .setText(indiceItem.m_taxa);
                ((TextView) view.findViewById(R.id.list_item_data_indice_textview))
                        .setText(indiceItem.m_data);
                break;
            }
            case VIEW_TYPE_INDICE_DUPLO: {
                IndiceItem.Duplo indiceItem = (IndiceItem.Duplo) getItem(position);
                ((TextView) view.findViewById(R.id.list_item_taxa_mensal_indice_textview))
                        .setText(indiceItem.m_taxaMensal);
                ((TextView) view.findViewById(R.id.list_item_mes_indice_textview))
                        .setText(indiceItem.m_nomeMensal);
                ((TextView) view.findViewById(R.id.list_item_taxa_anual_indice_textview))
                        .setText(indiceItem.m_taxaAnual);
                ((TextView) view.findViewById(R.id.list_item_ano_indice_textview))
                        .setText(indiceItem.m_nomeAnual);
                break;
            }
        }
    }
}