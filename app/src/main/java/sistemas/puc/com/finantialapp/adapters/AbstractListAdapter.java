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

public abstract class AbstractListAdapter<T,K extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<K> {

    private List<T> m_dataSet;

    public AbstractListAdapter(@NonNull List<T> dataSet) {
        m_dataSet = dataSet;
    }

    public T getItem(int position) {
        return m_dataSet.get(position);
    }

    @Override
    public int getItemCount() {
        return m_dataSet.size();
    }

    public void setData(@NonNull List<T> dataSet ) {
        notifyDataSetChanged();
        m_dataSet = dataSet;
    }

    public List<T> getData() {
        notifyDataSetChanged();
        return m_dataSet;
    }
}
