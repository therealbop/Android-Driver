package com.karry.network;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.telephony.TelephonyManager;


import com.karry.utility.Utility;

import javax.inject.Inject;
import dagger.android.DaggerService;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;


/**
 * <h2>NetworkCheckerService</h2>
 * <P>
 *  Checking for the Internet is there or not.
 *  After each five seconds.
 * </P>
 * @author 3Embed.
 * @version 1.0.
 * */
public class NetworkCheckerService extends DaggerService
{
    private static final String TAG = "NetworkCheckerService";
    private ConnectivityManager cm;
    public  NetworkTimer mTimer = null;

    @Inject NetworkStateHolder holder;
    @Inject RxNetworkObserver rxNetworkObserver;
    @Inject NetworkService networkService;

    @Override
    public void onCreate()
    {
        super.onCreate();
        int NOTIFICATION_ID = (int) (System.currentTimeMillis()%10000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startForeground(NOTIFICATION_ID, new Notification.Builder(this).build());

        Utility.printLog(TAG+" SERVICE on onCreate ");
        cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        int interval = 10;

        new Thread(() ->
        {
            try
            {
                Utility.printLog(TAG+" SERVICE on try ");
                if(mTimer!=null)
                    mTimer.cancel();

                if(mTimer == null)
                {
                    Utility.printLog(TAG+" SERVICE on null ");
                    mTimer = NetworkTimer.getNetworkTimer();
                    mTimer.scheduleAtFixedRate(new CheckForConnection(),1000*interval);
                }
                else
                    Utility.printLog(TAG+" SERVICE on not null ");
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                Utility.printLog(TAG+" SERVICE on exception "+ex);
            }
        }).start();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent)
    {
        super.onTaskRemoved(rootIntent);
        Utility.printLog(TAG+" SERVICE on onTaskRemoved ");
        stopSelf();
    }

    @Override
    public void onDestroy()
    {
        Utility.printLog(TAG+" SERVICE on destroyed "+mTimer);
        if(mTimer!=null)
        {
            Utility.printLog(TAG+" SERVICE onDestroy on not null ");
            mTimer.cancel();
            mTimer = null;
        }
        super.onDestroy();
    }


    class CheckForConnection extends TimerChecker
    {
        @Override
        public void run()
        {
            checkInternetConnection();
        }
    }
    /*
     *Checking the internet connection is there or not. */
/*    private void checkInternetConnection()
    {
        try
        {
            URL url=new URL("https://console.firebase.google.com/");
            HttpURLConnection urlConnection= (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            urlConnection.setConnectTimeout(2000);
            int status=urlConnection.getResponseCode();
            Utility.printLog(TAG+" SERVICE status  "+status);
            if(status==HttpURLConnection.HTTP_OK)
            {
                Utility.printLog(TAG+" HTTP_OK ");
                holder.setConnected(true);
                getConnectionType();

            }
            else
            {
                Utility.printLog(TAG+" HTTP_OK ");
                holder.setConnected(false);
                getConnectionType();
            }
        } catch (Exception e)
        {
            Utility.printLog(" exception in timer stop "+e);
            holder.setConnected(false);
            getConnectionType();
        }
        *//*
         *providing the network status. *//*
        rxNetworkObserver.publishData(holder);
    }*/

    /**
     * <h2>driversDistanceMatrixETAAPI</h2>
     * This class is used to get the ETA from google
     */
    private void checkInternetConnection()
    {
        String url = "https://www.google.co.in/";
        Observable<Response<ResponseBody>> request = networkService.checkInternet(url);
        request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d)
                    {
                    }

                    @Override
                    public void onNext(Response<ResponseBody> result)
                    {
                        Utility.printLog(TAG+ " SERVICE network code onNext: " + result.code());
                        switch (result.code())
                        {
                            case 200:
                                holder.setConnected(true);
                                getConnectionType();
                                break;

                            default:
                                Utility.printLog(TAG+" HTTP_OK ");
                                holder.setConnected(false);
                                getConnectionType();
                                break;
                        }
                        rxNetworkObserver.publishData(holder);
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        Utility.printLog(TAG+ " SERVICE url error: " + e);
                        holder.setConnected(false);
                        getConnectionType();
                        rxNetworkObserver.publishData(holder);
                    }

                    @Override
                    public void onComplete()
                    {
                        Utility.printLog(TAG+ " SERVICE url onComplete: " );
                    }
                });
    }

    /*
     *Getting the connection type. */
    private void getConnectionType()
    {
        if(cm!=null)
        {
            NetworkInfo networkInfo= cm.getActiveNetworkInfo();
            if(networkInfo!=null&&networkInfo.isConnectedOrConnecting())
            {
                holder.setConnectionType(isConnectionType(networkInfo.getType()));
                holder.setMessage("Connected");
            }else
            {
                holder.setConnectionType(ConnectionType.NOT_CONNECTED);
                holder.setMessage("Not Connected");
            }
        }else
        {
            holder.setConnectionType(ConnectionType.NOT_CONNECTED);
            holder.setMessage("Not Connected");
        }
    }

    /*
     * Getting the connection type in android.*/
    private ConnectionType isConnectionType(int type)
    {
        if(type== ConnectivityManager.TYPE_WIFI)
        {
            return ConnectionType.WIFI;
        }else
        {
            return ConnectionType.MOBILE;
        }
    }

    /*
     * Get the connection is good or not.*/
    private boolean isConnectionGood(int type,int subType)
    {
        if(type==ConnectivityManager.TYPE_WIFI)
        {
            return true;
        }else if(type==ConnectivityManager.TYPE_MOBILE)
        {
            switch(subType)
            {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        }else{
            return false;
        }
    }
}

