package sistemas.puc.com.finantialapp.model;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sistemas.puc.com.finantialapp.FetchRatesTask;
import sistemas.puc.com.finantialapp.entities.IndiceItem;
import sistemas.puc.com.finantialapp.entities.MoedaItem;
import sistemas.puc.com.finantialapp.entities.TesouroItem;
import sistemas.puc.com.finantialapp.util.Util;

public final class Database {

    private static final MoedaItem[] s_moedaData = {
            new MoedaItem("USD", "Dolar", 3.1133, Util.getTimeFromDate(7,10,2017)),
            new MoedaItem("GBP", "Libra", 4.0628, Util.getTimeFromDate(7,10,2017)),
            new MoedaItem("EUR", "Euro", 3.7145, Util.getTimeFromDate(7,10,2017))
    };

    private static final TesouroItem[] s_tesouroData = {
            new TesouroItem(
                    "Tesouro IPCA+ 2024",
                    "15/08/2024",
                    "4,66","4,78",
                    "2.194,90","2.177,62"),
            new TesouroItem(
                    "Tesouro IPCA+ 2035",
                    "15/05/2035",
                    "5,01","5,13",
                    "1.270,65","1.245,36"),
            new TesouroItem(
                    "Tesouro IPCA+ 2045",
                    "15/05/2045",
                    "5,01","5,13",
                    "780,38","756,19"),
            new TesouroItem(
                    "Tesouro SELIC 2023",
                    "15/05/2045",
                    "0,01","0,05",
                    "9.077,87","9.058,13"),
    };

    private static final IndiceItem[] s_indiceData = {
            new IndiceItem.Simples("CDI", "11/09/2017", "8,14%"),
            new IndiceItem.Simples("SELIC", "11/09/2017", "8,15%"),
            new IndiceItem.Duplo("Poupança", "Setembro", "0,50%", "2017", "5,22%"),
            new IndiceItem.Duplo("IPCA", "Agosto", "0,19%", "2017", "1,62%"),
            new IndiceItem.Duplo("INPC", "Agosto", "-0,03%", "2017", "1,27%"),
            new IndiceItem.Duplo("IGP-M", "Agosto", "0,10%", "2017", "-2,57%"),
    };

    private Database() {}

    public static List<MoedaItem> getMoedaList() {
        return new ArrayList<>(Arrays.asList(s_moedaData));
    }

    public static List<TesouroItem> getTesouroList() {
        return new ArrayList<>(Arrays.asList(s_tesouroData));
    }

    public static List<IndiceItem> getIndiceList() {
        return new ArrayList<>(Arrays.asList(s_indiceData));
    }

    public static void update(Context c) {
        FetchRatesTask frt = new FetchRatesTask();
        frt.execute("BRL"); // TODO: where should this string come from?
        Toast.makeText(c, "Database updated.", Toast.LENGTH_SHORT).show();
    }
}
