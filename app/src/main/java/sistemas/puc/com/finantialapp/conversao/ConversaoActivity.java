package sistemas.puc.com.finantialapp;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
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
import android.widget.TextView;

import sistemas.puc.com.finantialapp.data.FinantialContract.MoedaEntry;
import sistemas.puc.com.finantialapp.util.FloatingNumberMask;
import sistemas.puc.com.finantialapp.util.Util;

public class ConversaoActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemSelectedListener {

    public final static String EXTRA_LEFT_MOEDA = "extraMoedaEsq";
    public final static String EXTRA_RIGHT_MOEDA = "extraMoedaDir";

    private static final String MOEDA_FORMAT = "%1$.4f";
    private static final double DEFAULT_VALUE = 1.0;
    private static final char SEPARATOR = ',';

    private static final Uri MOEDA_URI = MoedaEntry.CONTENT_URI;

    private static final String[] MOEDA_COLUMNS = new String[]{
            MoedaEntry.TABLE_NAME + "." + MoedaEntry._ID,
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_CODE,
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_NAME,
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_DATE,
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_RATE,
    };

    Cursor m_cursor;

    TextInputLayout m_leftTextInputLayout;
    TextInputLayout m_rightTextInputLayout;

    EditText m_leftEditText;
    EditText m_rightEditText;

    Spinner m_leftSpinner;
    Spinner m_rightSpinner;

    TextView m_dateTextView;

    SimpleCursorAdapter m_leftSpinnerAdapter;
    SimpleCursorAdapter m_rightSpinnerAdapter;

    FloatingNumberMask m_leftFloatingNumberMask;
    FloatingNumberMask m_rightFloatingNumberMask;

    UpdateField m_leftUpdateField;
    UpdateField m_rightUpdateField;

    double m_leftRate = 1;
    double m_rightRate = 1;

