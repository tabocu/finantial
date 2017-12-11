package sistemas.puc.com.finantialapp.indice;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sistemas.puc.com.finantialapp.R;
import sistemas.puc.com.finantialapp.adapters.AbstractCursorAdapter;
import sistemas.puc.com.finantialapp.model.Database;
import sistemas.puc.com.finantialapp.util.DividerItemDecoration;

public class IndiceFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private AbstractCursorAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_indice, container, false);

        // Update database on startup
        Database.update(getContext());

        mAdapter = new IndiceCursorAdapter(getContext());
        mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_indice);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(getItemDecoration());

        getLoaderManager().initLoader(
                0,
                null,
                new IndiceLoaderCallback(getActivity(), mAdapter));

        return rootView;
    }

    @NonNull
    private DividerItemDecoration getItemDecoration() {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity());
        itemDecoration.setLastItemIncluded(false);
        // TODO: Fix hardcoded numbers
        itemDecoration.setPaddingLeftDp(32);
        itemDecoration.setPaddingRightDp(32);
        return itemDecoration;
    }
}
