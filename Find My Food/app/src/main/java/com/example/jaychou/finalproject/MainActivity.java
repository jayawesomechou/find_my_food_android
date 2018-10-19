package com.example.jaychou.finalproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };

    private static String LOG_TAG = "eRRORRRRR";

    List<business> filelist; //this is the object that is read/written from/to the file

    private ShakeListener mShakeListener;

    private CustomBaseAdapter adapter;

    private static final int INITIAL_REQUEST=1337;

    Context context;

    JSONObject jsonObject;
    JSONArray jsonArray;

    EditText et1,et2,et3;
    TextView tv1,tv2, tv3;

    DatabaseHelper helper;
    ArrayList<String> itemlist;
    List<business> businessData = new business().elist; // ArrayList<PaintRecord>
    String [] rating = {"5", ">4.5", ">4",">3.5",">3",
            ">2.5",">2","<1"};

    ArrayList<String> ratinglist = new  ArrayList<String>(Arrays.asList(rating));


    private TextToSpeech tts;              // TTS engine
    private boolean speechReady = false;   // true when TTS engine is loaded


    Spinner abspinner;
    ArrayAdapter<String> dataAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et1=(EditText) findViewById(R.id.et1);
        et2=(EditText) findViewById(R.id.et2);
//        et3=(EditText) findViewById(R.id.et3);

        tv1=(TextView) findViewById(R.id.textView1);
        tv2=(TextView) findViewById(R.id.textView2);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                speechReady = true;
            }
        });

        String text = "SushiMan is selected for you as today's dinner";

        if(speechReady) {

            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "myTTSid");
        }

//        tv3=(TextView) findViewById(R.id.textView3);

//        abspinner = (Spinner) findViewById(R.id.abspinner);
//
//        dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_dropdown_item,
//                ratinglist);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        abspinner.setAdapter(dataAdapter);




//        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
//        progressBar.setProgress(0);





        mShakeListener = new ShakeListener(this);
        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
            @Override
            public void onShake() {
                mShakeListener.stop();
                //do sth
//                Toast.makeText(MainActivity.this,"Shaking!!!!!!!",Toast.LENGTH_LONG).show();


//                if(filelist.size()>0) {
                    Random rand = new Random();

                    int n = rand.nextInt(filelist.size());

                    Toast.makeText(MainActivity.this,filelist.get(n).getName()+" is Selected For You",Toast.LENGTH_LONG).show();

                    String text = filelist.get(n).getName()+" is Selected For You";

                    if(speechReady) {

                        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "myTTSid");
                    }
                    Intent i=new Intent(MainActivity.this,DetailActivity.class);
                    i.putExtra("BObject",filelist.get(n));
                    i.putExtra("shake","1");
                    startActivity(i);

//                }
//
//                else{
//
//
//                    Toast.makeText(MainActivity.this,"Search and Add some restaurant you like",Toast.LENGTH_LONG).show();
//
//                }




                Log.d("shake","11");
                // finish
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mShakeListener.start();

                    }
                }, 2000);
            }
        });



        if (!canAccessLocation() || !canAccessContacts()) {
            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
        }

        // Download JSON file AsyncTask, note how i pass parameter for the progressBar to AsyncTask constructor
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            new DownloadJSON(progressBar).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        }
//        else {
//            new DownloadJSON(progressBar).execute();
//        }
    }




