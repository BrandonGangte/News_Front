package com.studymate101.newsfront;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class NewsActivity extends AppCompatActivity {
   /* Context context;
    private List<ListItem> listItems;

    public NewsActivity(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }*/


     WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Bundle extras = getIntent().getExtras();
        String urlLoad = extras.getString("urlLoad");

       webView = (WebView) findViewById(R.id.webView);
       WebSettings webSettings = webView.getSettings();
       webSettings.setJavaScriptEnabled(false);
       webView.loadUrl(urlLoad);

      //  Toast.makeText(getApplicationContext(),"Welcom to the NewsActivity",Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share_news_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Bundle extras = getIntent().getExtras();
        String news = extras.getString("urlLoad");
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_setting_news) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.putExtra(Intent.EXTRA_TEXT,news);
            share.setType("text/plain");
            startActivity(Intent.createChooser(share,"Share News via"));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

