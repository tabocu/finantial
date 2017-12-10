package sistemas.puc.com.finantialapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.List;

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
