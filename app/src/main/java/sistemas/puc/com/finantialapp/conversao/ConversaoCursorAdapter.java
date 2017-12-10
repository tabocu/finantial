package sistemas.puc.com.finantialapp.conversao;

import android.content.Context;
import android.support.v4.widget.SimpleCursorAdapter;

import sistemas.puc.com.finantialapp.R;
import sistemas.puc.com.finantialapp.data.FinantialContract.MoedaEntry;

public class ConversaoCursorAdapter extends SimpleCursorAdapter {
    static final int LAYOUT_ID = R.layout.support_simple_spinner_dropdown_item;

    static final String[] COLUMNS = new String[] {
            MoedaEntry.COLUMN_MOEDA_CODE,
    };

    static final int[] FIELDS_ID = new int[] {
            android.R.id.text1
    };

    public ConversaoCursorAdapter(Context context) {
        super(context, LAYOUT_ID, null, COLUMNS, FIELDS_ID,0);
        setDropDownViewResource(LAYOUT_ID);
    }
}
