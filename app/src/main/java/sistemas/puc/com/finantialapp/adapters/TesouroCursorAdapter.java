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
import sistemas.puc.com.finantialapp.data.FinantialContract.TesouroEntry;
import sistemas.puc.com.finantialapp.util.Util;

public class TesouroCursorAdapter extends AbstractCursorAdapter<TesouroCursorAdapter.ViewHolder> {

    public TesouroCursorAdapter(@NonNull Context context, @NonNull Cursor cursor) {
        super(context, cursor);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nomeTesouro;

        public TextView dataVencimentoTesouro;
        public TextView modalidadeAnoTesouro;

        public TextView rendimentoCompraTesouro;
        public TextView rendimentoVendaTesouro;
        public TextView precoCompraTesouro;
        public TextView precoMinCompraTesouro;
        public TextView precoVendaTesouro;

        public ViewHolder(View itemView) {
            super(itemView);
            nomeTesouro = (TextView) itemView.findViewById(R.id.item_tesouro_name_textview);
            dataVencimentoTesouro = (TextView) itemView.findViewById(R.id.item_tesouro_expiration_textview);
            modalidadeAnoTesouro = (TextView) itemView.findViewById(R.id.item_tesouro_mode_year_textview);
            rendimentoCompraTesouro = (TextView) itemView.findViewById(R.id.item_tesouro_buying_income_textview);
            rendimentoVendaTesouro = (TextView) itemView.findViewById(R.id.item_tesouro_selling_income_textview);
            precoCompraTesouro = (TextView) itemView.findViewById(R.id.item_tesouro_buying_price_textview);
            precoMinCompraTesouro = (TextView) itemView.findViewById(R.id.item_tesouro_min_buying_textview);
            precoVendaTesouro = (TextView) itemView.findViewById(R.id.item_tesouro_selling_price_textview);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tesouro, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        int colTesouroName = cursor.getColumnIndexOrThrow(TesouroEntry.COLUMN_TESOURO_NAME);
        int colTesouroExpiration = cursor.getColumnIndexOrThrow(TesouroEntry.COLUMN_TESOURO_EXPIRATION_DATE);
        int colTesouroMode = cursor.getColumnIndexOrThrow(TesouroEntry.COLUMN_TESOURO_MODE);
        int colTesouroYear = cursor.getColumnIndexOrThrow(TesouroEntry.COLUMN_TESOURO_YEAR);
        int colTesouroBuyIncome = cursor.getColumnIndexOrThrow(TesouroEntry.COLUMN_TESOURO_BUYING_INCOME);
        int colTesouroSellIncome = cursor.getColumnIndexOrThrow(TesouroEntry.COLUMN_TESOURO_SELLING_INCOME);
        int colTesouroBuyPrice = cursor.getColumnIndexOrThrow(TesouroEntry.COLUMN_TESOURO_BUYING_PRICE);
        int colTesouroMinCompra = cursor.getColumnIndexOrThrow(TesouroEntry.COLUMN_TESOURO_BUYING_MIN_VALUE);
        int colTesouroSellPrice = cursor.getColumnIndexOrThrow(TesouroEntry.COLUMN_TESOURO_SELLING_PRICE);

        holder.nomeTesouro.setText(cursor.getString(colTesouroName));
        holder.dataVencimentoTesouro.setText(Util.getDateStringFromLong(
                cursor.getLong(colTesouroExpiration)));
        String modeYear = cursor.getString(colTesouroMode) + " - " + cursor.getInt(colTesouroYear);
        holder.modalidadeAnoTesouro.setText(modeYear);
        holder.rendimentoVendaTesouro.setText(Util.getPercentStringFromDouble(
                cursor.getDouble(colTesouroSellIncome),2));
        holder.precoVendaTesouro.setText(Util.getBRStringFromDouble(
                cursor.getDouble(colTesouroSellPrice),2));

        if (!cursor.isNull(colTesouroBuyPrice)) {
            holder.precoCompraTesouro.setText(Util.getBRStringFromDouble(
                    cursor.getDouble(colTesouroBuyPrice),2));
            holder.precoMinCompraTesouro.setText(Util.getBRStringFromDouble(
                    cursor.getDouble(colTesouroMinCompra),2));
            holder.rendimentoCompraTesouro.setText(Util.getPercentStringFromDouble(
                    cursor.getDouble(colTesouroBuyIncome), 2));
        }
    }
}
