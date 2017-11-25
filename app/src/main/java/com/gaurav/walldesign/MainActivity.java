package com.gaurav.walldesign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Application;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private boolean mSearchViewAdded = false;
    private SearchView mSearchView;
    private WindowManager mWindowManager;
    private android.support.v7.widget.Toolbar mToolbar;
    private MenuItem searchItem;
    private boolean searchActive = false;


    public ArrayList<String> photo_url = new ArrayList<String>();

    public ArrayList<String> photo_author = new ArrayList<String>();

    public ArrayList<String> photo_like = new ArrayList<String>();

    private ArrayList<CustomPojo> listContentArr= new ArrayList<>();

    RecyclerView recyclerView;

    CustomAdapter adapter;

    MaterialSearchView searchView;

    String searchterm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(0xFFFFFFFF);

        toolbar.setTitle("Wallpapers");

        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                photo_author.clear();

                photo_url.clear();

                listContentArr.clear();

                photo_like.clear();

                searchterm = query;

                request(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

        request("none");
    }

    public void request(final String string) {

        RequestQueue queue = Volley.newRequestQueue(this);

        final String url;

        if (string.equals("none")) {

            url = "https://api.unsplash.com/photos/?client_id=05f9c7434351526e2c422c84b089a47e7a464b56ff34013db792b784b82a7ca3&per_page=50";

        } else {

            url = "https://api.unsplash.com/search/photos/?client_id=05f9c7434351526e2c422c84b089a47e7a464b56ff34013db792b784b82a7ca3&per_page=50&query="+searchterm;

        }

        StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONArray jsonArray;

                try {

                    if (string == "none") {

                        jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject post = jsonArray.getJSONObject(i);

                            photo_like.add(post.getString("likes"));

                            JSONObject user = post.getJSONObject("user");

                            photo_author.add(user.getString("name"));

                            JSONObject urls = post.getJSONObject("urls");

                            photo_url.add(urls.getString("raw"));

                        }

                    } else {

                        JSONObject jsonObject = new JSONObject(response);

                        jsonArray =  jsonObject.getJSONArray("results");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject post = jsonArray.getJSONObject(i);

                            photo_like.add(post.getString("likes"));

                            JSONObject urls = post.getJSONObject("urls");

                            photo_url.add(urls.getString("raw"));

                            JSONObject user = post.getJSONObject("user");

                            photo_author.add(user.getString("name"));

                        }

                    }

                    recyclerView = (RecyclerView) findViewById(R.id.recycleView);

                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                    adapter = new CustomAdapter(MainActivity.this);

                    for (int iter = 1; iter <= jsonArray.length(); iter++) {

                        CustomPojo pojoObject = new CustomPojo();

                        pojoObject.setId(photo_url.get(iter-1));

                        listContentArr.add(pojoObject);
                    }

                    adapter.setListContent(listContentArr);

                    recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));

                    recyclerView.setAdapter(adapter);

                    recyclerView.addOnItemTouchListener(

                            new RecyclerItemClickListener(MainActivity.this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {

                                @SuppressLint("NewApi")
                                @Override public void onItemClick(View view, int position) {

                                    ImageView imageView = (ImageView) view.findViewById(R.id.imageview);

                                    Intent intent = new Intent(MainActivity.this, Wallpaper.class);

                                    ActivityOptionsCompat options = ActivityOptionsCompat.
                                            makeSceneTransitionAnimation((Activity) MainActivity.this,
                                                    imageView,
                                                    ViewCompat.getTransitionName(imageView));

                                    intent.putExtra("id",photo_url.get(position));

                                    intent.putExtra("name",photo_author.get(position));

                                    intent.putExtra("likes",photo_like.get(position));

                                   startActivity(intent, options.toBundle());

                                }

                                @Override public void onLongItemClick(View view, int position) {

                                }
                            })
                    );

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


    public void print(String t){

        Toast.makeText(this,t,Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);

        searchView.setMenuItem(item);

        return true;
    }


}
