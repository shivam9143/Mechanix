package com.androidsolutions.shivam.mechanix;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Date;
import java.util.Enumeration;

import static android.content.Context.LOCATION_SERVICE;
import static com.crashlytics.android.answers.Answers.TAG;

public class Home extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationProvider.LocationCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private GoogleApiClient googleApiClient;
    Activity context;
    GPSTracker gpsTracker;
    //protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    double latitude;
    double longitude;
    String IPaddress;
    Boolean IPValue;
    String macaddress;
    String res = "", res1 = "";
    String host;
    URLEncoder u;
    String address;
    String city;
    ProgressBar pb;
    View rv;
    String resultedData = "";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button loc, add;
    protected LocationManager locationManager;
    ProgressDialog dialog;
    JSONArray jr;
    JSONObject jobj = new JSONObject();
    // flag for GPS status
    boolean isGPSEnabled = false;
    Button sendLoc;
    // flag for network status
    boolean isNetworkEnabled = false;
    TextView ln;
    // flag for GPS status
    boolean canGetLocation = false;
    String provider = "";
    Location location; // location
    JsonParser jsonparser = new JsonParser();
    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    // location last updated time
    private String mLastUpdateTime;
    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;
    TextView lat, lon, addr;
    Location curent;
    EditText et;
    SessionManager s;
    ConnectionDetector cd;
    boolean isInternetPresent;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters
    private android.location.LocationListener listener;
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0; // 1 minute
    //private OnFragmentInteractionListener mListener;
    private LocationProvider mLocationProvider;
    Location l=null;
    ProgressDialog d;
    JSONObject jresult=new JSONObject();
    LoadJS5 js=new LoadJS5();
    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;

    JSONObject job;
    String uid="";
    static String rslt;
    ProgressDialog d1;
    String safound="",saentered="",latfound="",longfound="";

    public Home() {
        // Required empty public constructor

    }


    // TODO: Rename and change types and number of parameters
