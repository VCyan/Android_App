package example.gabo.com.testapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
//import android.location.LocationListener;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationListener;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    public static final int PERMISSION_REQUEST_LOCATION_CODE =99;
    private GoogleMap mMap;
    private int proximityRadius = 10000;
    private Location lastLocation;
    private GoogleApiClient client;
    private LocationRequest locationReq;
    private Marker currentLocation;
    private double lat, longi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case PERMISSION_REQUEST_LOCATION_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        if(client == null){
                            buildGoogleAPIClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else{
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
                }
                return ;
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            buildGoogleAPIClient();
            mMap.setMyLocationEnabled(true);
            //return;
        }

    }

    public void searchPlace(View view){
        if(view.getId() == R.id.search_button){
            EditText tf_location = (EditText)findViewById(R.id.tf_location);
            String location = tf_location.getText().toString();
            List<Address> addressList = null;
            MarkerOptions markops = new MarkerOptions();
            if(!location.equals("")){
                Log.d("Maps", "String entered: "+location);
                Geocoder geocoder = new Geocoder(this);
                try {
                    addressList = geocoder.getFromLocationName(location, 5);
                    /*while (addressList.size()==0) {
                        addressList = geocoder.getFromLocationName(location, 5);
                    }*/
                    Log.d("Maps", "Found places:"+addressList.size());
                    if(addressList != null) {
                        for (int i = 0; i < addressList.size(); i++) {
                            Address myAddress = addressList.get(i);
                            LatLng mylat = new LatLng(myAddress.getLatitude(), myAddress.getLongitude());
                            markops.position(mylat);
                            markops.title(myAddress.getFeatureName());
                            markops.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            mMap.addMarker(markops);
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(mylat));
                        }
                    }
                    else{
                        Toast.makeText(this, "Location not found...", Toast.LENGTH_SHORT);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
        else if(view.getId() == R.id.movies_button){
            mMap.clear();
            String movies = "movie_theater";
            Object transferData[] = new Object[2];
            NearbyPlaces nearbyPlaces = new NearbyPlaces();

            String url = getUrl(lat,longi,movies);

            transferData[0] = mMap;
            transferData[1] = url;
            nearbyPlaces.execute(transferData);
            Toast.makeText(this, "Searching for cinemas...",Toast.LENGTH_SHORT);
            Toast.makeText(this, "Showing nearby cinemas...",Toast.LENGTH_SHORT);

        }

    }

    private String getUrl(double lat, double longi,String movies){
        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleURL.append("location="+lat +","+longi);
        googleURL.append("&radius="+proximityRadius);
        googleURL.append("&type="+movies);
        googleURL.append("&sensor=true");
        googleURL.append("&key=AIzaSyBJ6x3TZ9_a5wASzOA1Dca7q979sQMXqLQ");

       Log.d("Maps","URL="+ googleURL.toString());

        return googleURL.toString();

    }

    protected synchronized void buildGoogleAPIClient(){
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        client.connect();
    }

    public boolean checkLocationPermission(){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION_CODE);
            }
            else{
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION_CODE);
            }
            return false;
        }
        return true;
    }



    @Override
    public void onLocationChanged(Location location) {

        lat = location.getLatitude();
        longi = location.getLongitude();

        lastLocation = location;
        if(currentLocation != null){
            currentLocation.remove();
        }

        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions  markoptions = new MarkerOptions();
        markoptions.position(latlng);
        markoptions.title("Current Location");
        markoptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentLocation = mMap.addMarker(markoptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

        if (client != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
        }
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationReq = new LocationRequest();
        locationReq.setInterval(1000);
        locationReq.setFastestInterval(1000);
        locationReq.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            LocationServices.FusedLocationApi.requestLocationUpdates(client,locationReq,this);


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
