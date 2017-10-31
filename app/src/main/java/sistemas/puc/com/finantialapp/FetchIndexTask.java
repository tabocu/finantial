package sistemas.puc.com.finantialapp;

import android.content.ContentValues;
import android.content.Context;
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

import sistemas.puc.com.finantialapp.data.FinantialContract;
import sistemas.puc.com.finantialapp.entities.MoedaItem;
import sistemas.puc.com.finantialapp.model.MoedaMap;
import sistemas.puc.com.finantialapp.util.Util;

public class FetchIndexTask extends AsyncTask<Void, Void, List<ContentValues>>{

    private final static String BCB_VALUE = "valor";
    private final static String BCB_DATE = "data";
    private static final String SERIES_NUMBER = "{series_number}";

    private static final String BCB_BASE_URL = "http://api.bcb.gov.br/dados/serie/bcdata.sgs."
            +SERIES_NUMBER
            +"/dados/ultimos/1?formato=json";

    private static final String BCB_SELIC = "1178";

    private static final String IPCA_COMERCIALIZAVEIS = "4447";

    private final String LOG_TAG = FetchIndexTask.class.getSimpleName();
    private Context m_context;

    public FetchIndexTask(Context context) {
        m_context = context;
    }

    @Override
    protected void onPreExecute(){}

    @Override
    protected List<ContentValues> doInBackground(Void... voids) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String selicJsonStr = null;

        try {
            String selicBaseUrl = BCB_BASE_URL.replace(SERIES_NUMBER, BCB_SELIC);

            Uri selicBuiltUri = Uri.parse(selicBaseUrl).buildUpon().build();
            URL selicUrl = new URL(selicBuiltUri.toString());

            // Create the request and open the connection
            urlConnection = (HttpURLConnection) selicUrl.openConnection();
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
                selicJsonStr = buffer.toString();
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
            List<ContentValues> result = new ArrayList<>();
            result.add(getSelicFromJson(selicJsonStr));
            return result;
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
        if (result.size() > 0) {
            m_context.getContentResolver().delete(FinantialContract.MoedaEntry.CONTENT_URI, null, null);
            ContentValues[] cvArray = result.toArray(new ContentValues[result.size()]);
            m_context.getContentResolver().bulkInsert(FinantialContract.MoedaEntry.CONTENT_URI, cvArray);
        }
    }

    private ContentValues getSelicFromJson(String jsonStr) throws JSONException{


        ContentValues selic = new ContentValues();

        JSONArray jsonArray = new JSONArray(jsonStr);

        String date;
        long time;
        double value;
        for (int i=0; i<jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            date = obj.getString(BCB_DATE);
            time = Util.getTimeFromDateString(date);
            value = obj.getDouble(BCB_VALUE);

            selic.put(FinantialContract.IndiceEntry.COLUMN_INDICE_CODE, "??");
            selic.put(FinantialContract.IndiceEntry.COLUMN_INDICE_NAME, "Selic");
            selic.put(FinantialContract.IndiceEntry.COLUMN_INDICE_YEAR_RATE, value);
            selic.put(FinantialContract.IndiceEntry.COLUMN_INDICE_DATE, time);
            selic.put(FinantialContract.IndiceEntry.COLUMN_INDICE_TYPE, "??");
            selic.put(FinantialContract.IndiceEntry.COLUMN_INDICE_MONTH_RATE, "??");
        }

        return selic;
    }
}
