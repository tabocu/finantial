package sistemas.puc.com.finantialapp;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sistemas.puc.com.finantialapp.data.FinantialContract;
import sistemas.puc.com.finantialapp.model.IndiceEnum;
import sistemas.puc.com.finantialapp.util.HttpUtils;
import sistemas.puc.com.finantialapp.util.Util;

public class FetchTesouroTask extends AsyncTask<Void, Void, List<ContentValues>>{

    private static final String TESOURO_URL = "http://www.tesouro.gov.br/tesouro-direto-precos-e-taxas-dos-titulos";

    private final String LOG_TAG = FetchTesouroTask.class.getSimpleName();
    private Context m_context;

    public FetchTesouroTask(Context context) {
        m_context = context;
    }

    @Override
    protected void onPreExecute(){}

    @Override
    protected List<ContentValues> doInBackground(Void... voids) {

        String tesouroString = HttpUtils.getStringFromURL(TESOURO_URL);

        try {
            return getTesouroFromString(tesouroString);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... voids) {}

    @Override
    protected void onPostExecute(List<ContentValues> result) {
        if (result.size() > 0) {
            m_context.getContentResolver().delete
                    (FinantialContract.TesouroEntry.CONTENT_URI, null, null);
            ContentValues[] cvArray = result.toArray(new ContentValues[result.size()]);
            m_context.getContentResolver().bulkInsert
                    (FinantialContract.TesouroEntry.CONTENT_URI, cvArray);
        }
    }

    private List<ContentValues> getTesouroFromString(String tesouroString) throws Exception {
        // Begin
        HashMap<String, LinkedList<String>> tesouroHash = new HashMap<>();

        Pattern vendaPatern = Pattern.compile("<tr class=\"camposTesouroDireto\">(.+?)</tr>");
        Pattern compraPatern = Pattern.compile("<tr class=\"camposTesouroDireto \">(.+?)</tr>");
        Pattern contentPatern = Pattern.compile("<td[^>]*>(.+?)</td>");

        Matcher lineMatcher;
        Matcher fieldMatcher;

        lineMatcher = vendaPatern.matcher(tesouroString);
        while (lineMatcher.find()) {
            fieldMatcher = contentPatern.matcher(lineMatcher.group(1));
            LinkedList<String> titulo = new LinkedList<>();
            while (fieldMatcher.find()) {
                titulo.add(fieldMatcher.group(1));
            }
            tesouroHash.put(titulo.element(), titulo);
        }

        lineMatcher = compraPatern.matcher(tesouroString);
        while (lineMatcher.find()) {
            fieldMatcher = contentPatern.matcher(lineMatcher.group(1));
            fieldMatcher.find();
            LinkedList<String> titulo = tesouroHash.get(fieldMatcher.group(1));
            fieldMatcher.find();
            while (fieldMatcher.find()) {
                titulo.add(fieldMatcher.group(1));
            }
        }
        // End

        ArrayList<LinkedList<String>> titleList = new ArrayList<>(tesouroHash.values());
        List<ContentValues> result = new ArrayList<>();
        String name, mode;
        long expiration;
        double sellIncome, sellPrice, buyIncome, buyMin, buyPrice;
        ListIterator<String> iterator;

        for (LinkedList<String> title : titleList) {
            iterator = title.listIterator();
            String[] split = iterator.next().split(" \\(");
            name = split[0];
            mode = split[1].substring(0, split[1].indexOf(')'));
            expiration = Util.getTimeFromDateString(iterator.next(), Util.DATE_TEMPLATE_FORMAT_2);
            sellIncome = Double.parseDouble(iterator.next().replace(',','.'))/100.0;
            sellPrice = Double.parseDouble(iterator.next()
                    .replace("R$","")
                    .replace(".","")
                    .replace(",","."));

            ContentValues tesouroItem = new ContentValues();
            tesouroItem.put(FinantialContract.TesouroEntry.COLUMN_TESOURO_NAME, name);
            tesouroItem.put(FinantialContract.TesouroEntry.COLUMN_TESOURO_MODE, mode);
            tesouroItem.put(FinantialContract.TesouroEntry.COLUMN_TESOURO_EXPIRATION_DATE, expiration);
            tesouroItem.put(FinantialContract.TesouroEntry.COLUMN_TESOURO_YEAR, Util.getYearIntFromLong(expiration));
            tesouroItem.put(FinantialContract.TesouroEntry.COLUMN_TESOURO_SELLING_INCOME, sellIncome);
            tesouroItem.put(FinantialContract.TesouroEntry.COLUMN_TESOURO_SELLING_PRICE, sellPrice);

            if (title.size() > 5) {
                buyIncome = Double.parseDouble(iterator.next().replace(',','.'))/100.0;
                buyMin = Double.parseDouble(iterator.next()
                        .replace("R$","")
                        .replace(".","")
                        .replace(",","."));
                buyPrice = Double.parseDouble(iterator.next()
                        .replace("R$","")
                        .replace(".","")
                        .replace(",","."));
                tesouroItem.put(FinantialContract.TesouroEntry.COLUMN_TESOURO_BUYING_INCOME, buyIncome);
                tesouroItem.put(FinantialContract.TesouroEntry.COLUMN_TESOURO_BUYING_MIN_VALUE, buyMin);
                tesouroItem.put(FinantialContract.TesouroEntry.COLUMN_TESOURO_BUYING_PRICE, buyPrice);
            }

            result.add(tesouroItem);
        }

        return result;
    }
}
