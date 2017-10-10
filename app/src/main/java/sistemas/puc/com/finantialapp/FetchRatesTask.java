package sistemas.puc.com.finantialapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
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

import sistemas.puc.com.finantialapp.entities.MoedaItem;
import sistemas.puc.com.finantialapp.model.MoedaMap;
import sistemas.puc.com.finantialapp.util.Util;

public class FetchRatesTask extends AsyncTask<String, Void, List<MoedaItem>>{

    private static final String FIXER_URL = "http://api.fixer.io/latest?";
    private static final String BASE_PARAM = "base";
    private final String LOG_TAG = FetchRatesTask.class.getSimpleName();

    @Override
    protected void onPreExecute(){}

    @Override
    protected List<MoedaItem> doInBackground(String... params) {

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
    protected void onPostExecute(List<MoedaItem> result) {}

    private List<MoedaItem> getRatesFromJson(String jsonStr) throws JSONException{

        final String FIXER_RATES = "rates";
        final String FIXER_DATE = "date";

        List<MoedaItem> result = new ArrayList<>();

        JSONObject json = new JSONObject(jsonStr);
        JSONObject ratesJson = json.getJSONObject(FIXER_RATES);
        String date = json.getString(FIXER_DATE);
        long time = Util.getTimeFromDateString(date);

        Iterator<String> rates = ratesJson.keys();

        while (rates.hasNext()) {
            String code = rates.next();
            String name = MoedaMap.getCurrencyName(code);
            double rate = ratesJson.getDouble(code);
            result.add(new MoedaItem(code, name, rate, time));
        }

        return result;
    }
}
