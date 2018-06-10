package com.example.hamzakhan.weather;

import android.Manifest;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.service.voice.VoiceInteractionSession;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity  {
    TextView tx1, tx2, tx3;
    TextView min1, min2, min3, min4, min5, min6, date_;
    TextView max1, max2, max3, max4, max5, max6;
    TextView day1, day2, day3, day4, day5, day6;
    ImageButton imageButton;
    String Today;

    String num;
    String icon1;
    String message = null;

    private LocationManager locationManager;

    String lat, lon;
    private Bitmap bitmap;
    ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);





        imageView1 = (ImageView) findViewById(R.id.imageView);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView4 = (ImageView) findViewById(R.id.imageView4);
        imageView5 = (ImageView) findViewById(R.id.imageView5);
        imageView6 = (ImageView) findViewById(R.id.imageView6);
        imageView7 = (ImageView) findViewById(R.id.imageView7);

//if user has not selected any city
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                message="new york";
            } else {
                message = extras.getString("message");
            }
        } else {
            message = (String) savedInstanceState.getSerializable("message");
        }
        tx1 = (TextView) findViewById(R.id.Temprature);
        tx2 = (TextView) findViewById(R.id.Country_name);
        tx3 = (TextView) findViewById(R.id.Description);

        min1 = (TextView) findViewById(R.id.Day1);
        min2 = (TextView) findViewById(R.id.Day2);
        min3 = (TextView) findViewById(R.id.Day3);
        min4 = (TextView) findViewById(R.id.Day4);
        min5 = (TextView) findViewById(R.id.Day5);
        min6 = (TextView) findViewById(R.id.Day6);

        max1 = (TextView) findViewById(R.id.textView);
        max2 = (TextView) findViewById(R.id.textView2);
        max3 = (TextView) findViewById(R.id.textView3);
        max4 = (TextView) findViewById(R.id.textView4);
        max5 = (TextView) findViewById(R.id.textView5);
        max6 = (TextView) findViewById(R.id.textView6);

        day1 = (TextView) findViewById(R.id.textView7);
        day2 = (TextView) findViewById(R.id.textView8);
        day3 = (TextView) findViewById(R.id.textView9);
        day4 = (TextView) findViewById(R.id.textView10);
        day5 = (TextView) findViewById(R.id.textView11);
        day6 = (TextView) findViewById(R.id.textView12);


        tx1 = (TextView) findViewById(R.id.Temprature);


        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int[] count = new int[7];
//set current day
        switch (day) {
            case Calendar.SUNDAY:
                Today = "Sun";
                count[0] = 1;
                break;

            case Calendar.MONDAY:
                Today = "Mon";
                count[1] = 1;

                break;

            case Calendar.TUESDAY:
                Today = "Tue";
                count[2] = 1;
                break;

            case Calendar.WEDNESDAY:
                Today = "Wed";
                count[3] = 1;
                break;

            case Calendar.THURSDAY:
                Today = "Thu";
                count[4] = 1;
                break;

            case Calendar.FRIDAY:
                Today = "Fri";
                count[5] = 1;
                break;

            case Calendar.SATURDAY:
                Today = "Sat";
                count[6] = 1;
                break;

        }

        String[] week_days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        String[] current_days = new String[5];
//finding next 5 days according to the current day generic code
        for (int i = 0; i < 7; i++) {
            if (count[i] == 1) {

                int k = 0;
                i++;
                do {
                    if (i == 7) {
                        i = 0;
                    }
                    current_days[k] = week_days[i];
                    i++;
                    k++;
                } while (k != 5);
                break;
            }
        }

        day1.setText(Today);
        day2.setText(current_days[0]);
        day3.setText(current_days[1]);
        day4.setText(current_days[2]);
        day5.setText(current_days[3]);
        day6.setText(current_days[4]);


        date_ = (TextView) findViewById(R.id.Date);

        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, Search_cities.class);
                startActivity(in);

            }
        });

