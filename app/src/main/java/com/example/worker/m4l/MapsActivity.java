package com.example.worker.m4l;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private TextView textViewDate;
    private TextView textViewMove1;
    private TextView textViewMove2;
    private TextView textViewActive1;
    private TextView textViewActive2;
    private Button buttonAdd;

    private static final String TAG = MapsActivity.class.getSimpleName();


    private GoogleMap mMap;
    private View mapView;
    private CameraPosition mCameraPosition;

    // The entry point to the Fused Location Provider.
//    private FusedLocationProviderClient mFusedLocationProviderClient;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    DBHelper mydb;
    private UtilsPreference pref;

    private String m_Text = "";
    private Calendar selectedCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        textViewDate = findViewById(R.id.textViewDate);
        textViewMove1 = findViewById(R.id.textMove1);
        textViewMove2 = findViewById(R.id.textMove2);
        textViewActive1 = findViewById(R.id.textActive1);
        textViewActive2 = findViewById(R.id.textActive2);
        buttonAdd = findViewById(R.id.buttonAdd);

        findViewById(R.id.buttonAdd).setOnClickListener(this);
        findViewById(R.id.buttonSetting).setOnClickListener(this);
        findViewById(R.id.textActive1).setOnClickListener( this);
        findViewById(R.id.textActive2).setOnClickListener( this);
        findViewById(R.id.textMove1).setOnClickListener( this);
        findViewById(R.id.textMove2).setOnClickListener( this);
//        // Construct a FusedLocationProviderClient.
//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapView = mapFragment.getView();


        pref = new UtilsPreference(this);
        mydb = new DBHelper(this);
        if(mydb.historyNumberOfRows() < 1)
            createRandomData();

        selectedCalendar = Calendar.getInstance();
        updateData();
    }

    private void createRandomData(){
        showToast("create Random Histories!");

        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < 100; i++){
            float mul = 100;

            int val1 = (int) ((Math.random() * mul) + mul / 3);
            int val2 = (int) ((Math.random() * mul) + mul / 3);
            int val3 = (int) ((Math.random() * mul) + mul / 3);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String date = format.format(cal.getTime());

            ModelHistory model = new ModelHistory(0, val1, val2, val3, date);
            mydb.historyInsert(model);
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        // Add polylines and polygons to the map. This section shows just
        // a single polyline. Read the rest of the tutorial to learn more.
        Polyline polyline1 = mMap.addPolyline(new PolylineOptions()
                .clickable(false)
                .add(
                        new LatLng(-35.016, 143.321),
                        new LatLng(-34.747, 145.592),
                        new LatLng(-34.364, 147.891),
                        new LatLng(-33.501, 150.217),
                        new LatLng(-32.306, 149.248),
                        new LatLng(-32.491, 147.309))
                .color(getResources().getColor(R.color.colorPrimary)));

        // Position the map's camera near Alice Springs in the center of Australia,
        // and set the zoom factor so most of Australia shows on the screen.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-32.306, 149.248), 5));

        // Location Button to Bottom right.
        View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);rlp.setMargins(0,0,30,30);

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
//        updateLocationUI();

        // Get the current location of the device and set the position of the map.
//        getDeviceLocation();
    }


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            updateLocationUI();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    updateLocationUI();
                }
            }
        }
    }
