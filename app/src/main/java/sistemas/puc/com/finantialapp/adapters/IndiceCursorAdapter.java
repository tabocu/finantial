package sistemas.puc.com.finantialapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sistemas.puc.com.finantialapp.R;
import sistemas.puc.com.finantialapp.data.FinantialContract.MoedaEntry;
import sistemas.puc.com.finantialapp.entities.IndiceItem;
import sistemas.puc.com.finantialapp.util.Util;

public class IndiceCursorAdapter extends AbstractCursorAdapter<IndiceCursorAdapter.ViewHolder> {

    private static final int VIEW_TYPE_COUNT = 2;
    private static final int VIEW_TYPE_INDICE_SIMPLES = 0;
    private static final int VIEW_TYPE_INDICE_DUPLO = 1;

    public static final int COLUMN_INDICE_ID = 0;
    public static final int COLUMN_INDICE_CODE = 1;
    public static final int COLUMN_INDICE_NAME = 2;
    public static final int COLUMN_INDICE_DATE = 3;
    public static final int COLUMN_INDICE_MONTH_RATE = 4;
    public static final int COLUMN_INDICE_YEAR_RATE = 5;
    public static final int COLUMN_INDICE_TYPE = 6;

    public IndiceCursorAdapter(@NonNull Context context, @NonNull Cursor cursor) {
        super(context, cursor);
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
            codigoIndice = (TextView) itemView.findViewById(R.id.item_moeda_code_textview);
            nomeIndice = (TextView) itemView.findViewById(R.id.item_moeda_code_textview);
            switch(viewType) {
                case VIEW_TYPE_INDICE_SIMPLES:
                    dataIndice = (TextView) itemView.findViewById(R.id.item_moeda_code_textview);
                    taxaIndice = (TextView) itemView.findViewById(R.id.item_moeda_code_textview);
                    break;
                case VIEW_TYPE_INDICE_DUPLO:
                    mesIndice = (TextView) itemView.findViewById(R.id.item_moeda_code_textview);
                    taxaMesIndice = (TextView) itemView.findViewById(R.id.item_moeda_code_textview);
                    anoIndice = (TextView) itemView.findViewById(R.id.item_moeda_code_textview);
                    taxaAnoIndice = (TextView) itemView.findViewById(R.id.item_moeda_code_textview);
                    break;
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = -1;
        switch(viewType) {
            case VIEW_TYPE_INDICE_SIMPLES:
                layoutId = R.layout.item_indice_simples;
                break;
            case VIEW_TYPE_INDICE_DUPLO:
                layoutId = R.layout.item_indice_duplo;
                break;
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        ViewHolder holder = new ViewHolder(v,viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        holder.codigoIndice.setText(cursor.getString(COLUMN_INDICE_CODE));
        holder.nomeIndice.setText(cursor.getString(COLUMN_INDICE_NAME));

        int type = cursor.getInt(COLUMN_INDICE_TYPE);

        switch (type) {
            case VIEW_TYPE_INDICE_SIMPLES:
                holder.taxaIndice.setText(Util.getPercentStringFromDouble(
                        cursor.getDouble(COLUMN_INDICE_YEAR_RATE), 2));
                holder.dataIndice.setText(Util.getDateStringFromLong(cursor.getInt(COLUMN_INDICE_DATE)));
                break;
            case VIEW_TYPE_INDICE_DUPLO:
                holder.taxaMesIndice.setText(Util.getPercentStringFromDouble(
                        cursor.getDouble(COLUMN_INDICE_MONTH_RATE), 2));
                holder.mesIndice.setText(Util.getMonthStringFromLong(cursor.getInt(COLUMN_INDICE_DATE)));
                holder.taxaAnoIndice.setText(Util.getPercentStringFromDouble(
                        cursor.getDouble(COLUMN_INDICE_YEAR_RATE), 2));
                holder.anoIndice.setText(Util.getYearStringFromLong(cursor.getInt(COLUMN_INDICE_DATE)));
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