//    // Download JSON file AsyncTask
//    private class DownloadJSON extends AsyncTask<Void, Integer, Void> {
//        ProgressBar bar;
//
//        public DownloadJSON(ProgressBar bar) {
//            this.bar = bar;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            bar.setMax(100);
//            bar.setProgress(0);
//        }
//
//        // parse JSON info and put it in Caricature object
//        @Override
//        protected Void doInBackground(Void... params) {
//            itemlist = new ArrayList<String>();
//
//
//            jsonObject = JSONfunctions.getJSONfromURL("http://www.cs.bc.edu/~signoril/FinalProjectJSON.txt");
//
//            try {
//
//                jsonArray = jsonObject.getJSONArray("restaurant");
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    jsonObject = jsonArray.getJSONObject(i);
//
//                    business temp = new business();
//                    temp.setBusiness_id(jsonObject.optString("business_id"));
//                    temp.setName(jsonObject.optString("name"));
//                    temp.setAddress(jsonObject.optString("address"));
//                    temp.setCity(jsonObject.optString("city"));
//                    temp.setPostCode(jsonObject.optString("postal_code"));
//                    temp.setlat(jsonObject.optString("latitude"));
//                    temp.setLng(jsonObject.optString("longitude"));
//                    temp.setStars(jsonObject.optString("stars"));
//                    temp.setReview_count(jsonObject.optString("review_count"));
//                    temp.setIs_open(jsonObject.optString("is_open"));
////                    temp.setCategories(jsonObject.opt("categories"));
//
////                    temp.setlat(jsonObject.optDouble("latitude"));
////                    temp.setLng(jsonObject.optDouble("longitude"));
////                    temp.setStars(jsonObject.optDouble("stars"));
//
////                    temp.setReview_count(jsonObject.optInt("review_count"));
////                    temp.setPostCode(jsonObject.optInt("postal_code"));
////                    temp.setIs_open(jsonObject.optInt("is_open"));
//
//
////                    businessData.add(temp);
//
//                    //save to SQL
//                    helper.saveContents(temp);
//
//
//
//                    // For ListView use
////                    String s = "Mag: " + eq.getmagnitude() + "   Lat: " + eq.getlat() + "   Lng: " + eq.getlng();
////
////                    itemlist.add(s);
//
//
//                    // add the object to the listview to populate it
//
//                }
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//
//
//            return null;
//        }
//    }


    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm));
    }

    private boolean canAccessContacts() {
        return(hasPermission(Manifest.permission.READ_CONTACTS));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.menu_search:
                // do stuff here
//                Intent addIntent = new Intent(MainActivity.this, AddActivity.class);
//                startActivityForResult(addIntent, 101);

                String s1=et1.getText().toString();
                String s2=et2.getText().toString();
//                String s3=et3.getText().toString();

//                Toast.makeText(this, "map Selected", Toast.LENGTH_SHORT).show();
                Intent search=new Intent(MainActivity.this, SearchActivity.class);

//                search.putExtra("order","2");
                search.putExtra("s1",s1);
                search.putExtra("s2",s2);
//                map.putExtra("s3",s3);


                startActivity(search);
                return true;

            case R.id.menu_show:
                // do stuff here
                show();

                return true;

            case R.id.menu_close:
                // do stuff here
                finish();
//
//                String text="Andre Roach Hahahahahahaha";
//                if (speechReady) {
//                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null,"myTTSid");
//                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void show(){

        Object obj = getObjectFromFile(this, "filename");

        // if obj returns something, check it's type
        if (obj != null && obj instanceof ArrayList) {
            // use the existing List id it exists
            //myList = (ArrayList<String>) obj;
            filelist = (ArrayList<business>) obj; //notice the arraylist is of type student

        } else {
            // create new List if one doesn't exist
            //myList = new ArrayList<String>();
            filelist = new ArrayList<business>();
        }

        ListView lv=(ListView) findViewById(R.id.lv);

         adapter = new CustomBaseAdapter(MainActivity.this,
                filelist, R.layout.list_row);

        lv.setAdapter(adapter);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(MainActivity.this,DetailActivity.class);
                i.putExtra("BObject",filelist.get(position));
                startActivity(i);

            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                filelist.remove(position);
                saveObjectToFile(MainActivity.this,"filename",filelist);
                adapter.notifyDataSetChanged();


                return false;
            }
        });




    }


    public static void saveObjectToFile(Context context, String fileName, Object obj) {

        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.close();

        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, "saveObjectToFile FileNotFoundException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(LOG_TAG, "saveObjectToFile IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(LOG_TAG, "saveObjectToFile Exception: " + e.getMessage());
        }
    }

    public static Object getObjectFromFile(Context context, String filename) {
        try {
            FileInputStream fis = context.openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);

            Object object = ois.readObject();
            ois.close();

            return object;

        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, "getObjectFromFile FileNotFoundException: " + e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e(LOG_TAG, "getObjectFromFile IOException: " + e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            Log.e(LOG_TAG, "getObjectFromFile ClassNotFoundException: " + e.getMessage());
            return null;
        } catch (Exception e) {// Catch exception if any
            Log.e(LOG_TAG, "getBookmarksFromFile Exception: " + e.getMessage());
            return null;
        }
    }


    @Override
    protected void onDestroy() {
        if(mShakeListener!=null)
            mShakeListener.stop();
        super.onDestroy();
    }


}
