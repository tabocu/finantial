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

    public IndiceCursorAdapter(@NonNull Context context, @NonNull Cursor cursor) {
        super(context, cursor);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView codigoMoeda;
        public TextView nomeMoeda;
        public TextView cotacaoMoeda;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            switch(viewType) {
                case VIEW_TYPE_INDICE_SIMPLES:

                case VIEW_TYPE_INDICE_DUPLO:

            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moeda, parent, false);
        ViewHolder holder = new ViewHolder(v,viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        int columnMoedaCode = cursor.getColumnIndexOrThrow(MoedaEntry.COLUMN_MOEDA_CODE);
        int columnMoedaName = cursor.getColumnIndexOrThrow(MoedaEntry.COLUMN_MOEDA_NAME);
        int columnMoedaRate = cursor.getColumnIndexOrThrow(MoedaEntry.COLUMN_MOEDA_RATE);

        holder.codigoMoeda.setText(cursor.getString(columnMoedaCode));
        holder.nomeMoeda.setText(cursor.getString(columnMoedaName));

        double cotacao = cursor.getDouble(columnMoedaRate);
        holder.cotacaoMoeda.setText(Util.getRealStringFromDouble(cotacao,4));
    }

    @Override
    public int getItemViewType(int position) {
        return true ? VIEW_TYPE_INDICE_SIMPLES : VIEW_TYPE_INDICE_DUPLO;
    }

    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }
}
