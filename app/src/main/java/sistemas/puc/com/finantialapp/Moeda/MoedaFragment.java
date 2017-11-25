package sistemas.puc.com.finantialapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashSet;

import sistemas.puc.com.finantialapp.Moeda.LoaderCallback;
import sistemas.puc.com.finantialapp.adapters.MoedaCursorAdapter;
import sistemas.puc.com.finantialapp.adapters.RecyclerItemClickAdapter;
import sistemas.puc.com.finantialapp.data.FinantialContract.MoedaEntry;
import sistemas.puc.com.finantialapp.model.Database;
import sistemas.puc.com.finantialapp.util.DividerItemDecoration;

public class MoedaFragment extends Fragment implements
        ActionMode.Callback,
        RecyclerItemClickAdapter.OnItemClickListener {

    public static final String MOEDA_BASE = "BRL";

    private static final Uri MOEDA_URI = MoedaEntry.CONTENT_URI;

    private static final String[] MOEDA_COLUMNS = new String[]{
            MoedaEntry.TABLE_NAME + "." + MoedaEntry._ID,
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_CODE,
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_NAME,
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_RATE,
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_FAVORITE,
    };

    private RecyclerView m_recyclerView;
    private MoedaCursorAdapter m_adapter;
    private RecyclerView.LayoutManager m_layoutManager;

    private Cursor m_cursor = null;

    private boolean m_selectionMode = false;
    private ActionMode m_actionMode;

    LoaderCallback mLoaderCallBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_moeda, container, false);

        // Update database on startup
        Database.update(getContext());

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity());
        itemDecoration.setLastItemIncluded(false);
        // TODO: Fix hardcoded numbers
        itemDecoration.setPaddingLeftDp(72);
        itemDecoration.setPaddingRightDp(16);

        m_recyclerView = (RecyclerView) rootView.findViewById(R.id.listview_moeda);
        m_recyclerView.addItemDecoration(itemDecoration);
        m_recyclerView.setHasFixedSize(true);
        m_recyclerView.addOnItemTouchListener(
                new RecyclerItemClickAdapter(getContext(), m_recyclerView, this));

        m_layoutManager = new LinearLayoutManager(getContext());
        m_recyclerView.setLayoutManager(m_layoutManager);

        m_adapter = new MoedaCursorAdapter(getContext(), null);

        m_recyclerView.setAdapter(m_adapter);

        mLoaderCallBack = new LoaderCallback(getActivity(), m_adapter, MOEDA_BASE);
        getLoaderManager().initLoader(0, null, mLoaderCallBack);

        return rootView;
    }

    @Override
    public void onItemClick(View view, int position) {
        if (m_selectionMode) {
            toggleSelection(position);
        } else {
            Intent intent = new Intent(getContext(), ConversaoActivity.class);
            intent.putExtra(ConversaoActivity.EXTRA_RIGHT_MOEDA, MOEDA_BASE);

            m_cursor.moveToPosition(position);
            int codeColumnIndex = m_cursor.getColumnIndexOrThrow(MoedaEntry.COLUMN_MOEDA_CODE);
            String code = m_cursor.getString(codeColumnIndex);

            intent.putExtra(ConversaoActivity.EXTRA_LEFT_MOEDA, code);
            startActivity(intent);
        }
    }

    @Override
    public void onLongItemClick(View view, int position) {
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        Cursor cursor = m_adapter.getItemCursor(position);
        int columnMoedaId   = cursor.getColumnIndexOrThrow(MoedaEntry._ID);
        int id = cursor.getInt(columnMoedaId);
        m_adapter.toggleItemSelected(id);
        m_selectionMode = m_adapter.getItemSelectedCount() != 0;
        m_adapter.notifyItemChanged(position);

        if (m_selectionMode) {
            if (m_actionMode == null)
                m_actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(this);
            m_actionMode.setTitle(String.valueOf(m_adapter.getItemSelectedCount()));
        } else if (m_actionMode != null) {
            m_actionMode.finish();
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.moeda_context_menu, menu);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pin_as_favorite:
                handlePinned();
                mode.finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        m_adapter.clearItemsSelected();
        m_adapter.notifyDataSetChanged();
        m_selectionMode = false;
        m_actionMode = null;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) { return false; }

    private void handlePinned() {
        // Get all selected items
        HashSet<Integer> itemsSelected = m_adapter.getItemsSelected();

        if (itemsSelected.size() <= 0)
            return;

        // Format selected items to perform a query
        String selectionGroup = itemsSelected
                .toString()
                .replace("[", "(\'")
                .replace("]", "\')")
                .replace(", ", "\', \'");

        // Get all selected items that are marked as favorite
        Cursor cursor = getContext().getContentResolver().query(
                MOEDA_URI,
                null,
                MoedaEntry.TABLE_NAME + "." + MoedaEntry._ID + " IN " + selectionGroup +
                " AND " + MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_FAVORITE + " = 1",
                null,
                null);

        assert (cursor != null);

        // Create a content value with favorite attribute
        ContentValues contentValues = new ContentValues();
        // If there is any item marked as favorite on the previously query, mark all as non favorite
        contentValues.put(MoedaEntry.COLUMN_MOEDA_FAVORITE, cursor.getCount() > 0 ? 0 : 1);

        // Update all selected items with the favorite mark
        getContext().getContentResolver().update(
                MOEDA_URI,
                contentValues,
                MoedaEntry.TABLE_NAME + "." + MoedaEntry._ID + " IN " + selectionGroup,
                null);
    }
}
