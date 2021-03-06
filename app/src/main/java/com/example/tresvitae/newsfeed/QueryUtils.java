package com.example.tresvitae.newsfeed;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private static final int READ_TIMEOUT = 10000;  /* milliseconds */

    private static final int CONNECT_TIMEOUT = 15000; /* milliseconds */

    private static final int RESPONSE_CODE = 200;



    private QueryUtils() {
    }

    /**
     * Query the guardian website dataset and return a list of {@link NewsFeed} objects.
     */
    public static List<NewsFeed> fetchNewsFeedData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<NewsFeed> newsfeeds = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Earthquake}s
        return newsfeeds;
    }

    /**
     * Returns new URL object rom the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == RESPONSE_CODE) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /** Import data from Guardian JSON website to find and fetch proper data. */
    private static List<NewsFeed> extractFeatureFromJson(String newsfeedJSON) {
        if (TextUtils.isEmpty(newsfeedJSON)) {
            return null;
        }

        List<NewsFeed> newsfeeds = new ArrayList<>();
        try {
            String fullName;

            JSONObject jsonRoot = new JSONObject(newsfeedJSON);
            JSONObject responseObjects = jsonRoot.optJSONObject("response");

            JSONArray newsfeedArray = responseObjects.optJSONArray("results");
            for (int i = 0; i < newsfeedArray.length(); i++) {
                JSONObject currentNewsFeed = newsfeedArray.optJSONObject(i);

                String sectionName = currentNewsFeed.optString("sectionName");
                String webTitle = currentNewsFeed.optString("webTitle");
                String webPublicationDate = currentNewsFeed.optString("webPublicationDate");
                String webUrl = currentNewsFeed.optString("webUrl");

                JSONArray tags = currentNewsFeed.getJSONArray("tags");

                if (!tags.isNull(0)) {
                    JSONObject obj = tags.optJSONObject(0);
                    String authorName = obj.optString("firstName");
                    String authorLastName = obj.optString("lastName");

                    StringBuilder builder = new StringBuilder();
                    builder.append(authorName).append(" ").append(authorLastName);
                    fullName = builder.toString().toUpperCase();
                } else {
                    fullName = "Unknown author";
                }

                NewsFeed newsfeed = new NewsFeed(sectionName, webTitle, webPublicationDate, webUrl, fullName);

                newsfeeds.add(newsfeed);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return newsfeeds;
    }

}
