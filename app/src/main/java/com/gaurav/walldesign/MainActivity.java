package com.gaurav.walldesign;

import android.app.Activity;
import android.app.Application;
import android.content.ContextWrapper;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public ArrayList<String> photo_id = new ArrayList<String>();
    public ArrayList<String> photo_author = new ArrayList<String>();

    ImageView imageView;

    //Declare the Adapter, RecyclerView and our custom ArrayList
    RecyclerView recyclerView;
    CustomAdapter adapter;
    private ArrayList<CustomPojo> listContentArr= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* LinearLayout constraintLayout = (LinearLayout) findViewById(R.id.linearlayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();


        Picasso.with(this)
                .load("https://unsplash.com/photos/9WpQrQKbqFM/download")
                .into(imageView);
        */

        request();

    }

    public void request() {

        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "https://api.unsplash.com/photos/?client_id=05f9c7434351526e2c422c84b089a47e7a464b56ff34013db792b784b82a7ca3&per_page=50";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i=0; i <jsonArray.length(); i++) {

                        JSONObject post = jsonArray.getJSONObject(i);

                        photo_id.add(post.getString("id"));

                        JSONObject user = post.getJSONObject("user");

                        photo_author.add(user.getString("name"));

                    }

                    recyclerView = (RecyclerView) findViewById(R.id.recycleView);
                    //As explained in the tutorial, LineatLayoutManager tells the RecyclerView that the view
                    //must be arranged in linear fashion
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    adapter = new CustomAdapter(MainActivity.this);
                    //Method call for populating the view

                    for (int iter = 1; iter <= jsonArray.length(); iter++) {
                        //Creating POJO class object
                        CustomPojo pojoObject = new CustomPojo();
                        //Values are binded using set method of the POJO class
                        pojoObject.setId(photo_id.get(iter-1));
                        //After setting the values, we add all the Objects to the array
                        //Hence, listConentArr is a collection of Array of POJO objects
                        listContentArr.add(pojoObject);
                    }
                    //We set the array to the adapter
                    adapter.setListContent(listContentArr);
                    //We in turn set the adapter to the RecyclerView
                    recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                    RecyclerViewMargin decoration = new RecyclerViewMargin(5, 2);
                    recyclerView.addItemDecoration(decoration);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error","error"+error.toString());
                    }
                }

        );

        queue.add(postRequest);

    }

}
