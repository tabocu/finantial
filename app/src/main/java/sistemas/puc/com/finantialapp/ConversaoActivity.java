package sistemas.puc.com.finantialapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import sistemas.puc.com.finantialapp.entities.MoedaItem;
import sistemas.puc.com.finantialapp.model.Database;

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

        List<MoedaItem> moedaList = Database.getMoedaList();

        ArrayAdapter<MoedaItem> moedaEsqSpinnerAdapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, moedaList);
        moedaEsqSpinnerAdapter.setDropDownViewResource(
                R.layout.support_simple_spinner_dropdown_item);

        ArrayAdapter<MoedaItem> moedaDirSpinnerAdapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, moedaList);
        moedaDirSpinnerAdapter.setDropDownViewResource(
                R.layout.support_simple_spinner_dropdown_item);

        m_moedaEsqSpinner.setAdapter(moedaEsqSpinnerAdapter);
        m_moedaDirSpinner.setAdapter(moedaDirSpinnerAdapter);
    }
}
