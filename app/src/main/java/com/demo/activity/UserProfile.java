package com.demo.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.model.FirebaseUserDetail;
import com.demo.model.UserDetail;
import com.demo.utitlity.DataAttributes;
import com.demo.R;
import com.demo.utitlity.FirebaseConstant;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;

public class UserProfile extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    ImageView imageButton;
    String uid, name, gender = "", yearOfBirth, careOf, villageTehsil, postOffice, district, state, postCode;
    EditText editTextName, editTextEmail, editTextMobile, editTextDob, editTextAddress, editTextPinCode, editTextMedicalHistory;
    Button buttonSave;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userDetailRef = database.getReference(FirebaseConstant.USERDETIL);
    DatabaseReference testingNode = database.getReference("Test");
    RadioGroup radioGroup;
    Realm realm;
    boolean allFilled = true;
    int mYear, mMonth, mDay;

    //Location

    private static final String TAG = "MainActivity";
    private static final int REQUEST_LOCATION = 101;
    TextView tvSetLocation;
    Button btnGetLocation;

    //Location
    private boolean isLocationEnabled = false;
    private Location userLocation;
    private LocationManager locationManager;
    String currentAddress;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        realm = Realm.getDefaultInstance();
        imageButton = (ImageView) findViewById(R.id.imageViewScanner);
        editTextAddress = (EditText) findViewById(R.id.address);
        editTextName = (EditText) findViewById(R.id.name);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextMobile = (EditText) findViewById(R.id.mobileNumber);
        editTextDob = (EditText) findViewById(R.id.dob);
        editTextPinCode = (EditText) findViewById(R.id.pinCode);
        editTextMedicalHistory = (EditText) findViewById(R.id.medicalHistory);
        locationManager = (LocationManager) this.getSystemService(Service.LOCATION_SERVICE);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScanner();

            }
        });


        buttonSave = (Button) findViewById(R.id.btnSave);
        {
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allFilled = !editTextName.getText().toString().isEmpty();

                    allFilled = !editTextEmail.getText().toString().isEmpty();

                    if (editTextDob.getText().toString().isEmpty())
                        allFilled = false;
                    else if (editTextAddress.getText().toString().isEmpty())
                        allFilled = false;
                    else if (gender != null && gender.isEmpty())
                        allFilled = false;
                    else if (editTextMobile.getText().toString().isEmpty())
                        allFilled = false;
                    else if (editTextPinCode.getText().toString().isEmpty())
                        allFilled = false;
                    else if (editTextMedicalHistory.getText().toString().isEmpty())
                        allFilled = false;

                    if (allFilled)
                        saveOnFirebase();
                    else
                        Toast.makeText(UserProfile.this, "all field are mandotaory", Toast.LENGTH_SHORT).show();
                }
            });
        }
        String getkey = testingNode.push().getKey();
        testingNode.child(getkey).setValue(System.currentTimeMillis());
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.clearCheck();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    gender = rb.getText().toString();
                }

            }
        });
        editTextDob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(UserProfile.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar calendar=Calendar.getInstance();
                        calendar.set(selectedyear,selectedmonth,selectedday);
                        String myFormat = "MM/dd/yy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        editTextDob.setText(sdf.format(myCalendar.getTime()));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });
        initUI();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                location();

            }
        }, 1000);
    }

    void openScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan a Aadharcard QR Code");
        integrator.setResultDisplayDuration(500);
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            // process received data
            if (scanContent != null && !scanContent.isEmpty()) {
                processScannedData(scanContent);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Scan Cancelled", Toast.LENGTH_SHORT);
                toast.show();
            }

        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    protected void processScannedData(String scanData) {
        Log.e("Rajdeol", scanData);
        XmlPullParserFactory pullParserFactory;

        try {
            // init the parserfactory
            pullParserFactory = XmlPullParserFactory.newInstance();
            // get the parser
            XmlPullParser parser = pullParserFactory.newPullParser();

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(scanData));

            // parse the XML
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    Log.d("Rajdeol", "Start document");
                } else if (eventType == XmlPullParser.START_TAG && DataAttributes.AADHAAR_DATA_TAG.equals(parser.getName())) {
                    // extract data from tag
                    //uid
                    uid = parser.getAttributeValue(null, DataAttributes.AADHAR_UID_ATTR);
                    //name
                    name = parser.getAttributeValue(null, DataAttributes.AADHAR_NAME_ATTR);
                    //gender
                    gender = parser.getAttributeValue(null, DataAttributes.AADHAR_GENDER_ATTR);
                    // year of birth
                    yearOfBirth = parser.getAttributeValue(null, DataAttributes.AADHAR_YOB_ATTR);
                    // care of
                    careOf = parser.getAttributeValue(null, DataAttributes.AADHAR_CO_ATTR);
                    // village Tehsil
                    villageTehsil = parser.getAttributeValue(null, DataAttributes.AADHAR_VTC_ATTR);
                    // Post Office
                    postOffice = parser.getAttributeValue(null, DataAttributes.AADHAR_PO_ATTR);
                    // district
                    district = parser.getAttributeValue(null, DataAttributes.AADHAR_DIST_ATTR);
                    // state
                    state = parser.getAttributeValue(null, DataAttributes.AADHAR_STATE_ATTR);
                    // Post Code
                    postCode = parser.getAttributeValue(null, DataAttributes.AADHAR_PC_ATTR);

                } else if (eventType == XmlPullParser.END_TAG) {
                    Log.d("Rajdeol", "End tag " + parser.getName());

                } else if (eventType == XmlPullParser.TEXT) {
                    Log.d("Rajdeol", "Text " + parser.getText());

                }
                // update eventType
                eventType = parser.next();
            }

            // display the data on screen
            displayScannedData();
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

    }// EO function

    void displayScannedData() {
        Toast.makeText(this, name + "   and " + uid, Toast.LENGTH_SHORT).show();
        editTextName.setText(name);
        editTextDob.setText(yearOfBirth);
        editTextPinCode.setText(postOffice);
        editTextAddress.setText(careOf + "  " + villageTehsil + " \n " + postOffice + "  " + district + " " + state);
    }

    void saveOnFirebase() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            final String name = user.getDisplayName();
            final String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            final String uid = user.getUid();
            final FirebaseUserDetail userDetail = new FirebaseUserDetail();
            userDetail.setUserId(uid);
            if (!editTextName.getText().toString().isEmpty())
                userDetail.setUserName(editTextName.getText().toString());
            if (!editTextEmail.getText().toString().isEmpty())
                userDetail.setUserEmail(email);
            if (!editTextDob.getText().toString().isEmpty())
                userDetail.setUserDOB(editTextDob.getText().toString());
            if (!editTextAddress.getText().toString().isEmpty())
                userDetail.setUserAddress(editTextAddress.getText().toString());

            userDetail.setUserGender(gender);
            if (!editTextMobile.getText().toString().isEmpty())
                userDetail.setUserMobileNumber(editTextMobile.getText().toString());
            if (!editTextPinCode.getText().toString().isEmpty())
                userDetail.setUserPinCode(editTextPinCode.getText().toString());
            if (!editTextMedicalHistory.getText().toString().isEmpty())
                userDetail.setUserMedicalHistory(editTextMedicalHistory.getText().toString());

            Realm realm = Realm.getDefaultInstance();
            final UserDetail userDetail1 = new UserDetail();
            userDetail1.setUserId(user.getUid());
            userDetail1.setUserName(editTextName.getText().toString());
            userDetail1.setUserEmail(email);
            userDetail1.setUserDOB(editTextDob.getText().toString());
            userDetail1.setUserAddress(editTextAddress.getText().toString());

            userDetail1.setUserGender(gender);
            userDetail1.setUserMobileNumber(editTextMobile.getText().toString());
            userDetail1.setUserPinCode(editTextPinCode.getText().toString());
            userDetail1.setUserMedicalHistory(editTextMedicalHistory.getText().toString());


            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.delete(UserDetail.class);
                    realm.insertOrUpdate(userDetail1);
                    Toast.makeText(UserProfile.this, "RealmUpdated ", Toast.LENGTH_SHORT).show();
                }
            });
            Log.e("Hello **** ", realm.where(UserDetail.class).findFirst().getUserEmail());

            userDetailRef.child(uid).setValue(userDetail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(UserProfile.this, "User Detail Updated Success Fully", Toast.LENGTH_SHORT).show();
                }
            });

        }
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }

    Calendar myCalendar;

    void initUI() {

        try {
            editTextEmail.setEnabled(false);
            UserDetail userDetail = realm.where(UserDetail.class).findFirst();
            editTextEmail.setText(userDetail.getUserEmail());
            editTextName.setText(userDetail.getUserName());
            editTextMobile.setText(userDetail.getUserMobileNumber());
            editTextDob.setText(userDetail.getUserDOB());
            gender = userDetail.getUserGender();
            editTextAddress.setText(userDetail.getUserAddress());
            editTextPinCode.setText(userDetail.getUserPinCode());
            editTextMedicalHistory.setText(userDetail.getUserMedicalHistory());
            if (gender.equals("male"))
                radioGroup.check(R.id.radiomale);
            else
                radioGroup.check(R.id.radiofemale);
            myCalendar = Calendar.getInstance();




        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        editTextDob.setText(sdf.format(myCalendar.getTime()));
    }


    // Location

    void location() {

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
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, UserProfile.this);
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
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {
            initializeLocationVariables();
        } else {
            mGoogleApiClient.connect();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION: {
                //if permission denied permanentlygrantResults[i] == PackageManager.PERMISSION_DENIED
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    if (Build.VERSION.SDK_INT >= 23 && !shouldShowRequestPermissionRationale(permissions[0])) {
                        Toast.makeText(UserProfile.this, "Go to Settings and Grant the permission to use this feature.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
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
                Log.e(TAG, addresses.get(i).toString());
            }
            currentAddress = addresses.get(0).getAddressLine(0) + " " + addresses.get(0).getAddressLine(1) + addresses.get(0).getAddressLine(2) + addresses.get(0).getAddressLine(3);
//            tvSetLocation.setText(currentAddress.trim());

            editTextAddress.setText(currentAddress);
            editTextPinCode.setText(addresses.get(0).getPostalCode());

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
                        Toast.makeText(UserProfile.this, R.string.allow_location_permission, Toast.LENGTH_SHORT).show();

                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Toast.makeText(UserProfile.this, R.string.allow_location_permission, Toast.LENGTH_SHORT).show();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onLocationChanged(Location location) {

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
    public void onClick(View v) {

    }
}