package com.karry.utility.path_plot;

import com.google.android.gms.maps.model.LatLng;
import com.karry.utility.Utility;

/**
 * <h1>GetGoogleDirectionsUrl</h1>
 * used to get the directions url
 * @author 3Embed
 * @since on 02-04-2018.
 */
public class GetGoogleDirectionsUrl
{

    public static String getDirectionsUrl(LatLng origin, LatLng dest,String serverKey){

        try {
        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters+"&key="+ serverKey;
        Utility.printLog(" route url "+url);

        return url;
        }catch (Exception e){
            return null;
        }
    }
}
