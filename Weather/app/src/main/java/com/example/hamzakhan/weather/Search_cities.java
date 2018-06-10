package com.example.hamzakhan.weather;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Search_cities extends AppCompatActivity {

    ArrayList<String> List=new ArrayList<>();
    ListView city_list;
    EditText search;
    ArrayAdapter<String> arrayAdapter;
    ImageButton button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_search_cities);
            city_list=(ListView)findViewById(R.id.listview);
            search=(EditText)findViewById(R.id.editText);
            button=(ImageButton)findViewById(R.id.button);
            Get_json();
            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                   if(charSequence.equals(""))
                    {
                        Get_json();
                    }
                    else
                    {
                        (Search_cities.this).arrayAdapter.getFilter().filter(charSequence);
                            arrayAdapter.notifyDataSetChanged();

                    }


                }

                @Override
                public void afterTextChanged(final Editable editable) {

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Bundle basket4 = new Bundle();
                            basket4.putString("message", editable.toString());
                            Intent a4 = new Intent(Search_cities.this, MainActivity.class);
                            a4.putExtras(basket4);
                            startActivity(a4);

                        }
                    });




                }
            });




    }
    public void  Get_json() {

try
{

    InputStream stream=getAssets().open("city.list.min.json");
    JsonReader reader=new JsonReader(new InputStreamReader(stream,"UTF-8"));
    reader.beginArray();
    Gson gson=new GsonBuilder().create();

    while (reader.hasNext())
    {
        Cities cities=gson.fromJson(reader,Cities.class);
        List.add(cities.getName());

    }
    reader.close();


    arrayAdapter = new ArrayAdapter<String>(city_list.getContext(), android.R.layout.simple_list_item_1, List);
    city_list.setAdapter(arrayAdapter);


    city_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView tv = (TextView) view;
            String data = (String) tv.getText();
            Bundle basket4 = new Bundle();
            basket4.putString("message", data);
            Intent a4 = new Intent(Search_cities.this, MainActivity.class);
            a4.putExtras(basket4);
            startActivity(a4);
        }
    });

        }

        catch(Exception e)
        {
            e.printStackTrace();
        }


        }
        //Load json data from city.json using gson through stream model mixed object model beacuse of large json file


}



