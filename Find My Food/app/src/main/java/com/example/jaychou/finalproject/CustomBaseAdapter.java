package com.example.jaychou.finalproject;

/**
 * Created by jaychou on 5/4/17.
 */


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CheckBox;


public class CustomBaseAdapter extends BaseAdapter {
    Context context;
    int layoutToBeInflated;
    List<business> businessList;
    private boolean[] itemChecked;

    public CustomBaseAdapter(Context context, List<business> businessList, int resource) {
        this.context = context;
        this.businessList = businessList;
        this.layoutToBeInflated = resource;
        //for (int i = 0; i < paintList.size(); i++) {
        //itemChecked[i] = false;
        //}
//        itemChecked = new boolean[businessList.size()];
    }

    @Override
    public int getCount() { return businessList.size(); }

    @Override
    public business getItem(int position) { return businessList.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    public boolean itemIsChecked(int position) { return itemChecked[position]; }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutToBeInflated, null);

            holder = new MyViewHolder();

            // holder provides access to the widgets in the inflated layout
            holder.line1 = (TextView) row.findViewById(R.id.title);
            holder.line2 = (TextView) row.findViewById(R.id.author);
//            holder.cb = (CheckBox) row.findViewById(R.id.checkBox);
            holder.line3 = (TextView) row.findViewById(R.id.line3);


            row.setTag(holder);
        }
        else {
            holder = (MyViewHolder) row.getTag();
        }

        business b = getItem(position);
        holder.line1.setText(b.getName());
        holder.line2.setText("Rating: "+Double.toString(b.getStars()));
        holder.line3.setText(b.getPostCode());




        //holder.Ch.setImageResource(R.drawable.ic_launcher);
//        holder.cb.setOnCheckedChangeListener(null);
//        holder.cb.setChecked(itemChecked[position]);
//        holder.cb.setTag(position);
//        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                itemChecked[position] = b;
//            }
//        });

//        row.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, "Row Clicked - " + position, Toast.LENGTH_SHORT).show();
//            }
//        });
        return row;
    }

    public class MyViewHolder {
        TextView line1;
        TextView line2;
        TextView line3;

    }




}