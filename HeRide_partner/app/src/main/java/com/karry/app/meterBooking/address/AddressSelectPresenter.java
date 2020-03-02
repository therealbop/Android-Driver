package com.karry.app.meterBooking.address;

import static com.karry.utility.VariableConstant.RESPONSE_UNAUTHORIZED;

import android.content.Context;
import android.location.Address;
import com.karry.authentication.login.model.LanguagesList;
import com.karry.bookingFlow.model.AddressDataModel;
import com.karry.data.source.local.PreferenceHelperDataSource;
import com.karry.data.source.sqlite.SQLiteDataSource;
import com.karry.manager.location.RxAddressObserver;
import com.karry.network.NetworkService;
import com.karry.network.NetworkStateHolder;
import com.karry.network.RxNetworkObserver;
import com.karry.pojo.PlaceAutoCompletePojo;
import com.karry.pojo.bookingAssigned.BookingAssignedDataRideAppts;
import com.karry.service.LocationUpdateService;
import com.karry.utility.DataParser;
import com.karry.utility.Utility;
import com.karry.utility.VariableConstant;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Response;

/**
 *
 *
 * <h1>AddressSelectPresenter</h1>
 *
 * This class is used to call the API
 *
 * @author 3Embed
 * @since on 20-01-2018.
 */
public class AddressSelectPresenter implements AddressSelectContract.Presenter
{
    @Inject Context mContext;
    @Inject SQLiteDataSource addressDataSource;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject AddressSelectContract.View addressSelectView;
    @Inject PreferenceHelperDataSource preferencesHelper;
    @Inject RxAddressObserver rxAddressObserver;
    @Inject NetworkService networkService;
    @Inject CompositeDisposable compositeDisposable;
    @Inject RxNetworkObserver rxNetworkObserver;
    private boolean networkAvailable = true;
    private String TAG = "AddressSelectPresenter ";

    private ArrayList<PlaceAutoCompletePojo> mAddressList = new ArrayList<>();


    @Inject
    AddressSelectPresenter()
    {
    }

    /**********************************************************************************************/
    @Override
    public void setActionBar() {
        addressSelectView.initActionBar();
    }

    @Override
    public void setActionBarTitle() {
        addressSelectView.setTitle();
    }



    @Override
    public void toggleFavAddressField(boolean showFavAddressList) {
        if(showFavAddressList)
            addressSelectView.showFavAddressListUI();
        else {
            addressSelectView.hideFavAddressListUI();
        }
    }

    @Override
    public void subscribeNetworkObserver()
    {
        Observer<NetworkStateHolder> observer = new Observer<NetworkStateHolder>()
        {
            @Override
            public void onSubscribe(Disposable d)
            {
                compositeDisposable.add(d);
            }
            @Override
            public void onNext(NetworkStateHolder value)
            {
                Utility.printLog(""+" network not avalable "+value.isConnected());
                networkAvailable = networkStateHolder.isConnected();
                try {
                    if(value.isConnected())
                        addressSelectView.networkAvailable();
                    else
                        addressSelectView.networkNotAvailable();
                }catch (Exception e){
                    Utility.printLog(""+ Arrays.toString(e.getStackTrace()));
                }

            }
            @Override
            public void onError(Throwable e)
            {
                e.printStackTrace();
            }
            @Override
            public void onComplete()
            {}
        };
        rxNetworkObserver.subscribeOn(Schedulers.io());
        rxNetworkObserver.observeOn(AndroidSchedulers.mainThread());
        rxNetworkObserver.subscribe(observer);
    }

    @Override
    public void networkCheckOnresume() {
        if(networkAvailable)
            addressSelectView.networkAvailable();
        else
            addressSelectView.networkNotAvailable();
    }

    /**
     * <h2>rotateNextKey</h2>
     * used to rotate to next key
     */
    @Override
    public void rotateNextKey() {

        Utility.printLog(" ETA TEST Distance matrix key exceeded ");
        //if the stored key is exceeded then rotate to next key
        List<String> googleServerKeys=preferencesHelper.getGoogleServerKeys();
        if(googleServerKeys.size()>0)
        {
            Utility.printLog( " ETA TEST Distance matrix keys size before remove "+googleServerKeys.size());
            googleServerKeys.remove(0);
            if(googleServerKeys.size()>0)
            {
                Utility.printLog(" ETA TEST Distance matrix keys next key "+googleServerKeys.get(0));
                //store next key in shared pref
                /*preferencesHelper.setGoogleServerKey(googleServerKeys.get(0));*/
                //if the stored key is exceeded then rotate to next and call eta API
                addressSelectView.filterAddress();
            }
            //to store the google keys array by removing exceeded key from list
            preferencesHelper.setGoogleServerKeys(googleServerKeys);
        }
    }

    /**
     * <h2>getStoredValues</h2>
     * used to get the stored values
     */
    @Override
    public String getServerKey() {
        return preferencesHelper.getGoogleServerKey();
    }

