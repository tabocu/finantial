/*
 * Copyright (C) 2014 skyfish.jy@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package sistemas.puc.com.finantialapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;

public abstract class AbstractCursorAdapter<K extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<K> {

    private final Context m_context;
    private Cursor m_cursor;
    private boolean m_dataValid;
    private int m_rowIdColumn;
    private DataSetObserver m_dataSetObserver;

    public AbstractCursorAdapter(Context context, Cursor cursor) {
        m_context = context;
        m_cursor = cursor;
        m_dataValid = cursor != null;
        m_rowIdColumn = m_dataValid ? m_cursor.getColumnIndex("_id") : -1;
        m_dataSetObserver = new NotifyingDataSetObserver();
        if (m_cursor != null) {
            m_cursor.registerDataSetObserver(m_dataSetObserver);
        }
    }

    public Cursor getCursor() {
        return m_cursor;
    }

    public Context getContext() {
        return m_context;
    }

    @Override
    public int getItemCount() {
        if (m_dataValid && m_cursor != null) {
            return m_cursor.getCount();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if (m_dataValid && m_cursor != null && m_cursor.moveToPosition(position)) {
            return m_cursor.getLong(m_rowIdColumn);
        }
        return 0;
    }

    public Cursor getItemCursor(int position) {
        if (m_dataValid && m_cursor != null && m_cursor.moveToPosition(position)) {
            return m_cursor;
        }
        return null;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    public abstract void onBindViewHolder(K viewHolder, Cursor cursor);

    @Override
    public final void onBindViewHolder(K viewHolder, int position) {
        if (!m_dataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!m_cursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        onBindViewHolder(viewHolder, m_cursor);
    }

    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == m_cursor) {
            return null;
        }
        final Cursor oldCursor = m_cursor;
        if (oldCursor != null && m_dataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(m_dataSetObserver);
        }
        m_cursor = newCursor;
        if (m_cursor != null) {
            if (m_dataSetObserver != null) {
                m_cursor.registerDataSetObserver(m_dataSetObserver);
            }
            m_rowIdColumn = newCursor.getColumnIndexOrThrow("_id");
            m_dataValid = true;
            notifyDataSetChanged();
        } else {
            m_rowIdColumn = -1;
            m_dataValid = false;
            notifyDataSetChanged();
        }
        return oldCursor;
    }

    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            m_dataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            m_dataValid = false;
            notifyDataSetChanged();
        }
    }
}
