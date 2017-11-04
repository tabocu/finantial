package sistemas.puc.com.finantialapp;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import sistemas.puc.com.finantialapp.data.FinantialContract.MoedaEntry;
import sistemas.puc.com.finantialapp.util.FloatingNumberMask;

public class ConversaoActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemSelectedListener {

    public final static String EXTRA_MOEDA_ESQ = "extraMoedaEsq";
    public final static String EXTRA_MOEDA_DIR = "extraMoedaDir";

    private static final String MOEDA_FORMAT = "%1$.4f";
    private static final double DEFAULT_VALUE = 1.0;
    private static final char SEPARATOR = ',';

    private static final Uri MOEDA_URI = MoedaEntry.CONTENT_URI;

    private static final String[] MOEDA_COLUMNS = new String[]{
            MoedaEntry.TABLE_NAME + "." + MoedaEntry._ID,
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_CODE,
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_RATE,
    };

    EditText m_moedaEsqEditText;
    Spinner m_moedaEsqSpinner;
    SimpleCursorAdapter m_esqAdapter;
    FloatingNumberMask m_esqFloatingNumberMask;
    UpdateField m_esqUpdateField;

    EditText m_moedaDirEditText;
    Spinner m_moedaDirSpinner;
    SimpleCursorAdapter m_dirAdapter;
    FloatingNumberMask m_dirFloatingNumberMask;
    UpdateField m_dirUpdateField;

    Cursor m_cursor;

    double m_leftRate = 1;
    double m_rightRate = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversao);
        m_moedaEsqEditText = (EditText) findViewById(R.id.moeda_esq_edittext);
        m_moedaDirEditText = (EditText) findViewById(R.id.moeda_dir_edittext);
        m_moedaEsqSpinner = (Spinner) findViewById(R.id.moeda_esq_spinner);
        m_moedaDirSpinner = (Spinner) findViewById(R.id.moeda_dir_spinner);

        m_esqFloatingNumberMask = new FloatingNumberMask(m_moedaEsqEditText, SEPARATOR, 4);
        m_dirFloatingNumberMask = new FloatingNumberMask(m_moedaDirEditText, SEPARATOR, 4);

        m_esqUpdateField = new UpdateField(m_moedaEsqEditText, m_moedaDirEditText);
        m_dirUpdateField = new UpdateField(m_moedaDirEditText, m_moedaEsqEditText);

        m_moedaEsqEditText.addTextChangedListener(m_esqFloatingNumberMask);
        m_moedaDirEditText.addTextChangedListener(m_dirFloatingNumberMask);

        m_moedaEsqEditText.addTextChangedListener(m_esqUpdateField);
        m_moedaDirEditText.addTextChangedListener(m_dirUpdateField);

        int spinnerLayoutId = R.layout.support_simple_spinner_dropdown_item;
        m_esqAdapter = new SimpleCursorAdapter(this, spinnerLayoutId, null,
                new String[] {MOEDA_COLUMNS[1]}, new int[] {android.R.id.text1}, 0);
        m_dirAdapter = new SimpleCursorAdapter(this, spinnerLayoutId, null,
                new String[] {MOEDA_COLUMNS[1]}, new int[] {android.R.id.text1}, 0);
        m_esqAdapter.setDropDownViewResource(spinnerLayoutId);
        m_dirAdapter.setDropDownViewResource(spinnerLayoutId);

        m_moedaEsqSpinner.setAdapter(m_esqAdapter);
        m_moedaDirSpinner.setAdapter(m_dirAdapter);

        m_moedaEsqSpinner.setOnItemSelectedListener(this);
        m_moedaDirSpinner.setOnItemSelectedListener(this);

        m_moedaEsqEditText.setText(String.format(MOEDA_FORMAT, DEFAULT_VALUE)
                .replaceAll("[.,]",""));

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                MOEDA_URI,
                MOEDA_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        m_esqAdapter.swapCursor(data);
        m_dirAdapter.swapCursor(data);
        m_cursor = data;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        m_esqAdapter.swapCursor(null);
        m_dirAdapter.swapCursor(null);
        m_cursor = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        m_cursor.moveToPosition(position);
        double rate = m_cursor.getDouble(m_cursor.getColumnIndexOrThrow(MoedaEntry.COLUMN_MOEDA_RATE));
        String valueString = m_moedaEsqEditText.getText().toString().replace(SEPARATOR,'.');
        double valueDouble = valueString.isEmpty() ? 0.0 : Double.valueOf(valueString);

        if (parent == m_moedaEsqSpinner)
            m_leftRate = rate;
        else if (parent == m_moedaDirSpinner)
            m_rightRate = rate;

        m_esqUpdateField.setRate(m_rightRate/m_leftRate);
        m_dirUpdateField.setRate(m_leftRate/m_rightRate);

        valueDouble *= m_rightRate/m_leftRate;

        m_moedaEsqEditText.removeTextChangedListener(m_esqUpdateField);
        m_moedaDirEditText.removeTextChangedListener(m_dirUpdateField);

        m_moedaDirEditText.setText(String.format(MOEDA_FORMAT, valueDouble).replace('.',SEPARATOR));

        m_moedaEsqEditText.addTextChangedListener(m_esqUpdateField);
        m_moedaDirEditText.addTextChangedListener(m_dirUpdateField);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    static class UpdateField implements TextWatcher {

        private EditText m_fromEditText;
        private EditText m_toEditText;
        private static boolean m_isEditing = false;
        private double m_rate = 1;

        public UpdateField(EditText fromEditText, EditText toEditText) {
            m_fromEditText = fromEditText;
            m_toEditText = toEditText;
            m_rate = 1;
        }

        public void setRate(double rate) {
            m_rate = rate;
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (m_isEditing) return;
            m_isEditing = true;

            String valueString = m_fromEditText.getText().toString()
                    .replace(SEPARATOR, '.');
            double valueDouble = valueString.isEmpty() ? 0.0 : Double.valueOf(valueString) * m_rate;
            m_toEditText.setText(String.format(MOEDA_FORMAT, valueDouble)
                    .replace('.', SEPARATOR));

            m_isEditing = false;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
    }


}