    @Override
    public String getLat() {
        return String.valueOf(preferencesHelper.getCurrLatitude());
    }

    @Override
    public String getLng() {
        return String.valueOf(preferencesHelper.getCurrLongitude());
    }


    @Override
    public void clearComposite() {
        compositeDisposable.clear();
    }

    @Override
    public void getRecentAddress() {
        ArrayList<AddressDataModel> recentAddressList = addressDataSource.extractAllNonFavAddresses();
        if(recentAddressList!=null && recentAddressList.size()>0){
            mAddressList = new ArrayList<>();
            for(int i = recentAddressList.size()-1; i>=0; i--){

                PlaceAutoCompletePojo datModel=new PlaceAutoCompletePojo();
                datModel.setAddress(recentAddressList.get(i).getAddress());
                datModel.setPlaceId(recentAddressList.get(i).getPaceId());
                datModel.setLat(recentAddressList.get(i).getLat());
                datModel.setLng(recentAddressList.get(i).getLng());
                mAddressList.add(datModel);

                Utility.printLog("\nthe recent saved address : "+recentAddressList.get(i).getPaceId()+"\t"+recentAddressList.get(i).getAddress());

            }
            addressSelectView.addressFetchSuccess(mAddressList);
            addressSelectView.hideFavAddressListUI();

        }
    }

