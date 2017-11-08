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
import java.util.List;

import sistemas.puc.com.finantialapp.data.FinantialContract;
import sistemas.puc.com.finantialapp.model.IndiceEnum;
import sistemas.puc.com.finantialapp.util.HttpUtils;
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
    private static final String CDI_URL = "https://www.cetip.com.br";
    private static final String IGPM_POUCPANCA_URL = "http://sisweb.tesouro.gov.br/apex/f?p=2551:2";

    private final String LOG_TAG = FetchIndicesTask.class.getSimpleName();
    private Context m_context;

    public FetchIndicesTask(Context context) {
        m_context = context;
    }

    @Override
    protected void onPreExecute(){}

    @Override
    protected List<ContentValues> doInBackground(Void... voids) {

        String selicJsonStr = HttpUtils.getStringFromURL(SELIC_URL);
        String inpcJsonStr = HttpUtils.getStringFromURL(INPC_URL);
        String ipcaJsonStr = HttpUtils.getStringFromURL(IPCA_URL);
        String cdiString = HttpUtils.getStringFromURL(CDI_URL);
        String igpmPoupancaString = HttpUtils.getStringFromURL(IGPM_POUCPANCA_URL);

        try {
            List<ContentValues> result = new ArrayList<>();
            result.add(getSelicFromJson(selicJsonStr));
            result.add(getCDIFromString(cdiString));
            result.add(getIndiceFromIBGEJson(ipcaJsonStr, IndiceEnum.IPCA));
            result.add(getIndiceFromIBGEJson(inpcJsonStr, IndiceEnum.INPC));
            result.add(getIGPMFromString(igpmPoupancaString));
            result.add(getPoupancaFromString(igpmPoupancaString));
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

    private ContentValues getIGPMFromString(String igpmString) {
        String parse[];
        parse = igpmString.split("IGPM - ")[1].split("</b>");
        long time = Util.getTimeFromDateString(parse[0], Util.DATE_TEMPLATE_FORMAT_4);
        parse = parse[1].split("<td align=\"center\" class=\"camposTesouroDireto taxaprecos\">");
        double month = Double.parseDouble(parse[1].split("</td>")[0].replace(',','.'));
        double year = Double.parseDouble(parse[2].split("</td>")[0].replace(',','.'));

        ContentValues IGPM = setupIndiceCV(IndiceEnum.IGPM);
        IGPM.put(FinantialContract.IndiceEntry.COLUMN_INDICE_MONTH_RATE, month/100.0);
        IGPM.put(FinantialContract.IndiceEntry.COLUMN_INDICE_YEAR_RATE, year/100.0);
        IGPM.put(FinantialContract.IndiceEntry.COLUMN_INDICE_DATE, time);

        return IGPM;
    }

    private ContentValues getPoupancaFromString(String poupancaString) {
        String parse[];
        parse = poupancaString.split("POUPANÇA - ")[1].split("</b>");
        long time = Util.getTimeFromDateString(parse[0], Util.DATE_TEMPLATE_FORMAT_4);
        parse = parse[1].split("<td align=\"center\" class=\"camposTesouroDireto taxaprecos\">");
        double month = Double.parseDouble(parse[1].split("</td>")[0].replace(',','.'));
        double year = Double.parseDouble(parse[2].split("</td>")[0].replace(',','.'));

        ContentValues poupanca = setupIndiceCV(IndiceEnum.POUPANCA);
        poupanca.put(FinantialContract.IndiceEntry.COLUMN_INDICE_MONTH_RATE, month/100.0);
        poupanca.put(FinantialContract.IndiceEntry.COLUMN_INDICE_YEAR_RATE, year/100.0);
        poupanca.put(FinantialContract.IndiceEntry.COLUMN_INDICE_DATE, time);

        return poupanca;
    }

    private ContentValues getCDIFromString(String cdiString) {
        cdiString = cdiString.split("ctl00_Banner_lblTaxDateDI\">")[1];
        cdiString = cdiString.substring(0, cdiString.indexOf("%</span"));
        String cdiDateString = cdiString.substring(1,11);

        double cdiRate = Double.parseDouble(
                cdiString.substring(cdiString.indexOf("TaxDI\">")+7, cdiString.length())
                        .replace(',','.'));

        long time = Util.getTimeFromDateString(cdiDateString, Util.DATE_TEMPLATE_FORMAT_2);

        ContentValues CDI = setupIndiceCV(IndiceEnum.CDI);
        CDI.put(FinantialContract.IndiceEntry.COLUMN_INDICE_MONTH_RATE, 0);
        CDI.put(FinantialContract.IndiceEntry.COLUMN_INDICE_YEAR_RATE, cdiRate/100.0);
        CDI.put(FinantialContract.IndiceEntry.COLUMN_INDICE_DATE, time);

        return CDI;
    }

    private ContentValues getSelicFromJson(String jsonStr) throws JSONException{

        ContentValues selic = setupIndiceCV(IndiceEnum.SELIC);

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
        ContentValues indice = setupIndiceCV(indiceEnum);

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

    private ContentValues setupIndiceCV(IndiceEnum indiceEnum) {
        ContentValues cv = new ContentValues();
        cv.put(FinantialContract.IndiceEntry.COLUMN_INDICE_CODE, indiceEnum.getCode());
        cv.put(FinantialContract.IndiceEntry.COLUMN_INDICE_NAME, indiceEnum.getName());
        cv.put(FinantialContract.IndiceEntry.COLUMN_INDICE_TYPE, indiceEnum.getType().ordinal());
        return cv;
    }
}
