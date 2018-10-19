package com.example.jaychou.finalproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jaychou on 4/30/17.
 */

public class SearchActivity extends AppCompatActivity {

    private String ss1,ss2;

    ListView lv;
    ArrayList<business> businessObjs;
    ArrayList<String> namelist=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent search=getIntent();
        final String s1=search.getStringExtra("s1");
        final String s2=search.getStringExtra("s2");

        ss1=s1;
        ss2=s2;

         businessObjs = new ArrayList<business>();

        lv=(ListView) findViewById(R.id.lv);

        setProgressBarIndeterminateVisibility(true);
        new AsyncTask<Void, Void, List<business>>() {
            @Override
            protected List<business> doInBackground(Void... params) {
                String businesses = Yelp.getYelp(SearchActivity.this).search(s1, s2);

//				String businesses = Yelp.getYelp(YelpSearchListActivity.this).singlesearch(searchTerm);

                try {

                    return processJson(businesses);
                } catch (JSONException e) {
                    return Collections.<business>emptyList();
                }
            }

            @Override
            protected void onPostExecute(List<business> businesses) {
                setTitle("Search Results");


                CustomBaseAdapter adapter = new CustomBaseAdapter(SearchActivity.this,
                        businessObjs, R.layout.list_row);

                lv.setAdapter(adapter);
                lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                setProgressBarIndeterminateVisibility(false);
//                lv.setAdapter(new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_list_item_1, namelist));
            }
        }.execute();


//        lv.setAdapter(new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_list_item_1, namelist));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detail=new Intent(SearchActivity.this, DetailActivity.class);

                detail.putExtra("BObject",businessObjs.get(position));


                startActivity(detail);




            }
        });





    }


    List<business> processJson(String jsonStuff) throws JSONException {
        JSONObject json = new JSONObject(jsonStuff);
        JSONArray businesses = json.getJSONArray("businesses");
        for (int i = 0; i < businesses.length(); i++) {
            JSONObject jsonObject = businesses.getJSONObject(i);

                    business temp = new business();
                    temp.setBusiness_id(jsonObject.optString("id"));
                    temp.setName(jsonObject.optString("name"));

                    temp.setReview_count(jsonObject.optInt("review_count"));



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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.menu_show:
                // do stuff here
//                Intent addIntent = new Intent(MainActivity.this, AddActivity.class);
//                startActivityForResult(addIntent, 101);


//                Toast.makeText(this, "map Selected", Toast.LENGTH_SHORT).show();
                Intent search=new Intent(SearchActivity.this, MapActivity.class);

                search.putExtra("order","2");

                search.putExtra("s1",ss1);
                search.putExtra("s2",ss2);

//                search.putExtra("BList",businessObjs);
//                search.put
////                search.putExtra("order","2");
//                search.putExtra("s1",s1);
//                search.putExtra("s2",s2);
//                map.putExtra("s3",s3);


                startActivity(search);
                return true;

            case R.id.menu_back:
                // do stuff here
               Intent back=new Intent(SearchActivity.this, MainActivity.class);
                startActivity(back);

                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    }