    @Override
    public void fidLatLng(PlaceAutoCompletePojo placeAutoCompletePojo, boolean toCallApi) {
        String url = null;
        try {
            if (toCallApi) {
                final String ref_key = String.valueOf(placeAutoCompletePojo.getRef_key());
                url = getPlaceDetailsUrl(ref_key);
            }

            String finalUrl = url;
            Thread thread =
                new Thread(
                    () -> {
                        AddressDataModel addressDataModel = new AddressDataModel();
                        if (toCallApi) {
                            addressDataModel = AddressSelectPresenter.this.getPlaceData(finalUrl);
                        } else {
                            addressDataModel.setLat(placeAutoCompletePojo.getLat());
                            addressDataModel.setLng(placeAutoCompletePojo.getLng());
                        }

                        if (addressDataModel != null) {
                            addressDataModel.setAddress(placeAutoCompletePojo.getAddress());
                            addressDataModel.setPaceId(placeAutoCompletePojo.getPlaceId());
                            Utility.printLog(
                                "addressDataModel : "
                                    + addressDataModel.getLat()
                                    + " , "
                                    + addressDataModel.getLng());
                            LatLng latLng =
                                new LatLng(
                                    Double.parseDouble(addressDataModel.getLat()),
                                    Double.parseDouble(addressDataModel.getLng()));
                            addressDataSource.insertNonFavAddressData(
                                addressDataModel.getAddress(),
                                addressDataModel.getLat(),
                                addressDataModel.getLng(),
                                addressDataModel.getPaceId());
                            ArrayList<AddressDataModel> recentAddressList =
                                addressDataSource.extractAllNonFavAddresses();
                            Utility.printLog("recent address is : " + recentAddressList.get(0).getAddress());
                            startMeterBooking(
                                placeAutoCompletePojo.getAddress(),
                                String.valueOf(latLng),
                                placeAutoCompletePojo.getPlaceId());
                        }
                    });
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <h2>AddressDataModel</h2>
     * <p>
     * This method is providing the LAT-LONG based on the URL, we got from getPlaceDetailsUrl().
     * </p>
     * @param inputURL represent a URL.
     * @return returns AddressDataModel object based on the filter
     */
    private AddressDataModel getPlaceData(String inputURL)
    {
        AddressDataModel addressDataModel = new AddressDataModel();
        Utility.printLog("value of url: activity: "+inputURL);
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            URL url = new URL(inputURL);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
            Utility.printLog(TAG+" ,address:11: "+jsonResults);
            Gson gson = new Gson();
            DropLocationGoogleModel dropLocationGoogleModel = gson.fromJson(jsonResults.toString(),
                    DropLocationGoogleModel.class);
            if(dropLocationGoogleModel.getStatus().equals("OK"))
            {
                String lat = dropLocationGoogleModel.getResult().getGeometry().getLocation().getLat();
                String lng = dropLocationGoogleModel.getResult().getGeometry().getLocation().getLng();
                addressDataModel.setLat(lat);
                addressDataModel.setLng(lng);
                return addressDataModel;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Utility.printLog(TAG+ " Error API "+ e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }


    /**
     * <h>getPlaceDetailsUrl</h>
     * <p>
     * Creating the google API request list for getting the lat-long based on our requested address.
     * </p>
     */
    private String getPlaceDetailsUrl(String ref)
    {
        String key = "key="+preferencesHelper.getGoogleServerKey();
        String reference = "reference="+ref;                // reference of place
        String sensor = "sensor=false";                     // Sensor enabled
        String parameters = reference+"&"+sensor+"&"+key;   // Building the parameters to the web service
        String output = "json";                             // Output format
        return "https://maps.googleapis.com/maps/api/place/details/"+output+"?"+parameters;
    }

    /**********************************************************************************************/
    private void startMeterBooking(String dropAddress, String dropLatLng, String dropPlaceID) {
        fetchAddress(dropAddress, dropLatLng, dropPlaceID);
    }

  private void fetchAddress(String dropAddress, String dropLatLng, String dropPlaceID) {
    Utility.printLog(
        "current latlng is : "
            + preferencesHelper.getCurrLatitude()
            + ", "
            + preferencesHelper.getCurrLongitude());
    try {
      List<Address> addresses = new Utility().getPickupAddress(mContext,preferencesHelper);
      Utility.printLog("current latlng is : " + addresses.size());
      startMeterBookingAPI(addresses.get(0), dropAddress, dropLatLng, dropPlaceID);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

    /**
     * <h1>startMeterBookingAPI</h1>
     * <p>start the meterBooking</p>
     * @param PickUpAddress
     * @param dropAddress
     * @param dropLatLng
     * @param dropPlaceID
     */
    private void startMeterBookingAPI(Address PickUpAddress, String dropAddress, String dropLatLng, String dropPlaceID){

        Utility.printLog("lat lng is :  "+String.valueOf(preferencesHelper.getCurrLatitude())+","+
                String.valueOf(preferencesHelper.getCurrLongitude()));

        dropLatLng = dropLatLng.substring(10,dropLatLng.length()-1);
        String[] dropLatLong =  dropLatLng.split(",");

        Utility.printLog("the bundle values are : "+dropLatLong[0]);
        Utility.printLog("the bundle values are : "+dropLatLong[1]);

        String add;
        if(PickUpAddress!=null)
        {
            add = DataParser.getStringAddress(PickUpAddress);
        }
        else {
            add = "3embed soft private ltd.";
        }


        Observable<Response<ResponseBody>> startMeterBooking = networkService.startMeterBooking
                (preferencesHelper.getSessionToken(),
                        preferencesHelper.getLanguage(),
                        Utility.date(),
                        null,
                        null,
                        add,
                        null,
                        null,
                        null,
                        null,
                        null,
                        String.valueOf(preferencesHelper.getCurrLongitude()),
                        String.valueOf(preferencesHelper.getCurrLatitude()),
                        dropPlaceID,
                        null,
                        dropAddress,
                        null,
                        null,
                        null,
                        null,
                        null,
                        dropLatLong[0],
                        dropLatLong[1]);
        startMeterBooking.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {



                        Utility.printLog("startMeterBooking response is  : "+value);
                        try {
                            switch (value.code()) {
                                case VariableConstant.RESPONSE_CODE_SUCCESS:
                                    String resp=value.body().string();
                                    Utility.printLog("startMeterBooking success response is  : \n"+resp);

                                    JSONObject jsonObject = new JSONObject(resp);
                                    Gson gson = new Gson();
                                    BookingAssignedDataRideAppts bookingAssignedDataRideAppts = gson.fromJson(jsonObject.getJSONObject("data").toString(), BookingAssignedDataRideAppts.class);
                                    addressSelectView.meterBookingSuccess(bookingAssignedDataRideAppts);
                                    Utility.printLog("the appoinment count is : "+ bookingAssignedDataRideAppts.getBookingId());

                                    if(bookingAssignedDataRideAppts.getBookingStatus()!=null){
                                        preferencesHelper.setBookingStatus(bookingAssignedDataRideAppts.getBookingStatus());
                                    }

                                    if(!preferencesHelper.getBookingId().matches(bookingAssignedDataRideAppts.getBookingId())){

                                        LocationUpdateService.getLocationUpdateService().setCumulativeDistance( 0.0,0.0,0.0);
                                        preferencesHelper.setDistanceCalculated("0.0");
                                        preferencesHelper.setDistanceCalculatedShow("0.0");
                                        preferencesHelper.setBookingId(bookingAssignedDataRideAppts.getBookingId());
                                    }

                                    break;

                                case RESPONSE_UNAUTHORIZED:
                                    VariableConstant.FCM_TOPIS = preferencesHelper.getFcmTopic();
                                    VariableConstant.MQTT_TOPICS = preferencesHelper.getMqttTopic();
                                    LanguagesList languagesList = preferencesHelper.getLanguageSettings();
                                    preferencesHelper.clearSharedPredf();
                                    preferencesHelper.setLanguageSettings(languagesList);
                                    addressSelectView.goToLogin(DataParser.fetchErrorMessage(value));
                                    break;

                                default:
                                    Utility.BlueToast(mContext,DataParser.fetchErrorMessage(value));
                                    break;
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        addressSelectView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        addressSelectView.hideProgress();
                    }
                });
    }


}
