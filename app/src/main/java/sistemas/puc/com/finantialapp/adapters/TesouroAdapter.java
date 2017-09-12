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
import sistemas.puc.com.finantialapp.entities.TesouroItem;

public class TesouroAdapter extends ArrayAdapter<TesouroItem> {


    public TesouroAdapter(@NonNull Context context, @NonNull List<TesouroItem> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_tesouro, parent, false);
        } else {
            view = convertView;
        }

        final TextView nome = (TextView) view.findViewById(R.id.list_item_nome_tesouro_textview);
        final TextView dataVencimento = (TextView) view.findViewById(R.id.list_item_vencimento_tesouro_textview);
        final TextView rendimentoCompra = (TextView) view.findViewById(R.id.list_item_rendimento_compra_tesouro_textview);
        final TextView rendimentoVenda = (TextView) view.findViewById(R.id.list_item_rendimento_venda_tesouro_textview);
        final TextView precoUnitarioCompra = (TextView) view.findViewById(R.id.list_item_preco_compra_tesouro_textview);
        final TextView precoUnitarioVenda = (TextView) view.findViewById(R.id.list_item_preco_venda_tesouro_textview);

        final TesouroItem tesouroItem = getItem(position);

        nome.setText(tesouroItem.m_nome);
        dataVencimento.setText(tesouroItem.m_dataVencimento);
        rendimentoCompra.setText(tesouroItem.m_rendimentoCompra);
        rendimentoVenda.setText(tesouroItem.m_rendimentoVenda);
        precoUnitarioCompra.setText(tesouroItem.m_precoUnitarioCompra);
        precoUnitarioVenda.setText(tesouroItem.m_precoUnitarioVenda);

        return view;
    }
}
