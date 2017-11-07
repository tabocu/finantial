package sistemas.puc.com.finantialapp.util;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonUtils {

    private static final String LOG_TAG = "JSON_UTILS";

    public static String getJsonStr(String URL) {

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
}
