package com.demo.activity;

import android.Manifest;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.demo.R;
import com.demo.utitlity.Pref;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;

public class Home extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    ImageView imageViewProfile,imageViewHistory,imageViewCreateTest;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("userDetail");
    Realm realm;
    private static final String TAG = "MainActivity";
    private static final int REQUEST_LOCATION = 101;
    private boolean isLocationEnabled = false;
    private Location userLocation;
    private LocationManager locationManager;
    String currentAddress;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        realm=Realm.getDefaultInstance();
        imageViewProfile= (ImageView) findViewById(R.id.profile);
        imageViewHistory= (ImageView) findViewById(R.id.lastTest);
        imageViewCreateTest= (ImageView) findViewById(R.id.newTest);
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home.this,UserProfile.class);
                startActivity(intent);
                finish();
            }
        });
        imageViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home.this,HistoryScreen.class);
                startActivity(intent);
            }
        });
        imageViewCreateTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home.this,CreateTestScreen.class);
                startActivity(intent);
            }
        });

        initLocation();
    }

    private void initLocation() {
        //runtime permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
            if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                userLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                if (userLocation == null) {
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, Home.this);
                }
                if (userLocation != null) {
                    getLocation(userLocation);
                } else {
                    buildAlertMessageNoGps();
                }
            } else buildAlertMessageNoGps();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION: {
                //if permission denied permanentlygrantResults[i] == PackageManager.PERMISSION_DENIED
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    if (Build.VERSION.SDK_INT >= 23 && !shouldShowRequestPermissionRationale(permissions[0])) {
                        Toast.makeText(Home.this, "Go to Settings and Grant the permission to use this feature.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
    private void initializeLocationVariables() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(1000)
                    .setFastestInterval(1000);

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApiIfAvailable(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {
            initializeLocationVariables();
        } else {
            mGoogleApiClient.connect();
        }
    }

    private void getLocation(Location userLocation) {
        double latitude, longitude;
        latitude = userLocation.getLatitude();
        longitude = userLocation.getLongitude();
        Log.e(TAG, String.valueOf(longitude));
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            for (int i = 0; i < addresses.size(); i++) {
                Log.e(TAG,addresses.get(i).toString());
            }
            currentAddress = addresses.get(0).getAddressLine(0)+" "+addresses.get(0).getLocality();
           Log.e("Location is called",currentAddress);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.message_location_disabled_title))
                .setMessage(getString(R.string.message_location_disabled_message))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        Toast.makeText(Home.this, R.string.allow_location_permission, Toast.LENGTH_SHORT).show();

                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Toast.makeText(Home.this, R.string.allow_location_permission, Toast.LENGTH_SHORT).show();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_signOut:
                Toast.makeText(this, "Sign Out", Toast.LENGTH_SHORT).show();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.deleteAll();
                    }
                });

                Pref.setLogin(this,false);
                startActivity(new Intent(this,MainActivity.class));
                finish();
                // Red item was selected
                return true;
            case R.id.menu_admin:
                // Green item was selected
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
