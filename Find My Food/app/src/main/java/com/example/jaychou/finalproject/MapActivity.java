package com.example.jaychou.finalproject;

/**
 * Created by jaychou on 4/29/17.
 */


import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import android.location.Geocoder;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import android.content.Intent;

import java.util.ArrayList;
import java.util.*;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MapActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap map;
    private LatLng myLocation;
    private LatLng latLng;
    private LatLng latLng2;

    private String order;
    private String depth;

//    private ArrayList<MapItem> itemlist;

    private TextView tv1;
    private String bding;

    private SQLiteDatabase db;


    double lat, lng;
    Geocoder geocoder = null;
    List<Address> addressList = null;
//    DatabaseHelper helper;


    ArrayList<String> location;

    business received;

    ArrayList<business> Blist;

    ArrayList<business> businessObjs;
    ArrayList<String> namelist=new ArrayList<String>();


    String s1,s2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        businessObjs = new ArrayList<business>();



//        helper=new DatabaseHelper(this);

//        itemlist=new ArrayList<MapItem>();


//        final Intent i = getIntent();
//
//        bding=i.getStringExtra("Bdname");


        Intent i = getIntent();

        order=i.getStringExtra("order");


        s1=i.getStringExtra("s1");
        s2=i.getStringExtra("s2");

        received = (business) i.getSerializableExtra("showObject");


//        Blist=(ArrayList<business>) i.getSerializableExtra("BList");

//        order=i.getStringExtra("order");
//        if(order.equals("1")) {
//            final Earthquake received = (Earthquake) i.getSerializableExtra("personObject");
//            lat = Double.valueOf(received.getlat());
//            lng = Double.valueOf(received.getlng());
//            depth = received.getdepth();
//        }
//
//        else if(order.equals("2")){
//            final double d3 = Double.valueOf(i.getStringExtra("s1"));
//            final double d2 = Double.valueOf(i.getStringExtra("s2"));
//            final double d1 = Double.valueOf(i.getStringExtra("s3"));
//
//            helper.performQuery(d1,d2,d3,itemlist);
////            performQuery(d1,d2,d3);
//
//
//        }
//        else{}


       if(order.equals("2")) {
           new AsyncTask<Void, Void, List<business>>() {
               @Override
               protected List<business> doInBackground(Void... params) {
                   String businesses = Yelp.getYelp(MapActivity.this).search(s1, s2);

//				String businesses = Yelp.getYelp(YelpSearchListActivity.this).singlesearch(searchTerm);

                   try {

                       return processJson(businesses);
                   } catch (JSONException e) {
                       return Collections.<business>emptyList();
                   }
               }


           }.execute();

       }


        tv1 = (TextView) findViewById(R.id.tv1);


//        namelist=new ArrayList<String>();
//        schedulelist=new ArrayList<String>();
//        locationlist=new ArrayList<String>();


//        readFromFile("courseNameTemp1.txt",namelist);
//        readFromFile("courseScheduleTemp1.txt",schedulelist);
//        readFromFile("courseLocationTemp1.txt",locationlist);

//        String s=namelist.get(1);
//        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
//


//
//        for (int i=0;i<namelist.size(); i++){
//            s=s.concat(namelist.get(i));
//        }

//        tv2.setText(namelist.get(0));


        geocoder = new Geocoder(this);


        //gets the maps to load
        MapFragment mf = (MapFragment) getFragmentManager().findFragmentById(R.id.the_map);
        mf.getMapAsync(this);


//        Toast.makeText(this, Integer.toString(namelist.size()), Toast.LENGTH_SHORT).show();


    }


    @Override
    public void onMapReady(GoogleMap map) {    // map is loaded but not laid out yet
        this.map = map;
        map.setOnMapLoadedCallback(this);      // calls onMapLoaded when layout done
        UiSettings mapSettings;
        mapSettings = map.getUiSettings();
        mapSettings.setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
    }


    // maps are loaded and this is where I should perform the findArt() function to find my art
    //note use of geocoder.getFromLocationName() to find LonLat from address

    @Override
    public void onMapLoaded() {

        if (order.equals("1")) {

            map = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.the_map)).getMap();

            latLng = new LatLng(received.getlat(), received.getLng());

            map.addMarker(new MarkerOptions()
                    .position(latLng).title(received.getName()));

        }
//
        else if(order.equals("2")){

            map = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.the_map)).getMap();

            if(businessObjs.size()>0) {
                for (int k = 0; k < businessObjs.size(); k++) {
                    latLng2 = new LatLng(businessObjs.get(k).getlat(), businessObjs.get(k).getLng());


                    if (businessObjs.get(k).getStars()==3.0 || businessObjs.get(k).getStars()==2.5 || businessObjs.get(k).getStars()==2.0) {
                        map.addMarker(new MarkerOptions()
                                .position(latLng2)
                                .title(businessObjs.get(k).getName())
                                .snippet("Rating: " + businessObjs.get(k).getStars() + "      "
                                        + "Review Count:   " + Integer.toString(businessObjs.get(k).getReview_count()))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))


                        );

                    }
                    else if (businessObjs.get(k).getStars()==4.0 || businessObjs.get(k).getStars()==3.5 ) {
                        map.addMarker(new MarkerOptions()
                                .position(latLng2)
                                .title(businessObjs.get(k).getName())
                                .snippet("Rating: " + businessObjs.get(k).getStars() + "      "
                                        + "Review Count:   " + Integer.toString(businessObjs.get(k).getReview_count()))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))

                        );

                    }

                    else if (businessObjs.get(k).getStars()==4.5) {
                        map.addMarker(new MarkerOptions()
                                .position(latLng2)
                                .title(businessObjs.get(k).getName())
                                .snippet("Rating: " + businessObjs.get(k).getStars() + "      "
                                        + "Review Count:   " + Integer.toString(businessObjs.get(k).getReview_count()))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))

                        );

                    }
                    else {
                        map.addMarker(new MarkerOptions()
                                .position(latLng2)
                                .title(businessObjs.get(k).getName())
                                .snippet("Rating: " + businessObjs.get(k).getStars() + "      "
                                        + "Review Count:   " + Integer.toString(businessObjs.get(k).getReview_count()))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

                        );

                    }





                }
            }
            else{}

        }

        else{}


        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
