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
import java.util.List;

import sistemas.puc.com.finantialapp.data.FinantialContract;
import sistemas.puc.com.finantialapp.model.IndiceEnum;
import sistemas.puc.com.finantialapp.util.Util;

public class FetchIndicesTask extends AsyncTask<Void, Void, List<ContentValues>>{

    private final static String BCB_VALUE = "valor";
    private final static String BCB_DATE = "data";

    private final static String IBGE_VALUE = "v";
    private final static String IBGE_DATE = "p_cod";
    private final static int IBGE_MENSAL = 0;
    private final static int IBGE_ANUAL = 1;

    private static final String SELIC_URL = "http://api.bcb.gov.br/dados/serie/bcdata.sgs.1178/dados/ultimos/1?formato=json";
    private static final String IPCA_URL = "http://servicodados.ibge.gov.br/api/v2/conjunturais/1419/periodos/-1/indicadores";
    private static final String INPC_URL = "http://servicodados.ibge.gov.br/api/v2/conjunturais/1100/periodos/-1/indicadores";

    private final String LOG_TAG = FetchIndicesTask.class.getSimpleName();
    private Context m_context;

    public FetchIndicesTask(Context context) {
        m_context = context;
    }

    @Override
    protected void onPreExecute(){}

    @Override
    protected List<ContentValues> doInBackground(Void... voids) {

        String selicJsonStr = getJsonStr(SELIC_URL);
        String inpcJsonStr = getJsonStr(INPC_URL);
        String ipcaJsonStr = getJsonStr(IPCA_URL);

        //placeholders

        ContentValues CDI = new ContentValues();
        setupIndiceCV(CDI, IndiceEnum.CDI);
        CDI.put(FinantialContract.IndiceEntry.COLUMN_INDICE_MONTH_RATE, 0.0);
        CDI.put(FinantialContract.IndiceEntry.COLUMN_INDICE_YEAR_RATE, 0.0);
        CDI.put(FinantialContract.IndiceEntry.COLUMN_INDICE_DATE, "201709");

        ContentValues poupanca = new ContentValues();
        setupIndiceCV(poupanca, IndiceEnum.POUPANCA);
        poupanca.put(FinantialContract.IndiceEntry.COLUMN_INDICE_MONTH_RATE, 0.0);
        poupanca.put(FinantialContract.IndiceEntry.COLUMN_INDICE_YEAR_RATE, 0.0);
        poupanca.put(FinantialContract.IndiceEntry.COLUMN_INDICE_DATE, "201709");

        ContentValues IGPM = new ContentValues();
        setupIndiceCV(IGPM, IndiceEnum.IGPM);
        IGPM.put(FinantialContract.IndiceEntry.COLUMN_INDICE_MONTH_RATE, 0.0);
        IGPM.put(FinantialContract.IndiceEntry.COLUMN_INDICE_YEAR_RATE, 0.0);
        IGPM.put(FinantialContract.IndiceEntry.COLUMN_INDICE_DATE, "201709");

        try {
            List<ContentValues> result = new ArrayList<>();
            result.add(getSelicFromJson(selicJsonStr));
            result.add(CDI);
            result.add(getIndiceFromIBGEJson(ipcaJsonStr, IndiceEnum.IPCA));
            result.add(getIndiceFromIBGEJson(inpcJsonStr, IndiceEnum.INPC));
            result.add(IGPM);
            result.add(poupanca);
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
            m_context.getContentResolver().delete
                    (FinantialContract.IndiceEntry.CONTENT_URI, null, null);
            ContentValues[] cvArray = result.toArray(new ContentValues[result.size()]);
            m_context.getContentResolver().bulkInsert
                    (FinantialContract.IndiceEntry.CONTENT_URI, cvArray);
        }
    }

    private String getJsonStr(String URL) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            Uri builtURI = Uri.parse(URL).buildUpon().build();
            URL indexUrl = new URL(builtURI.toString());

            // Create the request and open the connection
            urlConnection = (HttpURLConnection) indexUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder stringBuilder = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

            if (stringBuilder.length() != 0) {
                return stringBuilder.toString();
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
    }

    private ContentValues getSelicFromJson(String jsonStr) throws JSONException{

        ContentValues selic = new ContentValues();
        setupIndiceCV(selic, IndiceEnum.SELIC);

        JSONArray jsonArray = new JSONArray(jsonStr);

        String date;
        long time;
        double value;

        JSONObject obj = jsonArray.getJSONObject(jsonArray.length()-1);

        date = obj.getString(BCB_DATE);
        time = Util.getTimeFromDateString(date, Util.DATE_TEMPLATE_FORMAT_2);
        value = obj.getDouble(BCB_VALUE)/100.0;

        selic.put(FinantialContract.IndiceEntry.COLUMN_INDICE_YEAR_RATE, value);
        selic.put(FinantialContract.IndiceEntry.COLUMN_INDICE_MONTH_RATE, 0);
        selic.put(FinantialContract.IndiceEntry.COLUMN_INDICE_DATE, time);

        return selic;
    }

    private ContentValues getIndiceFromIBGEJson(String jsonStr,
                                                IndiceEnum indiceEnum) throws JSONException {
        ContentValues indice = new ContentValues();
        setupIndiceCV(indice, indiceEnum);

        JSONArray jsonArray = new JSONArray(jsonStr);

        String date;
        long time;
        double value;

        JSONObject obj = jsonArray.getJSONObject(IBGE_ANUAL);
        date = obj.getString(IBGE_DATE);
        time = Util.getTimeFromDateString(date, Util.DATE_TEMPLATE_FORMAT_3);
        value = obj.getDouble(IBGE_VALUE)/100.0;

        indice.put(FinantialContract.IndiceEntry.COLUMN_INDICE_YEAR_RATE, value);
        indice.put(FinantialContract.IndiceEntry.COLUMN_INDICE_DATE, time);


        obj = jsonArray.getJSONObject(IBGE_MENSAL);
        value = obj.getDouble(IBGE_VALUE)/100.0;
        indice.put(FinantialContract.IndiceEntry.COLUMN_INDICE_MONTH_RATE, value);

        return indice;
    }

    private void setupIndiceCV(ContentValues cv, IndiceEnum indiceEnum) {
        cv.put(FinantialContract.IndiceEntry.COLUMN_INDICE_CODE, indiceEnum.getCode());
        cv.put(FinantialContract.IndiceEntry.COLUMN_INDICE_NAME, indiceEnum.getName());
        cv.put(FinantialContract.IndiceEntry.COLUMN_INDICE_TYPE, indiceEnum.getType().ordinal());
    }
}
