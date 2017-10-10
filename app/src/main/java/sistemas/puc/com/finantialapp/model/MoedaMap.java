package sistemas.puc.com.finantialapp.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MoedaMap {
    private static final HashMap<String, String> MOEDA_MAP = new HashMap<>(initializeMap());

    private static Map<String, String> initializeMap() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("USD", "Dolar");
        return Collections.unmodifiableMap(result);
    }
}
