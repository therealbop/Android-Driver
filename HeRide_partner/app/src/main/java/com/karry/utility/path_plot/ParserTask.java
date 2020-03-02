package com.karry.utility.path_plot;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.utility.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <h1>ParserTask</h1>
 * parse the Json data returned by
 */
public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>>
{
    private static final String TAG = "ParserTask";
    private RxRoutePathObserver rxRoutePathObserver;
    private int color ;
    private RotateKey rotateKey ;
    private LatLongBounds latLngBounds;
    private String status ;
    private PreferenceHelperDataSource preferenceHelperDataSource;

    ParserTask(RxRoutePathObserver rxRoutePathObserver, int color,
               PreferenceHelperDataSource preferenceHelperDataSource, RotateKey rotateKey)
    {
        this.color = color;
        this.rotateKey = rotateKey;
        this.rxRoutePathObserver = rxRoutePathObserver;
        this.preferenceHelperDataSource = preferenceHelperDataSource;
    }

    // Parsing the data in non-ui thread
    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData)
    {
        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;
        try
        {
            jObject = new JSONObject(jsonData[0]);
            Utility.printLog(TAG+" path plot response out "+ jsonData[0]);
            status = jObject.getString("status");
            if(status.equals("OK"))
            {
                DataParser parser = new DataParser();
                Utility.printLog(TAG+" path plot response "+ parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                latLngBounds = parser.getBounding(jObject);
                Utility.printLog(TAG+" Executing routes "+latLngBounds.toString());
                Utility.printLog(TAG+" Executing routes "+routes.toString());
            }
        } catch (Exception e)
        {
            Utility.printLog(TAG+ e.toString());
            e.printStackTrace();
        }
        return routes;
    }

    // Executes in UI thread, after the parsing process
    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        ArrayList<LatLng> points;
        PolylineOptions lineOptions = null;

        if(status!=null)
        {
            if(!status.equals("OK"))
            {
                Utility.printLog(TAG+" PATH PLOT TEST key exceeded ");
                //if the stored key is exceeded then rotate to next key
                List<String> googleServerKeys=preferenceHelperDataSource.getGoogleServerKeys();
                if(googleServerKeys.size()>0)
                {
                    Utility.printLog(TAG+ " PATH PLOT TEST TEST keys size before remove "+googleServerKeys.size());
                    googleServerKeys.remove(0);
                    if(googleServerKeys.size()>0)
                    {
                        Utility.printLog(TAG+" PATH PLOT TEST keys next key "+googleServerKeys.get(0));
                        //store next key in shared pref
                        preferenceHelperDataSource.setGoogleServerKey(googleServerKeys.get(0));
                        Utility.printLog(TAG+" PATH PLOT TEST set key "+preferenceHelperDataSource.getGoogleServerKey());
                        //if the stored key is exceeded then rotate to next and call eta API
                        rotateKey.rotateKey();
                    }
                    //to store the google keys array by removing exceeded key from list
                    preferenceHelperDataSource.setGoogleServerKeys(googleServerKeys);
                }
            }
        }

        if(result!=null)
        {
            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(color);

                latLngBounds.setPolylineOptions(lineOptions);
            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null)
                rxRoutePathObserver.notifyRoutePath(latLngBounds);
            else
                Utility.printLog(TAG+" without Polylines drawn");
        }
    }
}