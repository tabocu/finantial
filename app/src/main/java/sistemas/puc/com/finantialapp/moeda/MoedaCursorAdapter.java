package sistemas.puc.com.finantialapp.moeda;

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
import sistemas.puc.com.finantialapp.data.FinantialContract.MoedaEntry;
import sistemas.puc.com.finantialapp.util.Util;

public class MoedaCursorAdapter extends AbstractCursorAdapter<MoedaCursorAdapter.ViewHolder> {

    public MoedaCursorAdapter(@NonNull Context context) {
        super(context, null);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public View selectedPin;
        public View pinnedPin;
        public TextView codigoMoeda;
        public TextView nomeMoeda;
        public TextView cotacaoMoeda;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.item_moeda);
            selectedPin = itemView.findViewById(R.id.selected_pin);
            pinnedPin = itemView.findViewById(R.id.pinned_pin);
            codigoMoeda = (TextView) itemView.findViewById(R.id.item_moeda_code_textview);
            nomeMoeda = (TextView) itemView.findViewById(R.id.item_moeda_name_textview);
            cotacaoMoeda = (TextView) itemView.findViewById(R.id.item_moeda_value_textview);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moeda, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        int columnMoedaId       = cursor.getColumnIndexOrThrow(MoedaEntry._ID);
        int columnMoedaCode     = cursor.getColumnIndexOrThrow(MoedaEntry.COLUMN_MOEDA_CODE);
        int columnMoedaName     = cursor.getColumnIndexOrThrow(MoedaEntry.COLUMN_MOEDA_NAME);
        int columnMoedaRate     = cursor.getColumnIndexOrThrow(MoedaEntry.COLUMN_MOEDA_RATE);
        int columnMoedaFavorite = cursor.getColumnIndexOrThrow(MoedaEntry.COLUMN_MOEDA_FAVORITE);

        holder.selectedPin.setVisibility(isItemSelected(cursor.getInt(columnMoedaId)) ? View.VISIBLE : View.INVISIBLE);
        holder.pinnedPin.setVisibility(cursor.getInt(columnMoedaFavorite) == 1 ? View.VISIBLE : View.INVISIBLE);
        holder.view.setSelected(isItemSelected(cursor.getInt(columnMoedaId)));

        holder.codigoMoeda.setText(cursor.getString(columnMoedaCode));
        holder.nomeMoeda.setText(cursor.getString(columnMoedaName));

        double cotacao = cursor.getDouble(columnMoedaRate);
        holder.cotacaoMoeda.setText(Util.getRealStringFromDouble(cotacao,4));
    }
}