//    /**
//     * Gets the current location of the device, and positions the map's camera.
//     */
//    private void getDeviceLocation() {
//        /*
//         * Get the best and most recent location of the device, which may be null in rare
//         * cases when a location is not available.
//         */
//        try {
//            if (mLocationPermissionGranted) {
//                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
//                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Location> task) {
//                        if (task.isSuccessful()) {
//                            // Set the map's camera position to the current location of the device.
//                            mLastKnownLocation = task.getResult();
//                            if(mLastKnownLocation != null){
//                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                                        new LatLng(mLastKnownLocation.getLatitude(),
//                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
//                            }
//                            else{
//                                Toast.makeText(MapsActivity.this, "Location is null!", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Log.d(TAG, "Current location is null. Using defaults.");
//                            Log.e(TAG, "Exception: %s", task.getException());
//                            mMap.moveCamera(CameraUpdateFactory
//                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
//                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
//                        }
//                    }
//                });
//            }
//        } catch (SecurityException e)  {
//            Log.e("Exception: %s", e.getMessage());
//        }
//    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                showToast("Locatoin Enabled!");
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
//                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void gotoChartActivity(){
        Intent i = new Intent(MapsActivity.this, ChartActivity.class);
        startActivityForResult(i, 1);
    }

    private void addMinutes(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add active minutes!");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                addMinuteToDB();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void checkTodayDB(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String str1 = format.format(cal.getTime());
        ModelHistory history = mydb.historyGetByDate(str1);
        if(history.getId() == 0) {
            history.setDate(str1);
            mydb.historyInsert(history);
        }
    }

    private void addMinuteToDB(){
        checkTodayDB();
        int n = Integer.parseInt(m_Text);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String str1 = format.format(cal.getTime());
        ModelHistory history = mydb.historyGetByDate(str1);
        int manual = history.getManual();
        history.setManual(manual + n);
        mydb.historyUpdate(history);
        updateData();
    }

    private void showToast(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.textActive1){
            gotoChartActivity();
        }
        else if(v.getId() == R.id.textMove1){
            gotoChartActivity();
        }
        else if(v.getId() == R.id.textActive2){
            gotoChartActivity();
        }
        else if(v.getId() == R.id.textMove2){
            gotoChartActivity();
        }
        else if(v.getId() == R.id.buttonAdd){
            addMinutes();
        }
        else if(v.getId() == R.id.buttonSetting){
            if(UtilsCalendar.isToday(selectedCalendar) == 0){
                Intent i = new Intent(MapsActivity.this, SettingActivity.class);
                startActivityForResult(i, 2);
            }
            else {
                showToast("Today");
                Calendar cal = Calendar.getInstance();
                selectedCalendar = cal;
                updateData();
            }
        }
    }

    private void updateData(){
        checkTodayDB();

        if(UtilsCalendar.isToday(selectedCalendar) == 0){
            buttonAdd.setVisibility(View.VISIBLE);
        }
        else
            buttonAdd.setVisibility(View.INVISIBLE);

        int yy = selectedCalendar.get(Calendar.YEAR);
        int mm = selectedCalendar.get(Calendar.MONTH);
        int dd = selectedCalendar.get(Calendar.DAY_OF_MONTH);
        int ww = selectedCalendar.get(Calendar.DAY_OF_WEEK);
        if(ww > 0) ww--;

        String str = Utils.nameOfWeekday[ww] + String.format(" %02d ", dd) + Utils.nameOfMonth[mm];
        textViewDate.setText(str);


//        selectedCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String str1 = format.format(selectedCalendar.getTime());

        selectedCalendar.add(Calendar.DAY_OF_MONTH, -7);
        String str2 = format.format(selectedCalendar.getTime());
        selectedCalendar.set(yy, mm, dd);
        ArrayList<ModelHistory> histories = new ArrayList<ModelHistory>();

        histories = mydb.historyGetAll(str2, str1);

        int move = 0;
        int manual= 0;
        int active = 0;

        int sumOfMove = 0;
        int sumOfManual= 0;
        int sumOfActive = 0;

        String setMove = pref.getSettingMove();
        int setM = Integer.parseInt(setMove);
        String setActive = pref.getSettingActive();
        int setA = Integer.parseInt(setActive);

        int daysOfMove = 0;
        int daysOfActive = 0;

        for (int i = 0; i < 7; i++){
            if(i >= histories.size()) {
                continue;
            }

            ModelHistory h = histories.get(i);
            if(i == 0){
                move += h.getMove();
                manual += h.getManual();
                active += h.getActive();
            }
            sumOfMove += h.getMove();
            sumOfManual += h.getManual();
            sumOfActive += h.getActive();

            if(h.getMove() + h.getManual() + h.getActive() > setM)
                daysOfMove++;
            if(h.getActive() + h.getManual() > setA)
                daysOfActive++;
        }

        str = String.valueOf(sumOfMove + sumOfManual + sumOfActive) + " / " + String.valueOf(daysOfMove);
        textViewMove2.setText(str);
        str = String.valueOf(sumOfActive + sumOfActive) + " / " + String.valueOf(daysOfActive);
        textViewActive2.setText(str);


        str = String.valueOf(move + manual + active) + "  / " + String.valueOf(setM);
        textViewMove1.setText(str);
        str = String.valueOf(active + manual) + " / " + String.valueOf(setA);
        textViewActive1.setText(str);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                int yy = data.getIntExtra("year", 0);
                int mm = data.getIntExtra("month", 0);
                int dd = data.getIntExtra("day", 1);

//                Calendar cal = Calendar.getInstance();
                selectedCalendar.set(yy, mm, dd);
                updateData();
            }
        }

        if (requestCode == 2) { // setting Activity
            if(resultCode == Activity.RESULT_OK){
                updateData();
            }
        }
    }
}