//                String title = marker.getSnippet();
//                //Toast.makeText(getApplicationContext(), "Info window clicked", Toast.LENGTH_LONG).show();
//                //Toast.makeText(getApplicationContext(), "title: " + title, Toast.LENGTH_LONG).show();
//                business toSend;
//                for (int i = 0; i < businessObjs.size(); i++) {
//                    if (title.equals(businessObjs.get(i).getName())) {
//                        toSend = businessObjs.get(i);
//                        //Toast.makeText(getApplicationContext(), "Was the same" + title, Toast.LENGTH_LONG).show();
//                        Intent detail = new Intent(getApplicationContext(), DetailActivity.class);
//                        detail.putExtra("Bobject", toSend);
//                        startActivity(detail);
//                        break;
//                    }
//                }
                return false;
            }
        });
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String title = marker.getTitle();
//                Toast.makeText(this, title, Toast.LENGTH_LONG).show();

//                Toast.makeText(getApplicationContext(), title, Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), "title: " + title, Toast.LENGTH_LONG).show();
                int i=namelist.indexOf(title);
                Intent detail = new Intent(getApplicationContext(), DetailActivity.class);
                detail.putExtra("BObject", businessObjs.get(i));
                startActivity(detail);

//                for (int i = 0; i < businessObjs.size(); i++) {
//                    if (title.equals(businessObjs.get(i).getName())) {
//                        toSend = businessObjs.get(i);
//                        //Toast.makeText(getApplicationContext(), "Was the same" + title, Toast.LENGTH_LONG).show();
//                        Intent detail = new Intent(getApplicationContext(), DetailActivity.class);
//                        detail.putExtra("Bobject", toSend);
//                        startActivity(detail);
//                        break;
//                    }
//                }
            }
        });

        // read user's current location, if possible
//        myLocation = getMyLocation();
//        if (myLocation == null) {
//            //Toast.makeText(this, "Unable to access your location. Consider enabling Location in your device's Settings.", Toast.LENGTH_LONG).show();
//        } else {
//            map.addMarker(new MarkerOptions()
//                    .position(myLocation)
//                    .title("ME!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
//            );
//        }
    }






    /*
         * Returns the user's current location as a LatLng object.
         * Returns null if location could not be found (such as in an AVD emulated virtual device).
         */

    private LatLng getMyLocation() {
        // try to get location three ways: GPS, cell/wifi network, and 'passive' mode
//        requestPermissions();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (loc == null) {
            // fall back to network if GPS is not available
            loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (loc == null) {
            // fall back to "passive" location if GPS and network are not available
            loc = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }

        if (loc == null) {
            //Toast.makeText(this, "Unable to access your location. Consider enabling Location in your device's Settings.", Toast.LENGTH_LONG).show();
            return null;   // could not get user's location
        } else {
            double myLat = loc.getLatitude();
            double myLng = loc.getLongitude();
            return new LatLng(myLat, myLng);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (myLocation != null) {
            LatLng markerLatLng = marker.getPosition();
            map.addPolyline(new PolylineOptions()
                    .add(myLocation)
                    .add(markerLatLng)
            );
            return true;
        } else {
            return false;
        }
    }


    List<business> processJson(String jsonStuff) throws JSONException {
        JSONObject json = new JSONObject(jsonStuff);
        JSONArray businesses = json.getJSONArray("businesses");
        for (int i = 0; i < businesses.length(); i++) {
            JSONObject jsonObject = businesses.getJSONObject(i);

            business temp = new business();
            temp.setBusiness_id(jsonObject.optString("id"));
            temp.setName(jsonObject.optString("name"));


//                    temp.setAddress(jsonObject.optString("address"));
//                    temp.setCity(jsonObject.optString("city"));
//                    temp.setPostCode(jsonObject.optString("postal_code"));
//                    temp.setlat(jsonObject.optString("latitude"));
//                    temp.setLng(jsonObject.optString("longitude"));
//                    temp.setStars(jsonObject.optString("stars"));
            temp.setReview_count(jsonObject.optInt("review_count"));
//                    temp.setIs_open(jsonObject.optString("is_open"));
//                    temp.setCategories(jsonObject.opt("categories"));



            temp.setPhone(jsonObject.optString("display_phone"));
            temp.setImage_url(jsonObject.optString("image_url"));
            temp.setIs_close(jsonObject.optBoolean("is_closed"));


            JSONObject location=jsonObject.getJSONObject("location");

            temp.setCity(location.optString("city"));

            temp.setAddress(location.optString("address"));

            temp.setPostCode(location.optString("postal_code"));
            temp.setState(location.optString("state_code"));



            JSONObject coordinate=location.getJSONObject("coordinate");

            temp.setlat(coordinate.optDouble("latitude"));
            temp.setLng(coordinate.optDouble("longitude"));

            temp.setStars(jsonObject.optDouble("rating"));


            namelist.add(temp.getName());
            businessObjs.add(temp);



        }
        return businessObjs;
    }



}