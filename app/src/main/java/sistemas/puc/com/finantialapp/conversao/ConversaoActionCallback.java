package sistemas.puc.com.finantialapp.conversao;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import sistemas.puc.com.finantialapp.data.FinantialContract.MoedaEntry;
import sistemas.puc.com.finantialapp.util.FloatingNumberMask;

public class ConversaoActionCallback implements AdapterView.OnItemSelectedListener {

    static final String LOG_TAG = "ConversaoActionCallback";

    static final Uri URI = MoedaEntry.CONTENT_URI;

    static final int COLUMN_INDEX_CODE = 1;
    static final int COLUMN_INDEX_NAME = 2;
    static final int COLUMN_INDEX_RATE = 3;

    static final String[] COLUMNS = new String[] {
            MoedaEntry.TABLE_NAME + "." + MoedaEntry._ID,
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_CODE,
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_NAME,
            MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_RATE,
    };

    static final String SELECTION =
            MoedaEntry.TABLE_NAME + "." + MoedaEntry._ID + " = ? ";

    static final String MOEDA_FORMAT = "%1$.4f";
    static final char SEPARATOR = ',';

    final Context mContext;
    final Spinner mLeftSpinner;
    final Spinner mRightSpinner;
    final TextInputLayout mLeftText;
    final TextInputLayout mRightText;

    final FieldUpdater mLeftFieldUpdater;
    final FieldUpdater mRightFieldUpdater;

    boolean mIsEditing = false;

    public ConversaoActionCallback(Context context,
                                   Spinner leftSpinner,
                                   Spinner rightSpinner,
                                   TextInputLayout leftText,
                                   TextInputLayout rightText) {
        mContext = context;
        mLeftSpinner = leftSpinner;
        mRightSpinner = rightSpinner;
        mLeftText = leftText;
        mRightText = rightText;

        // Setting floating number masks
        setupFloatingNumberMask(leftText);
        setupFloatingNumberMask(rightText);

        mLeftFieldUpdater = new FieldUpdater(mLeftText, mRightText);
        mRightFieldUpdater = new FieldUpdater(mRightText, mLeftText);

        mLeftText.getEditText().addTextChangedListener(mLeftFieldUpdater);
        mRightText.getEditText().addTextChangedListener(mRightFieldUpdater);

        mLeftSpinner.setOnItemSelectedListener(this);
        mRightSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        double rate = getLeftRate()/getRightRate();

        Log.v(LOG_TAG,"Selected rate = " + rate);

        mLeftFieldUpdater.setRate(rate);
        mRightFieldUpdater.setRate(1.0/rate);
        mLeftFieldUpdater.forceUpdate();

        updateHint(mLeftSpinner, mLeftText);
        updateHint(mRightSpinner, mRightText);
    }

    public double getLeftRate() { return getRate(mLeftSpinner); }

    public double getLeftValue() { return getValue(mLeftText); }

    public double getRightRate() { return getRate(mRightSpinner); }

    public double getRightValue() { return getValue(mRightText); }

    public void setLeftValue(double value) { setValue(mLeftText, value); }

    public void setRightValue(double value) { setValue(mRightText, value); }

    private double getRate(Spinner spinner) {
        long id = spinner.getSelectedItemId();
        Cursor cursor = getCursorById(id);
        return cursor.getDouble(COLUMN_INDEX_RATE);
    }

    private double getValue(TextInputLayout text) {
        String valueString = text.getEditText().getText().toString()
                .replace(SEPARATOR,'.');
        return valueString.isEmpty() ? 0.0 : Double.valueOf(valueString);
    }

    private void setValue(TextInputLayout text, double value) {
        text.getEditText().setText(String.format(MOEDA_FORMAT, value)
                .replace('.',SEPARATOR));
    }

    private Cursor getCursorById(long id) {
        Cursor cursor = mContext.getContentResolver().query(
                URI,
                COLUMNS,
                SELECTION,
                new String[] { String.valueOf(id) },
                null);
        cursor.moveToFirst();
        return cursor;
    }

    private void updateHint(Spinner spinner, TextInputLayout text) {
        long id = spinner.getSelectedItemId();
        Cursor cursor = getCursorById(id);

        String code = cursor.getString(COLUMN_INDEX_CODE);
        String name = cursor.getString(COLUMN_INDEX_NAME);

        String hint =  new StringBuilder()
                .append(code)
                .append(" - ")
                .append(name)
                .toString();

        text.setHint(hint);
    }

    private void setupFloatingNumberMask(TextInputLayout text) {
        FloatingNumberMask mask = new FloatingNumberMask(
                text.getEditText(),
                SEPARATOR,
                4);
        text.getEditText().addTextChangedListener(mask);
    }

    class FieldUpdater implements TextWatcher {

        TextInputLayout mFromText;
        TextInputLayout mToText;
        double mRate;

        public FieldUpdater(TextInputLayout fromText, TextInputLayout toText) {
            mFromText = fromText;
            mToText = toText;
            mRate = 1.0;
        }

        public void setRate(double rate) { mRate = rate; }

        public void forceUpdate() { afterTextChanged(null); }

        @Override
        public void afterTextChanged(Editable s) {
            if (mIsEditing) return;
            mIsEditing = true;

            double value = getValue(mFromText)*mRate;
            setValue(mToText, value);

            mIsEditing = false;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}
