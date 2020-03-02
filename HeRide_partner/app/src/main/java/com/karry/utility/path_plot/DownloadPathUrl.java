package com.karry.utility.path_plot;

import android.os.AsyncTask;
import android.util.Log;

import com.karry.data.source.local.PreferenceHelperDataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// Fetches data from url passed
public class DownloadPathUrl extends AsyncTask<String, Void, String>
{
    private int color;
    private RxRoutePathObserver rxRoutePathObserver;
    private PreferenceHelperDataSource preferenceHelperDataSource;
    private RotateKey rotateKey;

    public DownloadPathUrl(RxRoutePathObserver rxRoutePathObserver, int color,
                           PreferenceHelperDataSource preferenceHelperDataSource, RotateKey rotateKey)
    {
        this.color = color;
        this.rxRoutePathObserver = rxRoutePathObserver;
        this.rxRoutePathObserver = rxRoutePathObserver;
        this.preferenceHelperDataSource = preferenceHelperDataSource;
        this.rotateKey=rotateKey;
    }

    @Override
    protected String doInBackground(String... url)
    {
        // For storing data from web service
        String data = "";

        try {
            // Fetching the data from web service
            data = downloadUrl(url[0]);
            Log.d("Background Task data", data.toString());
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result)
    {
        super.onPostExecute(result);
        // Invokes the thread for parsing the JSON data
        ParserTask parserTask  = new ParserTask(rxRoutePathObserver,color,preferenceHelperDataSource,
                rotateKey);
        parserTask.execute(result);
    }

    /**
     * <h2>downloadUrl</h2>
     * used to download the url for paths
     * @param strUrl url to be connected
     * @return returns the data
     * @throws IOException exception
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        HttpURLConnection urlConnection;
        URL url = new URL(strUrl);

        // Creating an http connection to communicate with url
        urlConnection = (HttpURLConnection) url.openConnection();

        // Connecting to url
        urlConnection.connect();

        // Reading data from url
        try (InputStream iStream = urlConnection.getInputStream())
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data);
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            urlConnection.disconnect();
        }
        return data;
    }
}