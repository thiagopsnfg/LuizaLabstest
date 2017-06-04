package com.br.weather.view;


import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Toast;

import com.br.weather.R;
import com.br.weather.data.model.City;
import com.br.weather.data.repository.WeatherRepository;
import com.br.weather.utils.Internet;
import com.br.weather.utils.MapsUtils;
import com.br.weather.utils.WeatherUtils;
import com.br.weather.view.Adapter.WeatherMapAdapter;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class WeatherPresenter {

    private static final float DEFAULT_MIN_ZOOM = 2.0f;
    private static final float DEFAULT_MAX_ZOOM = 22.0f;

    private WeatherView mWeatherView;
    private WeatherRepository mWeatherRepository;
    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private LatLng mCurrentLocation;
    private List<City> mCities;
    private List<Marker> mMarkers = new ArrayList<>();
    private GoogleMap mGoogleMap;

    public WeatherPresenter(Context context, WeatherView weatherView, GoogleApiClient googleApiClient) {
        this.mWeatherView = weatherView;
        this.mWeatherRepository = new WeatherRepository(context);
        this.mContext = context;
        this.mGoogleApiClient = googleApiClient;
    }

    public void setLocation(Location location) {
        this.mCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());
    }

    public void configMaps(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        mGoogleMap.setInfoWindowAdapter(new WeatherMapAdapter(mContext));
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(false);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.setMaxZoomPreference(DEFAULT_MAX_ZOOM);
        mGoogleMap.setMinZoomPreference(DEFAULT_MIN_ZOOM);
        mGoogleMap.setTrafficEnabled(false);
        mGoogleMap.setIndoorEnabled(false);
        mGoogleMap.setBuildingsEnabled(false);

        mGoogleMap.setOnMapLoadedCallback(this::setMapBounds);
        mGoogleMap.setOnCameraIdleListener(this::nextFetchWeather);
        mGoogleMap.setOnMarkerClickListener(this::onMarkerClick);
    }


    public void switchTemperatureUnit(MenuItem item) {
        switch (WeatherUtils.currentTemperatureUnit) {
            case WeatherUtils.CELSIUS:
                item.setIcon(R.drawable.ic_fahrenheit);
                WeatherUtils.currentTemperatureUnit = WeatherUtils.FAHRENHEIT;
                mWeatherView.refreshList();
                addMarkersToMap();
                break;
            case WeatherUtils.FAHRENHEIT:
                item.setIcon(R.drawable.ic_celsius);
                WeatherUtils.currentTemperatureUnit = WeatherUtils.CELSIUS;
                mWeatherView.refreshList();
                addMarkersToMap();
                break;
        }
    }

    public void switchView(MenuItem item) {
        if (item.getIcon()
                .getConstantState()
                .equals(ContextCompat
                        .getDrawable(mContext, R.drawable.ic_map)
                        .getConstantState())) {

            if (!checkMapsIsReady()) return;

            item.setIcon(R.drawable.ic_list);
            mWeatherView.showList(false);
            return;
        }

        item.setIcon(R.drawable.ic_map);
        mWeatherView.showList(true);
    }

    public void fetchWeather() {

        if (mCurrentLocation != null) return;

        if (!setFirstLocation()) return;

        String bbox = WeatherUtils.getSearchArea(mCurrentLocation);

        if (!Internet.hasInternet(mContext)) {
            mWeatherView.showLoading(false);
            mWeatherView.showNoConnection(true);
        }

        mWeatherRepository.getWeathers(bbox, new WeatherRepository.Message() {
            @Override
            public void onSuccess(List<City> cities) {
                mGoogleMap.setMyLocationEnabled(false);

                mCities = MapsUtils.sortCitiesNearMe(cities, mCurrentLocation);

                addMarkersToMap();
                mWeatherView.setListAdapter(mCities);
                mWeatherView.showLoading(false);
                mWeatherView.showList(true);
            }

            @Override
            public void onError() {
                mWeatherView.showLoading(false);
                mWeatherView.showNoConnection(true);
            }
        });
    }

    public void nextFetchWeather() {
        if (!Internet.hasInternet(mContext)) {
            mWeatherView.showNoConnection(true);
        }

        mCurrentLocation = mGoogleMap.getCameraPosition().target;

        String bbox = WeatherUtils.getSearchArea(mCurrentLocation);

        mWeatherRepository.getWeathers(bbox, new WeatherRepository.Message() {
            @Override
            public void onSuccess(List<City> cities) {
                mCities = MapsUtils.sortCitiesNearMe(cities, mCurrentLocation);
                addMarkersToMap();
                mWeatherView.setListAdapter(mCities);
            }

            @Override
            public void onError() {

            }
        });

    }

    private void addMarkersToMap() {
        mGoogleMap.clear();
        mMarkers.clear();

        for (City city : mCities) {
            LatLng latLng = city.getCoord().getLatLng();

            Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(latLng));
            marker.setTag(city);
            mMarkers.add(marker);
        }
    }

    private void setMapBounds() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (City city : mCities) {
            LatLng latLng = city.getCoord().getLatLng();
            builder.include(latLng);
        }

        LatLngBounds bounds = builder.build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
    }

    private boolean checkMapsIsReady() {
        if (mGoogleMap == null || mCities == null) {
            Toast.makeText(mContext, mContext.getString(R.string.map_dont_read_warning), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean setFirstLocation() {
        if (!mWeatherView.isPermissionLocationGranted()) {
            mWeatherView.getPermissionLocation();
            return false;
        }

        if (mCurrentLocation == null) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location == null) {
                mWeatherView.createLocationRequest();
                return false;
            }
            mCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        }

        return true;
    }

    private boolean onMarkerClick(Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = Math.max(1 - interpolator.getInterpolation((float) elapsed / duration), 0);
                marker.setAnchor(0.5f, 1.0f + 2 * t);

                if (t > 0.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });

        float zIndex = marker.getZIndex() + 1.0f;
        marker.setZIndex(zIndex);

        marker.showInfoWindow();

        return true;
    }
}
