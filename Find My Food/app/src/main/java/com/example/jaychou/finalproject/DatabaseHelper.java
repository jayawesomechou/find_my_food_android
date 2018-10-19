package com.example.jaychou.finalproject;

/**
 * Created by jaychou on 4/24/17.
 */

import android.content.Context;
import java.util.List;
import java.util.ArrayList;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import android.widget.TextView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.Calendar;

public class DatabaseHelper {

    Context context;
    private SQLiteDatabase db;
    private final String dbName = "JSONdb";


    DatabaseHelper(Context context) {
        this.context = context;
        try {
            db = context.openOrCreateDatabase(dbName, 0, null);
            createTable();

        } catch (Exception ex) {
            Toast.makeText(context, "failed", Toast.LENGTH_LONG).show();
        }


    }


    //create table
    private void createTable() {
        String Query = "SELECT name FROM sqlite_master WHERE type='table'";
        Cursor cs = db.rawQuery(Query, null);
        cs.moveToFirst();
        int nameIdx = cs.getColumnIndex("name");
        while (cs.isAfterLast() == false)
        {
            String name = cs.getString(nameIdx);
            if (  name.equals("JSONlist"))
                return;
            cs.moveToNext();
        }
        db.beginTransaction();
        try{

            db.execSQL("create table JSONlist (" +
                    " jsonID integer PRIMARY KEY autoincrement," +
                    " business_id text," +
                    " name text," +
                    " address text," +
                    " city text," +
                    " state text," +
                    " postal_code text," +
                    " latitude DOUBLE," +
                    " longitude DOUBLE," +
                    " stars text," +
                    " review_count text," +
                    " is_close text);  ");
            db.setTransactionSuccessful();
        }
        catch(Exception ex){
            Toast.makeText(context, "Failed to create table" , Toast.LENGTH_LONG).show();
        }
        finally {
            db.endTransaction();
        }

    }

    //Insert an item into database
    public void saveContents(business item) {
        db.beginTransaction();
        try {

            String submit = " insert into JSONlist(business_id,name,address,city,state,postal_code,latitude,longitude,stars,review_count,is_close) " +
                    "values ( '" + item.getBusiness_id() + "','" + item.getName() + "', '" + item.getAddress() + "','" + item.getCity() + "','" +
                    item.getState() + "','" + item.getPostCode() + "','" + Double.valueOf(item.getlat()) + "','" + Double.valueOf(item.getLng()) + "', '"
                    + item.getStars() + "','" + item.getReview_count() + "', '" + item.getIs_close() + "');";
            db.execSQL(submit);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Toast.makeText(context, "failed to enter data", Toast.LENGTH_LONG).show();
        } finally {
            db.endTransaction();
        }
    }
//


//    public void performQuery(double d1,double d2, double d3, List<MapItem> itemlist) {
////
////        Cursor cs = db.rawQuery("SELECT * FROM JSONlist  Where   lat < ?   AND lat > ? AND lng<?  AND  lng>?  AND magnitude>?",
////                new String[] {Double.toString(d1+10),Double.toString(d1-10),Double.toString(d2+10), Double.toString(d2-10), Double.toString(d3)});
////        int latId = cs.getColumnIndex("lat");
////        int lngId = cs.getColumnIndex("lng");
////        int magId = cs.getColumnIndex("magnitude");
////        cs.moveToFirst();
////        while (cs.isAfterLast() == false) {
////            double newlat = cs.getDouble(latId);
////            double newlng = cs.getDouble(lngId);
////            double newmag = cs.getDouble(magId);
////
////            itemlist.add(new MapItem(newlat, newlng, newmag));
////            cs.moveToNext();
//        }





}
