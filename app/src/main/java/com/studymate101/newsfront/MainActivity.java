package com.studymate101.newsfront;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.view.menu.ActionMenuItem;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.v7.widget.SearchView;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.studymate101.newsfront.Utils.UrlsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    public static final String URL_DATA = "https://newsapi.org/v2/top-headlines?country=us&apiKey=9c99aff6e6b741abb9647a1b779f1745";

    public static final String source = "https://newsapi.org/v2/sources?apiKey=9c99aff6e6b741abb9647a1b779f1745";

    private TextView tv_username,tv_email;
    private ImageView user_imageView;
    private SharedPreferences sp;
    private LinearLayout prof_section;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    //ArrayAdapter<String> spadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*tv_email = (TextView)findViewById(R.id.tv_email);
        tv_username = (TextView)findViewById(R.id.tv_username);
        user_imageView = (ImageView)findViewById(R.id.user_imageView);*/
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();


        //befrore user sign in
        /*spadapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_expandable_list_item_1,spitems);
        spinner.setAdapter(spadapter);
        prof_section.setVisibility(View.GONE);*/

        listItems = new ArrayList<>();

        loadRecyclerViewData(URL_DATA);




        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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

        SharedPreferences sdp = this.getPreferences(Context.MODE_PRIVATE);
        Boolean CheckLogin = sdp.getBoolean("ISLOGIN",false);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();

        if (CheckLogin == false){
            nav_Menu.findItem(R.id.nav_logout).setVisible(false);
            nav_Menu.findItem(R.id.nav_sigin).setVisible(true);
           /* signIn();
            Toast.makeText(getApplicationContext(),"signIn() is called with CheckLogin == false",Toast.LENGTH_LONG).show();*/
        }
        else if(CheckLogin == true){
            nav_Menu.findItem(R.id.nav_sigin).setVisible(false);
            nav_Menu.findItem(R.id.nav_logout).setVisible(true);
            signIn();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void loadRecyclerViewData(String url){
        String news_url = url;
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

                    for(int i = 0;i<array.length();i++) {
                        JSONObject o = array.getJSONObject(i);
                        ListItem item = new ListItem(o.getString("title"),
                                o.getString("description"),o.getString("urlToImage"),o.getJSONObject("source").getString("name"),o.getString("url"),o.getString("publishedAt"));
                        listItems.add(item);

                    }

                    adapter = new MyAdapter(listItems,getApplicationContext());
                    recyclerView.setAdapter(adapter);

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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

       MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);



       MenuItem searchItm = menu.findItem(R.id.search_view);
       SearchView searchView = (SearchView)searchItm.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String search_url_data;

                String querysearch = s;
                URL newsurls = UrlsUtils.buildUrl(querysearch);
                search_url_data = newsurls.toString();

               // Search Button done

                Intent  intent = new Intent(MainActivity.this,SearchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("search_url",search_url_data);
                intent.putExtras(bundle);
                startActivity(intent);

               //Toast.makeText(getApplicationContext(),search_url_data,Toast.LENGTH_LONG).show();;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the home action
        } else if (id == R.id.nav_readitlater) {

        } else if (id == R.id.nav_source) {


        } else if (id == R.id.nav_setting) {

        }else if (id == R.id.nav_sigin){
            signIn();

        } else if (id == R.id.nav_logout) {
            signOut();

        }else if(id==R.id.nav_share){

        }else if (id == R.id.nav_feed) {

            Intent send = new Intent(Intent.ACTION_SEND);
            send.putExtra(Intent.EXTRA_EMAIL,new String[]{"brandongangte@gmail.com"});
            //send.putExtra(Intent.EXTRA_SUBJECT,)
            send.setType("message/rfc822"); //code for email
            startActivity(Intent.createChooser(send,"Select Email to Send:"));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void signIn(){

        Intent intt = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
         startActivityForResult(intt,REQ_CODE);

     }
    private void signOut(){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

                updateUI(false);
            }
        });

    }

    private void handleResult(GoogleSignInResult result){
       /* SharedPreferences mPreferences = getSharedPreferences("CurrentUser",
                MODE_PRIVATE);
        SharedPreferences.Editor ed = mPreferences.edit();*/


        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            String email = account.getEmail();
            String img_url = account.getPhotoUrl().toString();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View header=navigationView.getHeaderView(0);
            tv_username = (TextView)header.findViewById(R.id.tv_username);
            tv_email = (TextView)header.findViewById(R.id.tv_email);
            user_imageView = (ImageView)header.findViewById(R.id.user_imageView);

            tv_username.setText(name);
            tv_email.setText(email);

            //Glide.with(this).load(img_url).apply(RequestOptions.circleCropTransform()).into(imageView);
            Glide.with(this).load(img_url).asBitmap().centerCrop().into(new BitmapImageViewTarget(user_imageView) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    user_imageView.setImageDrawable(circularBitmapDrawable);
                }
            });


            //Glide.with(this).load(img_url).into(user_imageView);

            //shared preference called here !!!!!!!!!!
            //sharedLoginInfo(result);



          /*  ed.putBoolean("ISLOGIN",true);
            ed.commit();
*/
            updateUI(true);
        }else {

            updateUI(false);
        }

    }

    private void updateUI(Boolean isLogin){
        NavigationView navigationView;
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        sp = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if(isLogin){
            editor.putBoolean("ISLOGIN",true);
            editor.commit();
            nav_Menu.findItem(R.id.nav_sigin).setVisible(false);
            nav_Menu.findItem(R.id.nav_logout).setVisible(true);
        }
        else {
            editor.putBoolean("ISLOGIN",false);
            editor.commit();
           nav_Menu.findItem(R.id.nav_logout).setVisible(false);
           nav_Menu.findItem(R.id.nav_sigin).setVisible(true);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQ_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }

    // Load this for the first login
/*
    public void sharedLoginInfo (GoogleSignInResult reslt){

        GoogleSignInAccount ac = reslt.getSignInAccount();
       sp = getSharedPreferences("loginInfo",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user_name",ac.toString());
        editor.putString("user_email",ac.toString());
        editor.putString("url_image",ac.getPhotoUrl().toString());
        editor.apply();
    }
*/

    // Load if its already login from previous exit
 /*   public void loadAsLogin(){
         sp = getSharedPreferences("loginInfo",Context.MODE_PRIVATE);

        String user = sp.getString("user_name","");
        String email_user = sp.getString("user_email","");
        String img_urlss = sp.getString("url_image","");


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header=navigationView.getHeaderView(0);
        tv_username = (TextView)header.findViewById(R.id.tv_username);
        tv_email = (TextView)header.findViewById(R.id.tv_email);
        user_imageView = (ImageView)header.findViewById(R.id.user_imageView);

        tv_username.setText(user);
        tv_email.setText(email_user);
        Glide.with(this).load(img_urlss).into(user_imageView);
    }*/


}
