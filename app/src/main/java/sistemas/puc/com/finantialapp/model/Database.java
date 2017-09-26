package sistemas.puc.com.finantialapp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sistemas.puc.com.finantialapp.entities.MoedaItem;

public class Database {

    private static final MoedaItem[] moeda_data = {
            new MoedaItem("Dolar", "R$3,1133", "06/09/2017"),
            new MoedaItem("Libra", "R$4,0628", "06/09/2017"),
            new MoedaItem("Euro", "R$3,7145", "06/09/2017")
    };

    private Database() {}

    public static List<MoedaItem> getMoedaList() {
        return new ArrayList<>(Arrays.asList(moeda_data));
    }
}
