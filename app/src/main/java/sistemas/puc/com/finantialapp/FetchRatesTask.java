package sistemas.puc.com.finantialapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import sistemas.puc.com.finantialapp.entities.MoedaItem;

public class FetchRatesTask extends AsyncTask<Void, Void, List<MoedaItem>>{

    private static final String FIXER_URL = "http://api.fixer.io/latest?base=BRL";
    private final String LOG_TAG = FetchRatesTask.class.getSimpleName();

    @Override
    protected void onPreExecute(){

    }

    @Override
    protected List<MoedaItem> doInBackground(Void... voids) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String ratesJsonStr = null;

        try {
            // Construct the URL for the Fixer.io query
            URL url = new URL(FIXER_URL);

            // Create the request and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // \n is not necessary but it makes debugging easier.
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

        // Call parser here to parse ratesJsonStr
        // then return the object created by it.

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... voids) {

    }

    @Override
    protected void onPostExecute(List<MoedaItem> result) {

    }
}
