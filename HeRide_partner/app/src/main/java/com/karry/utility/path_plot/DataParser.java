package com.karry.utility.path_plot;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.karry.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <h1>DataParser</h1>
 * used to decode the data for the route
 */
public class DataParser
{
    /** Receives a JSONObject and returns a list of lists containing latitude and longitude */
    public List<List<HashMap<String,String>>> parse(JSONObject jObject){
        List<List<HashMap<String, String>>> routes = new ArrayList<>() ;
        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;
        try
        {
            jRoutes = jObject.getJSONArray("routes");

            //Traversing all routes
            for(int i=0;i<jRoutes.length();i++){
                jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                List path = new ArrayList<>();

                // Traversing all legs
                for(int j=0;j<jLegs.length();j++){
                    jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");

                    // Traversing all steps
                    for(int k=0;k<jSteps.length();k++){
                        String polyline = "";
                        polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        // Traversing all points
                        for(int l=0;l<list.size();l++){
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("lat", Double.toString((list.get(l)).latitude) );
                            hm.put("lng", Double.toString((list.get(l)).longitude) );
                            path.add(hm);
                        }
                    }
                    routes.add(path);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return routes;
    }

    /** Receives a JSONObject and returns a latlong bounds*/
    LatLongBounds getBounding(JSONObject jObject){
        List<List<HashMap<String, String>>> routes = new ArrayList<>() ;
        JSONArray jRoutes;
        JSONObject jBounds;
        LatLongBounds latLngBounds = null;
        try
        {
            jRoutes = jObject.getJSONArray("routes");
            //Traversing all routes
            jBounds = ( (JSONObject)jRoutes.get(0)).getJSONObject("bounds");
            latLngBounds = new Gson().fromJson(jBounds.toString(),LatLongBounds.class);
            Utility.printLog(" inside the latlong bounds ");
            latLngBounds.setEstimatedDistanceTime(getDistanceDuration(jObject));

            return latLngBounds;
        } catch (JSONException e)
        {
            Utility.printLog(" inside the latlong bounds exception 1 "+e);

            e.printStackTrace();
        }catch (Exception e){
            Utility.printLog(" inside the latlong bounds exception 2 "+e);
            e.printStackTrace();
        }
        return latLngBounds;
    }


    private EstimatedDistanceTime getDistanceDuration(JSONObject jObject) {

        try {
            EstimatedDistanceTime estimatedDistanceTime = new EstimatedDistanceTime();
            String distance=jObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getString("text");
            String duration=jObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("duration").getString("text");
            estimatedDistanceTime.setDistance(distance);
            estimatedDistanceTime.setDuration(duration);
            return estimatedDistanceTime;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method to decode polyline points
     * Courtesy : https://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

}