/*    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        // initialize the necessary libraries
        init();

        // restore the values from saved instance state
        restoreValuesFromBundle(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rv = inflater.inflate(R.layout.fragment_home, container, false);

        // gpsTracker=new GPSTracker(getActivity());
        return rv;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loc = rv.findViewById(R.id.loc);
        ln = rv.findViewById(R.id.fragloc);
        lat = (rv.findViewById(R.id.fraglat));
        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
        lon = rv.findViewById(R.id.fraglong);
        addr = rv.findViewById(R.id.fragaddress);
        add = rv.findViewById(R.id.setloc);
        d1=new ProgressDialog(getActivity());
        dialog = new ProgressDialog(getActivity());
        d=new ProgressDialog(getActivity());
        jr=new JSONArray();
        s=new SessionManager();
        uid=s.getPreferences(getActivity(),"uid");
        et=rv.findViewById(R.id.myadd);
        add.setVisibility(View.GONE);
        loc.setAnimation(animation);
        //pb=rv.findViewById(R.id.progress);
        sendLoc=rv.findViewById(R.id.sendLoc);
        mLocationProvider = new LocationProvider(getActivity(), this);
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        sendLoc.setVisibility(View.GONE);
        mLocationProvider.connect();
        /*Geocoder gc = new Geocoder(context);
        if(gc.isPresent()){
            List<Address> list = null;
            try {
                list = gc.getFromLocation(37.42279, -122.08506,1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = list.get(0);
            StringBuilder str = new StringBuilder();
            str.append("Name: "+address.getLocality()+"\n");
            str.append("Sub-Admin Ares: " + address.getSubAdminArea() + "\n");
            str.append("Admin Area: " + address.getAdminArea() + "\n");
            str.append("Country: " + address.getCountryName() + "\n");
            str.append("Country Code:"  + address.getCountryCode() + "\n");
            String strAddress = str.toString();
        }*/
        String ins="Please read the instructions carefully."+"\n\n"+"1. On start of the application press Fetch Location Button and wait until waiting for location message is updated to some numbers.\n\n2. Press Get My  Garage Address Button.\n\n3. To get accurate address please roam near your garage and press Get My Address Button repeatedly until it shows precise location\n\n4. Enter your Garage Address in text box and press Set this As my Garage Address Button to set your address!\n\nCLICK OK TO CONTINUE";
        showInstructionDialog(ins,"Instructions");
        listener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
               // ln.setText(location.getLatitude() + "," + location.getLongitude());
                if(location!=null)
                {
                    lat.setText(location.getLatitude() + "");
                    lon.setText(location.getLongitude() + "");
                }
                else {
                    showAlertDialog("GPS Connectivity Problem, Try Again!");
                }
                //pb.setVisibility(View.GONE);
                if ( d1!=null && d1.isShowing() ){
                    d1.dismiss();
                    d1=null;
                }
             //   fetchadd();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Toast.makeText(getActivity(), "GPS Is Not ON", Toast.LENGTH_SHORT).show();
            }
        };

        sendLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.clearAnimation();
                cd = new ConnectionDetector(getActivity());
                isInternetPresent = cd.isConnectingToInternet();

                if(et.getText().toString().length()<3)
                {
                    et.setError("Please enter your valid garage address here!");
                }

                else if (!isInternetPresent)
                {
                    Log.e("innnnnnnn", "innnnnn");
                    // Internet Connection is Present
                    // make HTTP requests
                    showAlertDialog(
                            "You don't have internet connection");
                    return;
                }
                else {
                    job = new JSONObject();
                    /*try {
                        *//*job.put("lat", lat.getText().toString().trim());
                        job.put("lon", lon.getText().toString().trim());
                        job.put("address", addr.getText().toString().trim());
                        job.put("faddress",et.getText().toString().trim());
                        jr.put(job);*//*
                        //jresult.put("results", jr);*/
                        Intent i=new Intent(getActivity(),submit.class);
                        i.putExtra("lat",lat.getText().toString().trim());
                        i.putExtra("lon",lon.getText().toString().trim());
                        i.putExtra("add",addr.getText().toString().trim());
                        i.putExtra("fadd",et.getText().toString().trim());
                        startActivity(i);
                        //demo(jresult);
                    /*} catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                }
            }
        });

        loc.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       EnableGPSAutoMatically();
                                       mLocationProvider.connect();
                                       startLocationUpdates();
                                       Handler handler1 = new Handler();
                                       handler1.postDelayed(new Runnable() {
                                           @Override
                                           public void run() {
                                                   if(l!=null)
                                                   {
                                                       lat.setText(l.getLatitude()+"");
                                                       lon.setText(l.getLongitude()+"");
                                                       if ( d1!=null && d1.isShowing() ) {
                                                           d1.dismiss();
                                                           d1 = null;
                                                       }
                                                   }
                                                   else if(mCurrentLocation!=null) {
                                                       updateLocationUI();
                                                       if (d1 != null && d1.isShowing()) {
                                                           d1.dismiss();
                                                           d1 = null;
                                                       }
                                                   }
                                                   else
                                                   {
                                                       if ( d1!=null && d1.isShowing() ) {
                                                           d1.dismiss();
                                                           d1 = null;
                                                       }
                                                       showAlertDialog("Please try again!, Connectivity Problem");
                                                   }

                                           }
                                       },20000);
                                       Handler handler = new Handler();
                                       v.clearAnimation();
                                       add.setVisibility(View.VISIBLE);
                                       add.setAnimation(animation);
                                       handler.postDelayed(new Runnable() {
                                           @Override
                                           public void run() {
                                               if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                                   // TODO: Consider calling
                                                   //    ActivityCompat#requestPermissions
                                                   // here to request the missing permissions, and then overriding
                                                   //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                   //                                          int[] grantResults)
                                                   // to handle the case where the user grants the permission. See the documentation
                                                   // for ActivityCompat#requestPermissions for more details.
                                                   return;
                                               }
                                               // Location l=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                               //ln.setText(l.getLatitude()+","+l.getLongitude());
                                               locationManager.requestLocationUpdates("gps", 5000, 0, listener);
                                           }
                                       },5000);
                                       d1 = ProgressDialog.show(getActivity(), "Wait",
                                               "Fetching Location...", true);
                                   }
                               }
        );
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendLoc.setVisibility(View.VISIBLE);
                view.clearAnimation();
                sendLoc.setAnimation(animation);
                if(lat.getText().toString().length()<4)
                {
                    showAlertDialog("Please click Fetch Location again, G.P.S Connectivity Issue");
                    return;
                }
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                // l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                final String loclat=lat.getText().toString().trim();
                String loclon=lon.getText().toString().trim();
                if(loclat.equals("Waiting for Location"))
                {
                    if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                        location();
                    }
                    d=ProgressDialog.show(getActivity(), "",
                            "Wait Fetching Address...", true);
                    Handler h=new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if ( d!=null && d.isShowing() ) {
                                d.dismiss();
                                d = null;
                            }
                       if(loclat.equals("Waiting for Location"))
                       {
                           Toast.makeText(getActivity(),"Please try after 10 seconds",Toast.LENGTH_SHORT).show();
                       }
                        }
                    },6000);
                }
                else
                {
                    js=new LoadJS5();
                    js.execute("");
                }
            }
        });



    }

    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mSettingsClient = LocationServices.getSettingsClient(getActivity());

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;
        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Restoring values from saved instance state
     */
    private void restoreValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates");
            }

            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location");
            }

            if (savedInstanceState.containsKey("last_updated_on")) {
                mLastUpdateTime = savedInstanceState.getString("last_updated_on");
            }
        }

        updateLocationUI();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates);
        outState.putParcelable("last_known_location", mCurrentLocation);
        outState.putString("last_updated_on", mLastUpdateTime);

    }
    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        Toast.makeText(getActivity(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }
    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            lat.setText(mCurrentLocation.getLatitude() + "");
            lon.setText(mCurrentLocation.getLongitude() + "");
        }
    }
    public void fetchadd()
    {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(l==null)
        {
            if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                location();
            }
            d=ProgressDialog.show(getActivity(), "",
                    "Wait Fetching Address...", true);
            Handler h=new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(d.isShowing())
                        d.dismiss();
                    if(l==null)
                    {
                        Toast.makeText(getActivity(),"Please try after 10 seconds",Toast.LENGTH_SHORT).show();
                    }
                }
            },6000);
        }
        else
        {
            js=new LoadJS5();
            js.execute("");
        }
    }

    public void demo(JSONObject a)
    {
        try
        {
            rslt="START";
            Caller1 c=new Caller1();
            c.json=a;
            c.join();
            c.start();
            while(rslt=="START")
            {
                try
                {
                    Thread.sleep(10);
                }
                catch(Exception ex)
                {
                }
            }
            // Log.e("rslt", rslt);
            if(rslt.contains("(Network is unreachable)"))
            {
                showAlertDialog(
                        "Network Error!! Please Check Your Connection!!");
            }
            else {
                showAlertDialog(
                        "Submitted Successfully,"+rslt.toString());
            }
        }
        catch(Exception ex)
        {
            Log.e("ddddd", ex.getMessage().toString());
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mLocationProvider.connect();
    }

    public void location()
    {
        EnableGPSAutoMatically();
        mLocationProvider.connect();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                // Location l=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                //ln.setText(l.getLatitude()+","+l.getLongitude());
                locationManager.requestLocationUpdates("gps", 1000, 0, listener);
            }
        },5000);

    }
    private void EnableGPSAutoMatically() {
        GoogleApiClient googleApiClient = null;
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
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
                            //Toast.makeText(getActivity(),"Success",Toast.LENGTH_SHORT).show();
                            // All location settings are satisfied. The client can
                            // initialize location
                            // requests here.
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Toast.makeText(getActivity(),"Please turn on GPS",Toast.LENGTH_SHORT).show();
                            // Location settings are not satisfied. But could be
                            // fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling
                                // startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(getActivity(), 1000);

                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            Toast.makeText(getActivity(),"Setting change not allowed here!",Toast.LENGTH_SHORT).show();

                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1000) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
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

    private void showAlertDialog(final String scanContent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(scanContent)
                .setTitle("Result");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Toast.makeText(UserRegistration.this, "Saved", Toast.LENGTH_SHORT).show();


            }
        });
       /* builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(UserRegistration.this, "Not Saved", Toast.LENGTH_SHORT).show();
            }
        });*/

        builder.show();
    }
    private void showInstructionDialog(final String scanContent,String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(scanContent)
                .setTitle(title);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Toast.makeText(UserRegistration.this, "Saved", Toast.LENGTH_SHORT).show();


            }
        });
       /* builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(UserRegistration.this, "Not Saved", Toast.LENGTH_SHORT).show();
            }
        });*/

        builder.show();
    }


    @Override
    public void handleNewLocation(Location location) {
        Log.d("hello", location.toString());
        l=location;
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        //Toast.makeText(getActivity(),"Lat="+currentLatitude+",Long="+currentLongitude,Toast.LENGTH_SHORT).show();

    }

     class LoadJS5 extends AsyncTask<String, String, String>
    {
        protected String doInBackground(String... params)
        {
            try
            {
                //Toast.makeText(getActivity(), "Message="+status, Toast.LENGTH_LONG).show();
                //Log.e("zxcvbn", status);
                //String status=manager.getPreferences(con,"status");
                String h1="";

                  h1="https://maps.googleapis.com/maps/api/geocode/json?latlng="+lat.getText().toString().trim()+","+lon.getText().toString().trim()+"&key=AIzaSyDste8vHLXyi4H5HB6vXBPeMb_LICX6wEA";
                Log.e("ExactLocation", h1);
                resultedData = jsonparser.getJSON(h1);
                Log.e("resultExact",resultedData);
            }
            catch (Exception ex)
            {
                resultedData = "There's an error, that's all I know right now.. :(";
            }
            return resultedData;
        }

        @Override
        protected void onPreExecute()
        {

            dialog = ProgressDialog.show(getActivity(), "",
                    "Loading...", true);
        }



        @Override
        protected void onPostExecute(String r)
        {

            Context con1;
            try {

                if ( dialog!=null && dialog.isShowing() ){
                    dialog.dismiss();
                    dialog=null;
                }
                Log.e("Hello", "world");
                jobj = new JSONObject(r);

                //ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
                res=jobj.getJSONArray("results").toString();
                JSONArray jarray1=new JSONArray(res);

                String res=((jarray1.getJSONObject(0)).getString("formatted_address"));

               // res1=((jarray1.getJSONObject(1)).getString("formatted_address"));
                addr.setText(res);

                Log.e("Result Here", res);
               // Log.e("Result Here.....", res1);
                //loc=res;

/*
                WifiManager manager = (WifiManager)getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = manager.getConnectionInfo();
                macaddress = info.getMacAddress();
                NetwordDetect() ;
                String manufacturer = Build.MANUFACTURER;
                String model = Build.MODEL;
                if (model.startsWith(manufacturer))
                {
                    host=capitalize(model);
                    host=host.replace(" ", "");
                    host=host.trim();
                }
                else
                {
                    host=capitalize(manufacturer) + " " + model;
                    host=host.replace(" ", "");
                    host=host.trim();
                }*/
              //  demo();
            }
//			}
            catch (Exception ex)
            {
                Toast.makeText(getActivity(), "Please check your connection and try again, "+ex.getMessage().toString(), Toast.LENGTH_LONG)
                        .show();
                Log.e("error", ex.getMessage());

            }

        }
    }


    private void NetwordDetect() {

        boolean WIFI = false;

        boolean MOBILE = false;

        ConnectivityManager CM = (ConnectivityManager)getActivity().getApplicationContext(). getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] networkInfo = CM.getAllNetworkInfo();

        for (NetworkInfo netInfo : networkInfo) {

            if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))

                if (netInfo.isConnected())

                    WIFI = true;

            if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))

                if (netInfo.isConnected())

                    MOBILE = true;
        }

        if(WIFI == true)

        {
            IPaddress = GetDeviceipWiFiData();
        }

        if(MOBILE == true)
        {

            IPaddress = GetDeviceipMobileData();


        }

        // Toast.makeText(getActivity(), "Your network ip: " +IPaddress, Toast.LENGTH_LONG).show();
    }


    public String GetDeviceipMobileData(){
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements();) {
                NetworkInterface networkinterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkinterface.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("Current IP", ex.toString());
        }
        return null;
    }

    public String GetDeviceipWiFiData()
    {

        WifiManager wm = (WifiManager)getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        @SuppressWarnings("deprecation")

        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        return ip;

    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
  /*  public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

  */ /*

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/
    /*@Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(getActivity(),"hello",Toast.LENGTH_SHORT).show();
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
   /* public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
