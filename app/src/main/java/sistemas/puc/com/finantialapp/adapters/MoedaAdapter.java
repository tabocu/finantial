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
import sistemas.puc.com.finantialapp.util.Util;

public class MoedaAdapter extends AbstractListAdapter<MoedaItem,MoedaAdapter.ViewHolder> {

    public MoedaAdapter(@NonNull List<MoedaItem> dataSet) {
        super(dataSet);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView codigoMoeda;
        public TextView nomeMoeda;
        public TextView cotacaoMoeda;
        public TextView diaMoeda;
        public TextView mesMoeda;

        public ViewHolder(View itemView) {
            super(itemView);
            codigoMoeda = (TextView) itemView.findViewById(R.id.item_moeda_code_textview);
            nomeMoeda = (TextView) itemView.findViewById(R.id.item_moeda_name_textview);
            cotacaoMoeda = (TextView) itemView.findViewById(R.id.item_moeda_value_textview);
            diaMoeda = (TextView) itemView.findViewById(R.id.item_moeda_day_textview);
            mesMoeda = (TextView) itemView.findViewById(R.id.item_moeda_month_textview);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moeda, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.codigoMoeda.setText(getItem(position).getCodigo());
        holder.nomeMoeda.setText(getItem(position).getNome());

        double cotacao = getItem(position).getCotacao();
        holder.cotacaoMoeda.setText(Util.getRealStringFromDouble(cotacao));

        long time = getItem(position).getTime();
        holder.diaMoeda.setText(Util.getDayStringFromLong(time));
        holder.mesMoeda.setText(Util.getMonthStringFromLong(time));
    }
}
