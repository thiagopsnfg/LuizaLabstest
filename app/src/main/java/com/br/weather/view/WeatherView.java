package com.br.weather.view;

import android.support.v4.app.ActivityCompat;

import com.br.weather.data.model.City;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.List;

public interface WeatherView extends OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        ActivityCompat.OnRequestPermissionsResultCallback,
        LocationListener {

    void showLoading(boolean show);

    void showList(boolean show);

    void showNoConnection(boolean show);

    void enableOptionMenu(boolean enable);

    void refreshList();

    void setListAdapter(List<City> cities);

    void getPermissionLocation();

    void createLocationRequest();

    boolean isPermissionLocationGranted();
}
