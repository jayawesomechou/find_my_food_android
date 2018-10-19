package com.example.jaychou.finalproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaychou on 4/30/17.
 */


public class DetailActivity extends AppCompatActivity {

    private static String LOG_TAG = "eRRORRRRR";


    List<business> filelist; //this is the object that is read/written from/to the file

    business received;
    TextView tv1,tv2, tv3,tv4,tv5, tv6, tv7, state,phone,close,lat,lng;

    private TextToSpeech tts;              // TTS engine
    private boolean speechReady = false;   // true when TTS engine is loaded

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                speechReady = true;
            }
        });

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


        tv1=(TextView) findViewById(R.id.textView1);
        tv2=(TextView) findViewById(R.id.textView2);
        tv3=(TextView) findViewById(R.id.textView3);


        tv5=(TextView) findViewById(R.id.textView5);
        tv6=(TextView) findViewById(R.id.textView6);
        tv7=(TextView) findViewById(R.id.textView7);


        lat=(TextView) findViewById(R.id.lat);

        lng=(TextView) findViewById(R.id.lng);

        tv6=(TextView) findViewById(R.id.textView6);
        tv7=(TextView) findViewById(R.id.textView7);

        state=(TextView) findViewById(R.id.state);

        phone=(TextView) findViewById(R.id.phone);

        close=(TextView) findViewById(R.id.close);





        Intent detail=getIntent();

        received = (business) detail.getSerializableExtra("BObject");


        speak();

//        String text=received.getName()+"is Selected For You";
//        if (speechReady) {
//            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null,"myTTSid");
//        }


//        if(detail.getStringExtra("shake").equals("1")){
//
//            tts.speak(received.getName()+" is selected for you as today's dinner", TextToSpeech.QUEUE_FLUSH, null,"myTTSid");
//
//        }




        tv1.setText("Name:  "+received.getName());
        tv2.setText("Address:    "+received.getAddress());
        tv3.setText("City:   "+received.getCity());

        state.setText("State:   "+received.getState());


        phone.setText("Phone:    "+received.getPhone());


        lat.setText("Lat:  "+Double.toString(received.getlat()));

        lng.setText("Lng:  "+Double.toString(received.getLng()));

        tv5.setText("Postal Code:   "+received.getPostCode());
        tv6.setText("Rating:    "+Double.toString(received.getStars()));

        tv7.setText("Review Count:   "+Integer.toString(received.getReview_count()));

        if(received.getIs_close()){
            close.setText("Closed Now");
        }

        else{
            close.setText("Open Now");
        }


        ImageView imageView = (ImageView) findViewById(R.id.paint);
        new downloadImage(imageView).execute(received.getImage_url().toString());


    }



//    public void show(View view) {
//        // do stuff here
//        //get the data from the edittext fields
//        Intent show=new Intent(DetailActivity.this, MapActivity.class);
//        show.putExtra("showObject",received);
//        show.putExtra("order","1");
//
//        startActivity(show);
//
//    }

    private class downloadImage extends AsyncTask<String, Integer, Bitmap> {
        ImageView image;

        public downloadImage(ImageView image) {
            this.image = image;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap image ;
            HttpURLConnection httpConn = null;
            int resCode;
            InputStream in = null;

            //remember, there are two ways to do this
            try {
                URL url = new URL(params[0]);
                URLConnection urlConn = url.openConnection();
                if (!(urlConn instanceof HttpURLConnection)) {
                    throw new IOException("URL is not an Http URL");
                }

                httpConn = (HttpURLConnection) urlConn;
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                resCode = httpConn.getResponseCode();

                if (resCode == HttpURLConnection.HTTP_OK) {
                    in = httpConn.getInputStream();
                }

                image = BitmapFactory.decodeStream(in);
                return image;
            } catch (Throwable t) {
                t.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (Throwable t) {
                }
                try {
                    httpConn.disconnect();
                } catch (Throwable t) {
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            //super.onPostExecute(result);
            image.setImageBitmap(result);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detailmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.menu_show:
                Intent show=new Intent(DetailActivity.this, MapActivity.class);
                show.putExtra("showObject",received);
                show.putExtra("order","1");

                startActivity(show);
                return true;

            case R.id.menu_add:


                filelist.add(received);
                saveObjectToFile(this,"filename",filelist);
                Toast.makeText(this, "Restaurant Added", Toast.LENGTH_SHORT).show();

//                Intent show=new Intent(DetailActivity.this, MapActivity.class);
//                show.putExtra("showObject",received);
//                show.putExtra("order","1");
//
//                startActivity(show);
                return true;

            case R.id.menu_back:
                // do stuff here
                Intent back=new Intent(DetailActivity.this, MainActivity.class);
                startActivity(back);

                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void speak(){
//        String text="Andre Roach Hahahahahahaha";
//            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null,"myTTSid");
//

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


}