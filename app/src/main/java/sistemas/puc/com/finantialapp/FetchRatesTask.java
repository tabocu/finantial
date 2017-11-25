package sistemas.puc.com.finantialapp;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import sistemas.puc.com.finantialapp.moeda.MoedaFragment;
import sistemas.puc.com.finantialapp.data.FinantialContract.MoedaEntry;
import sistemas.puc.com.finantialapp.model.MoedaMap;
import sistemas.puc.com.finantialapp.util.Util;

public class FetchRatesTask extends AsyncTask<String, Void, List<ContentValues>>{

    private static final String FIXER_URL = "http://api.fixer.io/latest?";
    private static final String BASE_PARAM = "base";
    private final String LOG_TAG = FetchRatesTask.class.getSimpleName();
    private Context m_context;

    public FetchRatesTask(Context context) {
        m_context = context;
    }

    @Override
    protected void onPreExecute(){}

    @Override
    protected List<ContentValues> doInBackground(String... params) {

        if (params.length != 1) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String ratesJsonStr = null;

        try {
            Uri builtUri = Uri.parse(FIXER_URL).buildUpon()
                    .appendQueryParameter(BASE_PARAM, params[0])
                    .build();
            URL url = new URL(builtUri.toString());

            // Create the request and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() != 0) {
                ratesJsonStr = buffer.toString();
            } else {
                return null;
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getRatesFromJson(ratesJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... voids) {}

    @Override
    protected void onPostExecute(List<ContentValues> result) {
        if (result != null) {
            // iterate over all contentValues
            for (ContentValues contentValues : result) {
                // get moeda code and try to update an existent moeda
                String code = (String) contentValues.get(MoedaEntry.COLUMN_MOEDA_CODE);
                int updatedRows = m_context.getContentResolver().update(
                        MoedaEntry.CONTENT_URI,
                        contentValues,
                        MoedaEntry.TABLE_NAME + "." + MoedaEntry.COLUMN_MOEDA_CODE + " = ?",
                        new String[] {code});

                // check if one or none moeda were updated
                assert (updatedRows == 0 || updatedRows == 1);

                // if no moeda were updated, insert a new moeda
                if (updatedRows == 0) {
                    // mark the new moeda as not favorite
                    contentValues.put(MoedaEntry.COLUMN_MOEDA_FAVORITE, 0);
                    m_context.getContentResolver().insert(
                            MoedaEntry.CONTENT_URI,
                            contentValues);
                }
            }
        }
    }

    private List<ContentValues> getRatesFromJson(String jsonStr) throws JSONException{

        final String FIXER_RATES = "rates";
        final String FIXER_DATE = "date";

        List<ContentValues> result = new ArrayList<>();

        JSONObject json = new JSONObject(jsonStr);
        JSONObject ratesJson = json.getJSONObject(FIXER_RATES);
        String date = json.getString(FIXER_DATE);
        long time = Util.getTimeFromDateString(date, Util.DATE_TEMPLATE_FORMAT_1);

        Iterator<String> rates = ratesJson.keys();

        while (rates.hasNext()) {
            String code = rates.next();
            String name = MoedaMap.getCurrencyName(code);
            double rate = 1.0/ratesJson.getDouble(code);

            ContentValues moedaValues = new ContentValues();
            moedaValues.put(MoedaEntry.COLUMN_MOEDA_CODE, code);
            moedaValues.put(MoedaEntry.COLUMN_MOEDA_NAME, name);
            moedaValues.put(MoedaEntry.COLUMN_MOEDA_RATE, rate);
            moedaValues.put(MoedaEntry.COLUMN_MOEDA_DATE, time);

            result.add(moedaValues);
        }

        ContentValues baseMoedaValue = new ContentValues();
        String baseMoedaName = MoedaMap.getCurrencyName(MoedaFragment.MOEDA_BASE);
        baseMoedaValue.put(MoedaEntry.COLUMN_MOEDA_CODE, MoedaFragment.MOEDA_BASE);
        baseMoedaValue.put(MoedaEntry.COLUMN_MOEDA_NAME, baseMoedaName);
        baseMoedaValue.put(MoedaEntry.COLUMN_MOEDA_RATE, 1);
        baseMoedaValue.put(MoedaEntry.COLUMN_MOEDA_DATE, time);
        result.add(baseMoedaValue);

        return result;
    }
}
