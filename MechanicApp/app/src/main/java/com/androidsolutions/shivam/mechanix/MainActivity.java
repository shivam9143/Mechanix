package com.androidsolutions.shivam.mechanix;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationProvider.LocationCallback {

    TextView name, uid;
    SessionManager s;
    String name1, uid1;
    //Button loc;
    GPSTracker gps;
    Handler d=new Handler();
    Timer t=new Timer();
    private static final int FINE_LOCATION_PERMISSION_CONSTANT = 99;
    static public final int REQUEST_LOCATION = 1;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    private static final String TAG = "MainActivity";
    double latitude = 0, longitude = 0;
    private static final String[] LOCATION_AND_CONTACTS =
            {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_CONTACTS};
    LocationManager locationManager;
    private String provider;
    private LocationProvider mLocationProvider;
    private LocationListener listener;
     Intent intent=null;
    private static final int RC_CAMERA_PERM = 123;
    private static final int RC_LOCATION_CONTACTS_PERM = 124;
     String st="";
    static List<Contact> contacts;
   // TextView ln;
    JSONArray ja,jb;
    JSONObject job1,job2;
  //  Thread t1,t2;
    msg t2=new msg();
    runn t1=new runn();
    class runn extends Thread
    {
        public void run()
        {
            try {
                t2.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ConnectionDetector cd=new ConnectionDetector(MainActivity.this);
            if(cd.isConnectingToInternet())
            {
                Log.e("Current eaddd",Thread.currentThread().getName());
                StringRequest strReq = new StringRequest(Request.Method.POST,
                        Config.URL_VERIFY_OTP, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            JSONArray ar = new JSONArray(response);
                            JSONObject responseObj = ar.getJSONObject(0);

                            // Parsing json object response
                            // response will be a json object
                            int stat = responseObj.getInt("status");
                            boolean error = responseObj.getBoolean("error");
                            String message = responseObj.getString("message");

                            if (!error && stat == 0) {
                                Log.e("message", message + "kimjonh");
                                // parsing the user profile information
                                // JSONObject profileObj = responseObj.getJSONObject("profile");

                                s.setPreferences(MainActivity.this, "ContactsStatus", "1");
                                //   Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                            } else {
                                // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {/*
                Toast.makeText(getApplicationContext(),
                        "Error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();*/
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.getMessage());
            /*Toast.makeText(getApplicationContext(),
                    error.getMessage(), Toast.LENGTH_SHORT).show();
       */
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("contacts", st);
                        params.put("uid", uid1);
                        Log.e(TAG, "Posting params: " + params.toString());
                        return params;
                    }
                };
                // Adding request to request queue
                MyApplication.getInstance().addToRequestQueue(strReq);
            }

        }
    }


    class msg extends Thread
    {
        public void run()
        {
            List<Contact> contactsList = contacts;
            Log.e("LISTTTT SIZEEEE",contactsList.size()+"");
            int BATCH_SIZE = 40;
            if (contactsList != null && contactsList.size() > 0) {
                // Batching contact push
                for (int i = 0; i < (contactsList.size() / BATCH_SIZE) + 1; i++) {
                    List<Contact> subList = null;
                    if ((i + 1) * BATCH_SIZE > contactsList.size()) {
                        subList = contactsList.subList(i * BATCH_SIZE, contactsList.size());
                    } else {
                        subList = contactsList.subList(i * BATCH_SIZE, (i + 1) * BATCH_SIZE);
                    }
                    ja = new JSONArray();
                    Log.e("Current eaddd",Thread.currentThread().getName()+"i="+(i*BATCH_SIZE)+",to"+((i+1)*BATCH_SIZE));

                    for (Contact c : subList) {
                        job1 = new JSONObject();
                        try {
                            job1.put("Name", c.getName());
                            job1.put("Number", c.getNumber());
                            ja.put(job1);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                    try {
                        job2 = new JSONObject();
                        try {
                            job2.put("contacts", ja);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        st = job2.toString();
                        Log.e("paraaaaaaaa", st);
                        ConnectionDetector cd=new ConnectionDetector(MainActivity.this);
                        if(cd.isConnectingToInternet())
                        {
                            t1=new runn();
                            t1.setName("Volley Thread");
                            t1.start();
                            t1.join();
                            /*t1.start();
                            t1.join();
*/                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, new uprofile()).commit();
        NavigationView navView = findViewById(R.id.nav_view);
        View headerView = navView.getHeaderView(0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //final ActionBar actionBar =getSupportActionBar();
       /* BitmapDrawable background = new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.login_bg));
        background.setTileModeX(android.graphics.Shader.TileMode.REPEAT);
        getSupportActionBar().setBackgroundDrawable(background);*/
        intent=new Intent(MainActivity.this,NotificationService.class);
       /* loc = findViewById(R.id.loc1);
        ln = findViewById(R.id.location);*/
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        s = new SessionManager();
        uid1 = s.getPreferences(MainActivity.this, "uid");
        name1 = s.getPreferences(MainActivity.this, "name");
        Log.e("name1=", name1);
        Log.e("uid1=", uid1);
        name = headerView.findViewById(R.id.navname);
        uid = headerView.findViewById(R.id.navuid);
        name.setText(name1);
        uid.setText(uid1);
        locationAndContactsTask();
        new FetchContacts(getApplicationContext());
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    new FetchContacts(MainActivity.this).execute();
                    d.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (contacts != null) {
                                try {
                                    Log.e("contacts", st);
                                    ja = new JSONArray();
                                    t2 = new msg();
                                    t2.setName("Batch Thread");
                                    t2.start();
                                } catch (Exception e) {
                                }
                            }
                            //create getContactsFromOS() to fetch OS Contacts
                        }
                    }, 30000);
                    t.cancel();
                }
            }
        },0,3000);
        mLocationProvider = new LocationProvider(MainActivity.this, this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void EnableGPSAutoMatically() {
        GoogleApiClient googleApiClient = null;
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);
            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                           // Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_SHORT).show();
                            mLocationProvider.connect();

                            // All location settings are satisfied. The client can
                            // initialize location
                            // requests here.
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Toast.makeText(MainActivity.this,"Gps is not ON",Toast.LENGTH_SHORT).show();
                            // Location settings are not satisfied. But could be
                            // fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling
                                // startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(MainActivity.this, 1000);

                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            Toast.makeText(MainActivity.this,"Setting change not allowed here!",Toast.LENGTH_SHORT).show();

                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
    }

    private boolean hasCameraPermission() {
        return EasyPermissions.hasPermissions(this, android.Manifest.permission.CAMERA);
    }

    private boolean hasLocationAndContactsPermissions() {
        return EasyPermissions.hasPermissions(this, LOCATION_AND_CONTACTS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mLocationProvider.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationProvider.disconnect();
    }

    private boolean hasSmsPermission() {
        return EasyPermissions.hasPermissions(this, android.Manifest.permission.READ_SMS);
    }

    private boolean hasStoragePermission() {
        return EasyPermissions.hasPermissions(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void cameraTask() {
        if (hasCameraPermission()) {
            // Have permission, do the thing!
           // Toast.makeText(this, "TODO: Camera things", Toast.LENGTH_LONG).show();
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_camera),
                    RC_CAMERA_PERM,
                    android.Manifest.permission.CAMERA);
        }
    }

    @AfterPermissionGranted(RC_LOCATION_CONTACTS_PERM)
    public void locationAndContactsTask() {
        if (hasLocationAndContactsPermissions()) {
            // Have permissions, do the thing!
           // Toast.makeText(this, "TODO: Location and Contacts things", Toast.LENGTH_LONG).show();
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_location_contacts),
                    RC_LOCATION_CONTACTS_PERM,
                    LOCATION_AND_CONTACTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            String yes = getString(R.string.yes);
            String no = getString(R.string.no);

            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(
                    this,
                    getString(R.string.returned_from_app_settings_to_activity,
                            hasCameraPermission() ? yes : no,
                            hasLocationAndContactsPermissions() ? yes : no,
                            hasSmsPermission() ? yes : no),
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

 /*   @Override
    public void onRationaleAccepted(int requestCode) {
        Log.d(TAG, "onRationaleAccepted:" + requestCode);
    }

    @Override
    public void onRationaleDenied(int requestCode) {
        Log.d(TAG, "onRationaleDenied:" + requestCode);
    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment f = null;
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            f=new uprofile();
           // Toast.makeText(MainActivity.this,"Hello fragment",Toast.LENGTH_SHORT).show();
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            f=new Home();
        } else if (id == R.id.nav_slideshow) {
            f=new MyBookings();
        } else if (id == R.id.nav_manage) {
            s.setPreferences(MainActivity.this,"name","out");
            s.setPreferences(MainActivity.this,"uid","out");
            Intent i=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
            finish();
        }
        if (f != null) {
            //Toast.makeText(MainActivity.this,"inside Hello framgment",Toast.LENGTH_SHORT).show();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, f).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawers();
        return true;
    }

    @Override
    public void onRationaleAccepted(int requestCode) {

    }

    @Override
    public void onRationaleDenied(int requestCode) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    public void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        Toast.makeText(MainActivity.this,"Lat="+currentLatitude+",Long="+currentLongitude,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
