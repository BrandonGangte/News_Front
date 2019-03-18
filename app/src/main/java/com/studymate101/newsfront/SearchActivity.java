package com.studymate101.newsfront;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/// NEED TO CHANGE THE CODE

public class SearchActivity extends AppCompatActivity {

    //public static final String URL_DATAw = "https://newsapi.org/v2/everything?q=Loganpaul&apiKey=9c99aff6e6b741abb9647a1b779f1745";

    private RecyclerView recycler2;
    private RecyclerView.Adapter adapterr;
    private List<ListItem> listItemss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recycler2 = (RecyclerView) findViewById(R.id.recycler2);
        recycler2.setHasFixedSize(true);
        recycler2.setLayoutManager(new LinearLayoutManager(this));


        listItemss = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        String urlLoaded = extras.getString("search_url");
        loadRecyclerViewDataSearch(urlLoaded);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayoutManager layoutM = (LinearLayoutManager) recycler2.getLayoutManager();
                layoutM.smoothScrollToPosition(recycler2, null, 0);
            }
        });

        recycler2.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0)
                    fab.show();
                else if (dy <= 0)
                    fab.hide();
            }
        });
    }


    private void loadRecyclerViewDataSearch(String urls){
         String news_url = urls;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, news_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray array = jsonObject.getJSONArray("articles");
                    if(array.length()>20){
                        for(int i = 0;i<20;i++) {
                            JSONObject os = array.getJSONObject(i);
                            ListItem item = new ListItem(os.getString("title"),
                                    os.getString("description"),os.getString("urlToImage"),os.getJSONObject("source").getString("name"),os.getString("url"),os.getString("publishedAt"));
                            listItemss.add(item);

                        }

                    }else {
                        for(int i = 0;i<array.length();i++) {
                            JSONObject os = array.getJSONObject(i);
                            ListItem item = new ListItem(os.getString("title"),
                                    os.getString("description"),os.getString("urlToImage"),os.getJSONObject("source").getString("name"),os.getString("url"),os.getString("publishedAt"));
                            listItemss.add(item);
                        }
                    }


                    adapterr = new SearchAdapter(listItemss,getApplicationContext());
                    recycler2.setAdapter(adapterr);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_setting) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
