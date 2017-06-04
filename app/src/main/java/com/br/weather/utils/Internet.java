package com.br.weather.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class Internet {

    public static boolean hasInternet(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity == null) return Boolean.FALSE;

        NetworkInfo netInfo = connectivity.getActiveNetworkInfo();

        if (netInfo == null) return Boolean.FALSE;

        int netType = netInfo.getType();

        if (netType == ConnectivityManager.TYPE_WIFI || netType == ConnectivityManager.TYPE_MOBILE || netType == ConnectivityManager.TYPE_WIMAX) {
            return netInfo.isConnected();
        } else {
            return Boolean.FALSE;
        }
    }
}
