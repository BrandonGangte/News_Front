package com.studymate101.newsfront.Utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Brandon on 1/21/2018.
 */

public class UrlsUtils {

    final static String NEWS_SEARCH_URL =
            "https://newsapi.org/v2/everything";

    final static String PARAM_QUERY = "q";

    /*
     * The sort field. One of stars, forks, or updated.
     * Default: results are sorted by best match if no field is specified.
     */
    final static String URL_API ="apiKey";
    final static String KEY = "9c99aff6e6b741abb9647a1b779f1745";


    /**
     * Builds the URL used to query Github.
     *
     * @param searchurl The keyword that will be queried for.
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrl(String searchurl) {
        // COMPLETED (1) Fill in this method to build the proper Github query URL
        Uri builtUri = Uri.parse(NEWS_SEARCH_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, searchurl)
                .appendQueryParameter(URL_API,KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

}
