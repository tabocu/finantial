package sistemas.puc.com.finantialapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sistemas.puc.com.finantialapp.R;
import sistemas.puc.com.finantialapp.entities.MoedaItem;

public class MoedaAdapter extends AbstractListAdapter<MoedaItem,MoedaAdapter.ViewHolder> {

    public MoedaAdapter(@NonNull List<MoedaItem> dataSet) {
        super(dataSet);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nomeMoeda;
        public TextView cotacaoMoeda;
        public TextView dataMoeda;

        public ViewHolder(View itemView) {
            super(itemView);
            nomeMoeda = (TextView) itemView.findViewById(R.id.list_item_nome_moeda_textview);
            cotacaoMoeda = (TextView) itemView.findViewById(R.id.list_item_cotacao_moeda_textview);
            dataMoeda = (TextView) itemView.findViewById(R.id.list_item_data_moeda_textview);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_moeda, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nomeMoeda.setText(getItem(position).m_nome);
        holder.cotacaoMoeda.setText(getItem(position).m_cotacao);
        holder.dataMoeda.setText(getItem(position).m_data);
    }
}
