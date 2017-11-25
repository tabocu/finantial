package sistemas.puc.com.finantialapp.Moeda;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.HashSet;

import sistemas.puc.com.finantialapp.ConversaoActivity;
import sistemas.puc.com.finantialapp.R;
import sistemas.puc.com.finantialapp.adapters.AbstractCursorAdapter;
import sistemas.puc.com.finantialapp.adapters.RecyclerItemClickAdapter;
import sistemas.puc.com.finantialapp.data.FinantialContract.MoedaEntry;

public class MoedaActionCallback implements
        ActionMode.Callback,
        RecyclerItemClickAdapter.OnItemClickListener {

    static final Uri URI = MoedaEntry.CONTENT_URI;

    static final String[] COLUMNS = new String[]{
            MoedaEntry.TABLE_NAME + "." + MoedaEntry._ID,
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_FAVORITE,
    };

    static final String SELECTION_FAVORITE =
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_FAVORITE + " = 1 ";

    static final String SELECTION_ID =
            MoedaEntry.TABLE_NAME + "." + MoedaEntry._ID + " IN ";

    Context mContext;
    AbstractCursorAdapter mAdapter;
    String mMoedaBase;
    ActionMode mActionMode;

    public MoedaActionCallback(Context context,
                               AbstractCursorAdapter adapter,
                               String moedaBase) {
        mContext = context;
        mAdapter = adapter;
        mMoedaBase = moedaBase;
        mActionMode = null;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.moeda_context_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) { return false; }

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
        mAdapter.clearItemsSelected();
        mAdapter.notifyDataSetChanged();
        mActionMode = null;
    }

    @Override
    public void onItemClick(View view, int position) {
        if (mActionMode == null) {
            Intent intent = new Intent(mContext, ConversaoActivity.class);
            intent.putExtra(ConversaoActivity.EXTRA_RIGHT_MOEDA, mMoedaBase);

            mAdapter.getCursor().moveToPosition(position);
            int codeColumnIndex = mAdapter.getCursor().getColumnIndexOrThrow(MoedaEntry.COLUMN_MOEDA_CODE);
            String code = mAdapter.getCursor().getString(codeColumnIndex);

            intent.putExtra(ConversaoActivity.EXTRA_LEFT_MOEDA, code);
            mContext.startActivity(intent);
        } else {
            toggleSelection(position);
        }
    }

    @Override
    public void onLongItemClick(View view, int position) {
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        Cursor cursor = mAdapter.getItemCursor(position);
        int columnMoedaId   = cursor.getColumnIndexOrThrow(MoedaEntry._ID);
        int id = cursor.getInt(columnMoedaId);
        mAdapter.toggleItemSelected(id);
        mAdapter.notifyItemChanged(position);

        if (mActionMode == null)
            mActionMode = ((AppCompatActivity) mContext).startSupportActionMode(this);

        mActionMode.setTitle(String.valueOf(mAdapter.getItemSelectedCount()));

        if (mAdapter.getItemSelectedCount() == 0)
            mActionMode.finish();
    }

    private void handlePinned() {
        // Get all selected items
        HashSet<Integer> itemsSelected = mAdapter.getItemsSelected();

        if (itemsSelected.size() == 0)
            return;

        // Format selected items to perform a query
        String selectionGroup = itemsSelected
                .toString()
                .replace("[", "(\'")
                .replace("]", "\')")
                .replace(", ", "\', \'");

        // Get all selected items that are marked as favorite
        Cursor cursor = mContext.getContentResolver().query(
                URI,
                COLUMNS,
                SELECTION_FAVORITE + " AND " + SELECTION_ID + selectionGroup,
                null,
                null);

        assert (cursor != null);

        // Create a content value with favorite attribute
        ContentValues contentValues = new ContentValues();
        // If there is any item marked as favorite on the previously query, mark all as non favorite
        contentValues.put(MoedaEntry.COLUMN_MOEDA_FAVORITE, cursor.getCount() > 0 ? 0 : 1);

        // Update all selected items with the favorite mark
        mContext.getContentResolver().update(
                URI,
                contentValues,
                SELECTION_ID + selectionGroup,
                null);
    }
}
