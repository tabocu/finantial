package sistemas.puc.com.finantialapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

public class ConversaoActivity extends AppCompatActivity {

    EditText m_moedaEsqEditText;
    EditText m_moedaDirEditText;
    Spinner m_moedaEsqSpinner;
    Spinner m_moedaDirSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversao);
        m_moedaEsqEditText = (EditText) findViewById(R.id.moeda_esq_edittext);
        m_moedaDirEditText = (EditText) findViewById(R.id.moeda_dir_edittext);
        m_moedaEsqSpinner = (Spinner) findViewById(R.id.moeda_esq_spinner);
        m_moedaDirSpinner = (Spinner) findViewById(R.id.moeda_dir_spinner);
    }
}
