package com.br.weather.view;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.br.weather.R;
import com.br.weather.data.model.City;
import com.br.weather.view.Adapter.WeatherAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeatherActivity extends AppCompatActivity implements WeatherView {

    //region props
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 208;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.list) RecyclerView listWeathers;
    @BindView(R.id.view_no_connection) View noConnection;
    @BindView(R.id.view_list) View listView;
    @BindView(R.id.view_map) View mapView;
    @BindView(R.id.view_loading) View loading;

    private WeatherPresenter mWeatherPresenter;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private WeatherAdapter mWeatherAdapter;
    private MenuItem mUnitMenuItem;
    private MenuItem mViewStateMenuItem;
    //endregion

    //region Life cycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        listWeathers.setLayoutManager(mLinearLayoutManager);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_LOW_POWER)
                .setInterval(100000)
                .setFastestInterval(50000);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        this.mWeatherPresenter = new WeatherPresenter(this, this, mGoogleApiClient);
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }
    //endregion

    //region Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        mUnitMenuItem = menu.findItem(R.id.ac_change_unit);
        mViewStateMenuItem = menu.findItem(R.id.ac_change_view);
        enableOptionMenu(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ac_change_unit:
                mWeatherPresenter.switchTemperatureUnit(item);
                break;
            case R.id.ac_change_view:
                mWeatherPresenter.switchView(item);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    @Override
    public void showLoading(boolean show) {
        loading.setVisibility(show ? View.VISIBLE : View.GONE);
        listView.setVisibility(View.GONE);
        mapView.setVisibility(View.GONE);

        enableOptionMenu(false);
    }

    @Override
    public void showList(boolean show) {
        listView.setVisibility(show ? View.VISIBLE : View.GONE);
        mapView.setVisibility(show ? View.GONE : View.VISIBLE);

        enableOptionMenu(true);
    }

    @Override
    public void showNoConnection(boolean show) {
        noConnection.setVisibility(show ? View.VISIBLE : View.GONE);

        listView.setVisibility(View.GONE);
        mapView.setVisibility(View.GONE);
        enableOptionMenu(false);
    }

    @Override
    public void enableOptionMenu(boolean enable) {
        mUnitMenuItem.setVisible(enable);
        mViewStateMenuItem.setVisible(enable);
    }

    @Override
    public void refreshList() {
        mWeatherAdapter.notifyDataSetChanged();
    }

    @Override
    public void setListAdapter(List<City> cities) {
        if (listWeathers != null) {
            mWeatherAdapter = new WeatherAdapter(cities);
            listWeathers.setAdapter(mWeatherAdapter);
        }
    }

    //region Permission
    @Override
    public void getPermissionLocation() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setTitle(R.string.permission_rationale_location_title);
            alertBuilder.setMessage(R.string.permission_rationale_location_message);
            alertBuilder.setCancelable(false);
            alertBuilder.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_sad_cloud));

            alertBuilder.setPositiveButton(R.string.confirm, (dialog, which) -> {
                requestPermission();
                dialog.dismiss();
            });

            alertBuilder.create().show();
        }

        requestPermission();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(WeatherActivity.this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION},
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }

    public void createLocationRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> pendingResult = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        pendingResult.setResultCallback(result -> {
            final Status status = result.getStatus();

            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {
                        status.startResolutionForResult(
                                WeatherActivity.this, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    } catch (IntentSender.SendIntentException e) {
                        showNoConnection(true);
                    }
                    break;
            }
        });
    }

    @Override
    public boolean isPermissionLocationGranted() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
    //endregion

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mWeatherPresenter.fetchWeather();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mWeatherPresenter.configMaps(googleMap);
    }

    @Override
    public void onLocationChanged(Location location) {
        mWeatherPresenter.setLocation(location);
        mWeatherPresenter.fetchWeather();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mWeatherPresenter.fetchWeather();
    }

    @OnClick(R.id.root_no_connection)
    public void tryReconnect() {
        showNoConnection(false);
        showLoading(true);
        mWeatherPresenter.fetchWeather();
    }
}
