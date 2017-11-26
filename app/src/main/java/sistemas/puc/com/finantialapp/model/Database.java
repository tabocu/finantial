package sistemas.puc.com.finantialapp.model;

import android.content.Context;
import android.widget.Toast;

import sistemas.puc.com.finantialapp.FetchIndicesTask;
import sistemas.puc.com.finantialapp.FetchRatesTask;
import sistemas.puc.com.finantialapp.FetchTesouroTask;
import sistemas.puc.com.finantialapp.moeda.MoedaFragment;

public final class Database {

    private Database() {}

    public static void update(Context c) {
        FetchRatesTask frt = new FetchRatesTask(c);
        frt.execute(MoedaFragment.MOEDA_BASE);
        FetchIndicesTask fit = new FetchIndicesTask(c);
        fit.execute();
        FetchTesouroTask ftt = new FetchTesouroTask(c);
        ftt.execute();
        Toast.makeText(c, "Database updated.", Toast.LENGTH_SHORT).show();
    }
}