    String m_leftCode = "USD";
    String m_rightCode = "USD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Getting bundle arguments
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            m_leftCode = arguments.getString(EXTRA_LEFT_MOEDA);
            m_rightCode = arguments.getString(EXTRA_RIGHT_MOEDA);
        }

        // Setting layout
        setContentView(R.layout.activity_conversao);

        // Getting views
        m_leftTextInputLayout = (TextInputLayout) findViewById(R.id.left_textinputlayout_moeda);
        m_rightTextInputLayout = (TextInputLayout) findViewById(R.id.right_textinputlayout_moeda);

        m_leftEditText = (EditText) findViewById(R.id.left_edittext_moeda);
        m_rightEditText = (EditText) findViewById(R.id.right_edittext_moeda);

        m_leftSpinner = (Spinner) findViewById(R.id.left_spinner_moeda);
        m_rightSpinner = (Spinner) findViewById(R.id.right_spinner_moeda);

        m_dateTextView = (TextView) findViewById(R.id.data_textview_moeda);

        // Setting floating number masks
        m_leftFloatingNumberMask = new FloatingNumberMask(m_leftEditText, SEPARATOR, 4);
        m_rightFloatingNumberMask = new FloatingNumberMask(m_rightEditText, SEPARATOR, 4);

        m_leftEditText.addTextChangedListener(m_leftFloatingNumberMask);
        m_rightEditText.addTextChangedListener(m_rightFloatingNumberMask);

        // Setting updater (it's responsible for change one editext if the other changes)
        m_leftUpdateField = new UpdateField(m_leftEditText, m_rightEditText);
        m_rightUpdateField = new UpdateField(m_rightEditText, m_leftEditText);

        m_leftEditText.addTextChangedListener(m_leftUpdateField);
        m_rightEditText.addTextChangedListener(m_rightUpdateField);

        // Setting spinners adapters
        int spinnerLayoutId = R.layout.support_simple_spinner_dropdown_item;
        String [] columnsNames = new String[] { MOEDA_COLUMNS[1] };
        int [] fieldsId = new int[] { android.R.id.text1 };
        m_leftSpinnerAdapter = new SimpleCursorAdapter(this, spinnerLayoutId, null,
                columnsNames, fieldsId, 0);
        m_rightSpinnerAdapter = new SimpleCursorAdapter(this, spinnerLayoutId, null,
                columnsNames, fieldsId, 0);
        m_leftSpinnerAdapter.setDropDownViewResource(spinnerLayoutId);
        m_rightSpinnerAdapter.setDropDownViewResource(spinnerLayoutId);

        m_leftSpinner.setAdapter(m_leftSpinnerAdapter);
        m_rightSpinner.setAdapter(m_rightSpinnerAdapter);

        // Setting listener for changes on spinners
        m_leftSpinner.setOnItemSelectedListener(this);
        m_rightSpinner.setOnItemSelectedListener(this);

        // Setting default value of edittext
        m_leftEditText.setText(String.format(MOEDA_FORMAT, DEFAULT_VALUE)
                .replaceAll("[.,]",""));

        // Starts the callback
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
        m_cursor = data;

        m_leftSpinnerAdapter.swapCursor(m_cursor);
        m_rightSpinnerAdapter.swapCursor(m_cursor);

        int codeColumnIndex = m_cursor.getColumnIndexOrThrow(MoedaEntry.COLUMN_MOEDA_CODE);
        int dateColumnIndex = m_cursor.getColumnIndexOrThrow(MoedaEntry.COLUMN_MOEDA_DATE);

        m_cursor.moveToFirst();

        long date = m_cursor.getLong(dateColumnIndex);
        m_dateTextView.setText(getString(R.string.cotacao_date, Util.getDateStringFromLong(date)));

        boolean foundLeft = false, foundRight = false;
        while (!m_cursor.isAfterLast() && (!foundLeft || !foundRight)) {
            if (m_cursor.getString(codeColumnIndex).equals(m_leftCode) && !foundLeft) {
                m_leftSpinner.setSelection(m_cursor.getPosition());
                foundLeft = true;
            }
            if (m_cursor.getString(codeColumnIndex).equals(m_rightCode) && !foundRight) {
                m_rightSpinner.setSelection(m_cursor.getPosition());
                foundRight = true;
            }
            m_cursor.moveToNext();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        m_leftSpinnerAdapter.swapCursor(null);
        m_rightSpinnerAdapter.swapCursor(null);
        m_cursor = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (m_cursor == null)
            return;

        int codeColumnIndex = m_cursor.getColumnIndexOrThrow(MoedaEntry.COLUMN_MOEDA_CODE);
        int nameColumnIndex = m_cursor.getColumnIndexOrThrow(MoedaEntry.COLUMN_MOEDA_NAME);
        int rateColumnIndex = m_cursor.getColumnIndexOrThrow(MoedaEntry.COLUMN_MOEDA_RATE);

        m_cursor.moveToPosition(position);

        String code = m_cursor.getString(codeColumnIndex);
        String name = m_cursor.getString(nameColumnIndex);
        double rate = m_cursor.getDouble(rateColumnIndex);

        if (parent == m_leftSpinner) {
            m_leftRate = rate;
            m_leftCode = code;
            m_leftTextInputLayout.setHint(code + " - " + name);
        } else if (parent == m_rightSpinner) {
            m_rightRate = rate;
            m_rightCode = code;
            m_rightTextInputLayout.setHint(code + " - " + name);
        }

        String valueString = m_leftEditText.getText().toString().replace(SEPARATOR,'.');
        double valueDouble = valueString.isEmpty() ? 0.0 : Double.valueOf(valueString);

        valueDouble *= m_leftRate/m_rightRate;

        m_leftUpdateField.setRate(valueDouble);
        m_rightUpdateField.setRate(1/valueDouble);

        m_leftEditText.removeTextChangedListener(m_leftUpdateField);
        m_rightEditText.removeTextChangedListener(m_rightUpdateField);

        m_rightEditText.setText(String.format(MOEDA_FORMAT, valueDouble).replace('.',SEPARATOR));

        m_leftEditText.addTextChangedListener(m_leftUpdateField);
        m_rightEditText.addTextChangedListener(m_rightUpdateField);
    }

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

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}
