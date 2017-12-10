package sistemas.puc.com.finantialapp.indice;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sistemas.puc.com.finantialapp.R;
import sistemas.puc.com.finantialapp.adapters.AbstractCursorAdapter;
import sistemas.puc.com.finantialapp.model.IndiceEnum;
import sistemas.puc.com.finantialapp.util.Util;

public class IndiceCursorAdapter extends AbstractCursorAdapter<IndiceCursorAdapter.ViewHolder> {

    static final int VIEW_TYPE_COUNT = 2;

    static final int COLUMN_INDICE_CODE = 1;
    static final int COLUMN_INDICE_NAME = 2;
    static final int COLUMN_INDICE_DATE = 3;
    static final int COLUMN_INDICE_MONTH_RATE = 4;
    static final int COLUMN_INDICE_YEAR_RATE = 5;
    static final int COLUMN_INDICE_TYPE = 6;

    public IndiceCursorAdapter(@NonNull Context context) {
        super(context, null);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView codigoIndice;
        public TextView nomeIndice;

        public TextView dataIndice;
        public TextView taxaIndice;

        public TextView mesIndice;
        public TextView taxaMesIndice;
        public TextView anoIndice;
        public TextView taxaAnoIndice;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            codigoIndice = (TextView) itemView.findViewById(R.id.item_indice_codigo_textview);
            nomeIndice = (TextView) itemView.findViewById(R.id.item_indice_nome_textview);
            IndiceEnum.Type indiceType = IndiceEnum.Type.values()[viewType];
            switch(indiceType) {
                case SIMPLE:
                    dataIndice = (TextView) itemView.findViewById(R.id.item_indice_data_textview);
                    taxaIndice = (TextView) itemView.findViewById(R.id.item_indice_taxa_textview);
                    break;
                case DOUBLE:
                    mesIndice = (TextView) itemView.findViewById(R.id.item_indice_mes_textview);
                    taxaMesIndice = (TextView) itemView.findViewById(R.id.item_indice_taxa_mensal_textview);
                    anoIndice = (TextView) itemView.findViewById(R.id.item_indice_ano_textview);
                    taxaAnoIndice = (TextView) itemView.findViewById(R.id.item_indice_taxa_anual_textview);
                    break;
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        int layoutId = -1;
        IndiceEnum.Type indiceType = IndiceEnum.Type.values()[type];
        switch(indiceType) {
            case SIMPLE:
                layoutId = R.layout.item_indice_simples;
                break;
            case DOUBLE:
                layoutId = R.layout.item_indice_duplo;
                break;
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        ViewHolder holder = new ViewHolder(v,type);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        holder.codigoIndice.setText(cursor.getString(COLUMN_INDICE_CODE));
        holder.nomeIndice.setText(cursor.getString(COLUMN_INDICE_NAME));

        IndiceEnum.Type type = IndiceEnum.Type.values()[cursor.getInt(COLUMN_INDICE_TYPE)];

        switch (type) {
            case SIMPLE:
                holder.taxaIndice.setText(Util.getPercentStringFromDouble(
                        cursor.getDouble(COLUMN_INDICE_YEAR_RATE), 2));
                holder.dataIndice.setText(Util.getDateStringFromLong(cursor.getLong(COLUMN_INDICE_DATE)));
                break;
            case DOUBLE:
                holder.taxaMesIndice.setText(Util.getPercentStringFromDouble(
                        cursor.getDouble(COLUMN_INDICE_MONTH_RATE), 2));
                holder.mesIndice.setText(Util.getMonthStringFromLong(cursor.getLong(COLUMN_INDICE_DATE)));
                holder.taxaAnoIndice.setText(Util.getPercentStringFromDouble(
                        cursor.getDouble(COLUMN_INDICE_YEAR_RATE), 2));
                holder.anoIndice.setText(Util.getYearStringFromLong(cursor.getLong(COLUMN_INDICE_DATE)));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getItemCursor(position).getInt(COLUMN_INDICE_TYPE);
    }

    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }
}
