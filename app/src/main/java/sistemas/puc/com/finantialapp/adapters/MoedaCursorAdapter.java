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
import sistemas.puc.com.finantialapp.data.FinantialContract;
import sistemas.puc.com.finantialapp.util.Util;

public class MoedaCursorAdapter extends AbstractCursorAdapter<MoedaCursorAdapter.ViewHolder> {

    private final int columnMoedaCode;
    private final int columnMoedaName;
    private final int columnMoedaRate;

    public MoedaCursorAdapter(@NonNull Context context, @NonNull Cursor cursor) {
        super(context, cursor);
        columnMoedaCode = cursor.getColumnIndexOrThrow(FinantialContract.MoedaEntry.COLUMN_MOEDA_CODE);
        columnMoedaName = cursor.getColumnIndexOrThrow(FinantialContract.MoedaEntry.COLUMN_MOEDA_NAME);
        columnMoedaRate = cursor.getColumnIndexOrThrow(FinantialContract.MoedaEntry.COLUMN_MOEDA_RATE);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView codigoMoeda;
        public TextView nomeMoeda;
        public TextView cotacaoMoeda;

        public ViewHolder(View itemView) {
            super(itemView);
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
        holder.codigoMoeda.setText(cursor.getString(columnMoedaCode));
        holder.nomeMoeda.setText(cursor.getString(columnMoedaName));

        double cotacao = cursor.getDouble(columnMoedaRate);
        holder.cotacaoMoeda.setText(Util.getRealStringFromDouble(cotacao,4));
    }
}