if(message!=null) {
    if (message.matches("[0-9]+")) {

        Set_weather_by_zipcode();
        Set_Forcast_days();
    }
    else if(message.matches("[0-9 ]+"))
    {

        String[] temp=message.split(" ");
     lat=temp[0];
     lon=temp[1];

     Set_weather_by_lon_lat();
     Set_Forcast_days_lat_lon();

    }

    else {
          Set_Weather_By_Name();
          Set_Forcast_days();


    }

}



    }

    private void Set_Weather_By_Name() {

        String url="http://api.openweathermap.org/data/2.5/weather?q="+message+"&appid=62f2014ba95c2e7d58e6f529defab9e4&units=imperial";

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{

                JSONObject jsonObject=response.getJSONObject("main");
                    JSONArray jsonArray=response.getJSONArray("weather");
                    JSONObject object=jsonArray.getJSONObject(0);
                    String temprature=String.valueOf(jsonObject.getInt("temp"));
                    String  temprature_min=String.valueOf(jsonObject.getInt("temp_min"));
                    String  temprature_max=String.valueOf(jsonObject.getInt("temp_max"));

                    tx1.setText(temprature);
                    min1.setText(temprature_min);
                    max1.setText(temprature_max);
                    String description=object.getString("description");

                    tx3.setText(description.toUpperCase());

                    num=object.getString("icon");
                    loadimage(imageView1,"http://openweathermap.org/img/w/"+num+".png");

                    loadimage(imageView7,"http://openweathermap.org/img/w/"+num+".png");




                    if(description.equals("light intensity shower rain")) {
                        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_main);
                        relativeLayout.setBackgroundResource(R.drawable.rain);
                    }
                   String Country_name=response.getString("name");
                    tx2.setText(Country_name);
                    Calendar calendar=Calendar.getInstance();
                   SimpleDateFormat s=new SimpleDateFormat("MM-dd-EEEE");
                    String date=s.format(calendar.getTime());
                    date_.setText(date);




                }
                catch (JSONException e)
                {   e.printStackTrace();

                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }
        );
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }//setweather by name of city selected through list of cities

    private void loadimage(ImageView im ,String url)// set images for current weather conditions
    {

        Picasso.with(this).load(url).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                .into(im,new com.squareup.picasso.Callback()
                {

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });

    }
    private void Set_weather_by_zipcode()
    {
        String url="http://api.openweathermap.org/data/2.5/weather?zip="+message+",us&appid=62f2014ba95c2e7d58e6f529defab9e4&units=imperial";

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    //read json data from url using given api

                    JSONObject jsonObject=response.getJSONObject("main");
                    JSONArray jsonArray=response.getJSONArray("weather");
                    JSONObject object=jsonArray.getJSONObject(0);
                    String temprature=String.valueOf(jsonObject.getInt("temp"));
                    String  temprature_min=String.valueOf(jsonObject.getInt("temp_min"));
                    String  temprature_max=String.valueOf(jsonObject.getInt("temp_max"));

                    tx1.setText(temprature);
                    min1.setText(temprature_min);
                    max1.setText(temprature_max);
                    String description=object.getString("description");

                    tx3.setText(description.toUpperCase());

                    num=object.getString("icon");
                    loadimage(imageView1,"http://openweathermap.org/img/w/"+num+".png");

                    loadimage(imageView7,"http://openweathermap.org/img/w/"+num+".png");




                    if(description.equals("light intensity shower rain")) {
                        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_main);
                        relativeLayout.setBackgroundResource(R.drawable.rain);
                    }
                    String Country_name=response.getString("name");
                    tx2.setText(Country_name);
                    Calendar calendar=Calendar.getInstance();
                    SimpleDateFormat s=new SimpleDateFormat("MM-dd-EEEE");
                    String date=s.format(calendar.getTime());
                    date_.setText(date);




                }
                catch (JSONException e)
                {   e.printStackTrace();

                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }
        );
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
    private void Set_weather_by_lon_lat()
    {
        String url="http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid=62f2014ba95c2e7d58e6f529defab9e4&units=imperial";

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{

                    JSONObject jsonObject=response.getJSONObject("main");
                    JSONArray jsonArray=response.getJSONArray("weather");
                    JSONObject object=jsonArray.getJSONObject(0);
                    String temprature=String.valueOf(jsonObject.getInt("temp"));
                    String  temprature_min=String.valueOf(jsonObject.getInt("temp_min"));
                    String  temprature_max=String.valueOf(jsonObject.getInt("temp_max"));

                    tx1.setText(temprature);
                    min1.setText(temprature_min);
                    max1.setText(temprature_max);
                    String description=object.getString("description");

                    tx3.setText(description.toUpperCase());

                    num=object.getString("icon");
                    loadimage(imageView1,"http://openweathermap.org/img/w/"+num+".png");

                    loadimage(imageView7,"http://openweathermap.org/img/w/"+num+".png");




                    if(description.equals("light intensity shower rain")) {
                        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_main);
                        relativeLayout.setBackgroundResource(R.drawable.rain);
                    }
                    String Country_name=response.getString("name");
                    tx2.setText(Country_name);
                    Calendar calendar=Calendar.getInstance();
                    SimpleDateFormat s=new SimpleDateFormat("MM-dd-EEEE");
                    String date=s.format(calendar.getTime());
                    date_.setText(date);




                }
                catch (JSONException e)
                {   e.printStackTrace();

                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }
        );
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
    private void Set_Forcast_days()
    {
        String url="http://api.openweathermap.org/data/2.5/forecast?q="+message+"&appid=62f2014ba95c2e7d58e6f529defab9e4&units=imperial";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                            try
                            {
                               JSONArray jsonArray=response.getJSONArray("list");



                                   JSONObject jsonObject=jsonArray.getJSONObject(0);
                                   JSONObject jsonObject1=jsonObject.getJSONObject("main");
                                   String temp1=String.valueOf(jsonObject1.getInt("temp_min"));
                                   String temp2= String.valueOf(jsonObject1.getInt("temp_max"));
                                   max2.setText(temp2);
                                   min2.setText(temp1);

                                   JSONArray jsonArray1=jsonObject.getJSONArray("weather");
                                   JSONObject jsonObject2=jsonArray1.getJSONObject(0);
                                   icon1=jsonObject2.getString("icon");
                                   loadimage(imageView2,"http://openweathermap.org/img/w/"+icon1+".png");



                                   jsonObject=jsonArray.getJSONObject(7);
                                   jsonObject1=jsonObject.getJSONObject("main");
                                   temp1=String.valueOf(jsonObject1.getInt("temp_min"));
                                   temp2= String.valueOf(jsonObject1.getInt("temp_max"));
                                max3.setText(temp2);
                                min3.setText(temp1);


                                 jsonArray1=jsonObject.getJSONArray("weather");
                                 jsonObject2=jsonArray1.getJSONObject(0);
                                icon1=jsonObject2.getString("icon");
                                loadimage(imageView3,"http://openweathermap.org/img/w/"+icon1+".png");

                                jsonObject=jsonArray.getJSONObject(15);
                                jsonObject1=jsonObject.getJSONObject("main");
                                temp1=String.valueOf(jsonObject1.getInt("temp_min"));
                                temp2= String.valueOf(jsonObject1.getInt("temp_max"));
                                max4.setText(temp2);
                                min4.setText(temp1);

                                jsonArray1=jsonObject.getJSONArray("weather");
                                jsonObject2=jsonArray1.getJSONObject(0);
                                icon1=jsonObject2.getString("icon");
                                loadimage(imageView4,"http://openweathermap.org/img/w/"+icon1+".png");


                                jsonObject=jsonArray.getJSONObject(23);
                                jsonObject1=jsonObject.getJSONObject("main");
                                temp1=String.valueOf(jsonObject1.getInt("temp_min"));
                                temp2= String.valueOf(jsonObject1.getInt("temp_max"));
                                max5.setText(temp2);
                                min5.setText(temp1);

                                jsonArray1=jsonObject.getJSONArray("weather");
                                jsonObject2=jsonArray1.getJSONObject(0);
                                icon1=jsonObject2.getString("icon");
                                loadimage(imageView5,"http://openweathermap.org/img/w/"+icon1+".png");


                                jsonObject=jsonArray.getJSONObject(31);
                                jsonObject1=jsonObject.getJSONObject("main");

                                temp1=String.valueOf(jsonObject1.getInt("temp_min"));
                                temp2= String.valueOf(jsonObject1.getInt("temp_max"));
                                max6.setText(temp2);
                                min6.setText(temp1);

                                jsonArray1=jsonObject.getJSONArray("weather");
                                jsonObject2=jsonArray1.getJSONObject(0);
                                icon1=jsonObject2.getString("icon");
                                loadimage(imageView6,"http://openweathermap.org/img/w/"+icon1+".png");


                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        RequestQueue requestQueue1= Volley.newRequestQueue(this);
        requestQueue1.add(jsonObjectRequest);

    }
    private void Set_Forcast_days_lat_lon()
    {
        String url="http://api.openweathermap.org/data/2.5/forecast?lat="+lat+"&lon="+lon+"&appid=62f2014ba95c2e7d58e6f529defab9e4&units=imperial";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    JSONArray jsonArray=response.getJSONArray("list");


                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    JSONObject jsonObject1=jsonObject.getJSONObject("main");
                    String temp1=String.valueOf(jsonObject1.getInt("temp_min"));
                    String temp2= String.valueOf(jsonObject1.getInt("temp_max"));
                    max2.setText(temp2);
                    min2.setText(temp1);

                    JSONArray jsonArray1=jsonObject.getJSONArray("weather");
                    JSONObject jsonObject2=jsonArray1.getJSONObject(0);
                    icon1=jsonObject2.getString("icon");
                    loadimage(imageView2,"http://openweathermap.org/img/w/"+icon1+".png");



                    jsonObject=jsonArray.getJSONObject(7);
                    jsonObject1=jsonObject.getJSONObject("main");
                    temp1=String.valueOf(jsonObject1.getInt("temp_min"));
                    temp2= String.valueOf(jsonObject1.getInt("temp_max"));
                    max3.setText(temp2);
                    min3.setText(temp1);


                    jsonArray1=jsonObject.getJSONArray("weather");
                    jsonObject2=jsonArray1.getJSONObject(0);
                    icon1=jsonObject2.getString("icon");
                    loadimage(imageView3,"http://openweathermap.org/img/w/"+icon1+".png");

                    jsonObject=jsonArray.getJSONObject(15);
                    jsonObject1=jsonObject.getJSONObject("main");
                    temp1=String.valueOf(jsonObject1.getInt("temp_min"));
                    temp2= String.valueOf(jsonObject1.getInt("temp_max"));
                    max4.setText(temp2);
                    min4.setText(temp1);

                    jsonArray1=jsonObject.getJSONArray("weather");
                    jsonObject2=jsonArray1.getJSONObject(0);
                    icon1=jsonObject2.getString("icon");
                    loadimage(imageView4,"http://openweathermap.org/img/w/"+icon1+".png");


                    jsonObject=jsonArray.getJSONObject(23);
                    jsonObject1=jsonObject.getJSONObject("main");
                    temp1=String.valueOf(jsonObject1.getInt("temp_min"));
                    temp2= String.valueOf(jsonObject1.getInt("temp_max"));
                    max5.setText(temp2);
                    min5.setText(temp1);

                    jsonArray1=jsonObject.getJSONArray("weather");
                    jsonObject2=jsonArray1.getJSONObject(0);
                    icon1=jsonObject2.getString("icon");
                    loadimage(imageView5,"http://openweathermap.org/img/w/"+icon1+".png");


                    jsonObject=jsonArray.getJSONObject(31);
                    jsonObject1=jsonObject.getJSONObject("main");

                    temp1=String.valueOf(jsonObject1.getInt("temp_min"));
                    temp2= String.valueOf(jsonObject1.getInt("temp_max"));
                    max6.setText(temp2);
                    min6.setText(temp1);

                    jsonArray1=jsonObject.getJSONArray("weather");
                    jsonObject2=jsonArray1.getJSONObject(0);
                    icon1=jsonObject2.getString("icon");
                    loadimage(imageView6,"http://openweathermap.org/img/w/"+icon1+".png");


                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        RequestQueue requestQueue1= Volley.newRequestQueue(this);
        requestQueue1.add(jsonObjectRequest);

    }


}
