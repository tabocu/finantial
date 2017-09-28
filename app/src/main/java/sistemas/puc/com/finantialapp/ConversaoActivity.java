package sistemas.puc.com.finantialapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import sistemas.puc.com.finantialapp.entities.MoedaItem;
import sistemas.puc.com.finantialapp.model.Database;

public class ConversaoActivity extends AppCompatActivity {

    public final static String EXTRA_MOEDA_ESQ = "extraMoedaEsq";
    public final static String EXTRA_MOEDA_DIR = "extraMoedaDir";

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

        Bundle b = getIntent().getExtras();
        if (b != null) {
            String moeda_esq = b.getString(EXTRA_MOEDA_ESQ);
            String moeda_dir = b.getString(EXTRA_MOEDA_DIR);

            // TODO Buscar a posição da moeda no array corretamente.
            int moeda_esq_position = 0;
            int moeda_dir_position = 0;
            for (int i = 0; i < m_moedaEsqSpinner.getAdapter().getCount(); i++) {
                if (((MoedaItem)m_moedaEsqSpinner.getItemAtPosition(i)).m_nome.equals(moeda_esq)) {
                    moeda_esq_position = i;
                }
                else if (((MoedaItem)m_moedaEsqSpinner.getItemAtPosition(i)).m_nome.equals(moeda_dir)) {
                    moeda_dir_position = i;
                }
            }

            m_moedaEsqSpinner.setSelection(moeda_esq_position);
            m_moedaDirSpinner.setSelection(moeda_dir_position);
        }
    }
}
