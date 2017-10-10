package sistemas.puc.com.finantialapp.model;

import android.support.annotation.NonNull;
import android.support.annotation.Size;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class MoedaMap {
    private static final HashMap<String, String> MOEDA_MAP = new HashMap<>(initializeMap());

    private MoedaMap() {}

    // TODO: Move map to resources to enable translation.
    private static Map<String, String> initializeMap() {
        Map<String, String> result = new HashMap<String, String>();

        result.put("AUD", "Dólar Australiano");
        result.put("BGN", "Lev Búlgaro");
        result.put("BRL", "Real");
        result.put("CAD", "Dólar Canadense");
        result.put("CHF", "Franco Suíço");
        result.put("CNY", "Renminbi");
        result.put("CZK", "Coroa Checa");
        result.put("DKK", "Coroa Dinamarquesa");
        result.put("EUR", "Euro");
        result.put("GBP", "Libra Esterlina");
        result.put("HKD", "Dólar de Hong Kong");
        result.put("HRK", "Kuna Croata");
        result.put("HUF", "Forint");
        result.put("IDR", "Rúpia Indonésia");
        result.put("ILS", "Shekel");
        result.put("INR", "Rúpia Indiana");
        result.put("JPY", "Iene");
        result.put("KRW", "Won Sul-coreano");
        result.put("MXN", "Peso Mexicano");
        result.put("MYR", "Ringgit");
        result.put("NOK", "Coroa Norueguesa");
        result.put("NZD", "Dólar da Nóva Zelândia");
        result.put("PHP", "Peso Filipino");
        result.put("PLN", "Zloty");
        result.put("RON", "Leu Romeno");
        result.put("RUB", "Rublo");
        result.put("SEK", "Coroa Sueca");
        result.put("SGD", "Dólar de Singapura");
        result.put("THB", "Baht");
        result.put("TRY", "Nova Lira Turca");
        result.put("USD", "Dólar Americano");
        result.put("ZAR", "Rand");

        return Collections.unmodifiableMap(result);
    }

    /**
     * @param code Currency code following ISO 4217.
     * @return     The currency name in portuguese.
     */
    public static String getCurrencyName(@NonNull @Size(3) String code) {
        return MOEDA_MAP.get(code);
    }
}
