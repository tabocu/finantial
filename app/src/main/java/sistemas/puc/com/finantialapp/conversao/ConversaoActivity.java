package sistemas.puc.com.finantialapp.conversao;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;

import sistemas.puc.com.finantialapp.R;

public class ConversaoActivity extends AppCompatActivity {

    public final static String EXTRA_LEFT_MOEDA = "extraMoedaEsq";
    public final static String EXTRA_RIGHT_MOEDA = "extraMoedaDir";

    final static double DEFAULT_LEFT_VALUE = 1.0;

    TextInputLayout mLeftTextInputLayout;
    TextInputLayout mRightTextInputLayout;

    Spinner mLeftSpinner;
    Spinner mRightSpinner;

    SimpleCursorAdapter mLeftSpinnerAdapter;
    SimpleCursorAdapter mRightSpinnerAdapter;

    ConversaoActionCallback mActionCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversao);

        // Getting views
        mLeftTextInputLayout = (TextInputLayout) findViewById(R.id.left_textinputlayout_moeda);
        mRightTextInputLayout = (TextInputLayout) findViewById(R.id.right_textinputlayout_moeda);

        mLeftSpinner = (Spinner) findViewById(R.id.left_spinner_moeda);
        mRightSpinner = (Spinner) findViewById(R.id.right_spinner_moeda);

        mLeftSpinnerAdapter = new ConversaoCursorAdapter(this);
        mRightSpinnerAdapter = new ConversaoCursorAdapter(this);

        mLeftSpinner.setAdapter(mLeftSpinnerAdapter);
        mRightSpinner.setAdapter(mRightSpinnerAdapter);

        mActionCallback = new ConversaoActionCallback(
                this,
                mLeftSpinner,
                mRightSpinner,
                mLeftTextInputLayout,
                mRightTextInputLayout);

        mActionCallback.setLeftValue(DEFAULT_LEFT_VALUE);

        // Getting bundle arguments
        String leftCode = "USD";
        String rightCode = "USD";

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            leftCode = arguments.getString(EXTRA_LEFT_MOEDA);
            rightCode = arguments.getString(EXTRA_RIGHT_MOEDA);
        }

        // Starts the callback
        getSupportLoaderManager().initLoader(
                0,
                null,
                new ConversaoLoaderCallback(
                        this,
                        mLeftSpinner,
                        leftCode));

        getSupportLoaderManager().initLoader(
                1,
                null,
                new ConversaoLoaderCallback(
                        this,
                        mRightSpinner,
                        rightCode));
    }
}
