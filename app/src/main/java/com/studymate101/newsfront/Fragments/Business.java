package com.studymate101.newsfront.Fragments;


import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.studymate101.newsfront.ListItem;
import com.studymate101.newsfront.MainActivity;
import com.studymate101.newsfront.MyAdapter;
import com.studymate101.newsfront.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Business extends Fragment {
    //public String[] news_source = new String[]{"breitbart-news","cnbc"};
    public static final String URL_DATA = "https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=9c99aff6e6b741abb9647a1b779f1745";
    public static final String URL_DATA2 = "https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=9c99aff6e6b741abb9647a1b779f1745";
    //   public static final String source = "https://newsapi.org/v2/sources?apiKey=9c99aff6e6b741abb9647a1b779f1745";


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    public Business() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

       // bus_frame = (FrameLayout)view.findViewById(R.id.bus_frame_layout);
        // Inflate the layout for this fragment

        listItems = new ArrayList<>();

/*
        ((MainActivity) getActivity())
                .setActionBarTitle("Business");*/
       /* sp = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);

        checktitle = sp.getString("TITLE","Business");
        SharedPreferences.Editor editor = sp.edit();
        if(checktitle == "Headlines") {
            ((MainActivity) getActivity())
                    .setActionBarTitle("Headlines");
            editor.putString("TITLE", "Headlines");
            editor.commit();
        }
        else if(checktitle=="Techonology"){
            ((MainActivity) getActivity())
                    .setActionBarTitle("Techonology");
            editor.putString("TITLE","Techonology");
            editor.commit();
        }*/
     loadRecyclerViewData();


        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/


                LinearLayoutManager layoutM = (LinearLayoutManager) recyclerView.getLayoutManager();
                layoutM.smoothScrollToPosition(recyclerView, null, 0);

            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0)
                    fab.show();
                else if (dy <= 0)
                    fab.hide();
            }
        });

        return view;
    }

         /*   ((MainActivity) getActivity())
                    .setActionBarTitle("Business");*/


    private void loadRecyclerViewData(){
        String[] news_url = new String[]{URL_DATA,URL_DATA2};
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        final Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content),"No Internet Connection",Snackbar.LENGTH_LONG);
        snackbar.setAction("Retry",new MyReconnector());
        snackbar.setActionTextColor(getResources().getColor(R.color.white));



        for(int j = 0 ; j<2;j++){

            StringRequest stringRequest = new StringRequest(Request.Method.GET, news_url[j], new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {

                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray array = jsonObject.getJSONArray("articles");

                        for(int i = 0;i<array.length();i++) {
                            JSONObject o = array.getJSONObject(i);
                            ListItem item = new ListItem(o.getString("title"),
                                    o.getString("description"),o.getString("urlToImage"),o.getJSONObject("source").getString("name"),o.getString("url"),o.getString("publishedAt"));
                            listItems.add(item);

                        }

                        adapter = new MyAdapter(listItems,getContext());
                        recyclerView.setAdapter(adapter);
                        snackbar.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                   // Snackbar snackbar = Snackbar.make(bus_frame,"No Internet Connection ",Snackbar.LENGTH_INDEFINITE);
                    //snackbar.show();
                    //Toast.makeText(getContext(), error.getMessage(),Toast.LENGTH_LONG).show();
                    snackbar.show();
                    progressDialog.dismiss();
                }
            });


           // snackbar.dismiss();
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        }
    }
    public class MyReconnector implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            loadRecyclerViewData();

            //Toast.makeText(getContext(),"Its Working",Toast.LENGTH_LONG).show();
        }
    }

}
