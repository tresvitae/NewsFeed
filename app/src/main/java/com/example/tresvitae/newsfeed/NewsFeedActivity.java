package com.example.tresvitae.newsfeed;


import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class NewsFeedActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<NewsFeed>> {

    public static final String LOG_TAG = NewsFeedActivity.class.getName();

    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?";

    private static final int NEWSFEED_LOADER_ID = 1;

    private NewsFeedAdapter fieldAdapter;

    private TextView fieldEmptyTextView;

    private ConnectivityManager connMgr;

    private  NetworkInfo networkInfo;

    /** Displays main view which contains list of articles with section, author's name and date of publish. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        ListView newsfeedListView = (ListView) findViewById(R.id.tech_list_view);

        fieldEmptyTextView = (TextView) findViewById(R.id.empty_view);
        newsfeedListView.setEmptyView(fieldEmptyTextView);

        fieldAdapter = new NewsFeedAdapter(this, new ArrayList<NewsFeed>());

        newsfeedListView.setAdapter(fieldAdapter);

        /** Available to read any clicked(chosen) article from browser. */
        newsfeedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                NewsFeed currentNewsFeed = fieldAdapter.getItem(position);
                Uri newsfeedUri = Uri.parse(currentNewsFeed.getWebUri());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsfeedUri);
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity.
        connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            getLoaderManager().initLoader(NEWSFEED_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            fieldEmptyTextView.setText(R.string.no_internet);
        }
    }

    /**
     *  Customized the API link to give you desired JSON response and
     *  narrowed it down to preferences selected by user in Settings activity like order-by and numbers of articles.
     */
    @Override
    public Loader<List<NewsFeed>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String orderBy = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        String articlesNumber = sharedPreferences.getString(
                getString(R.string.settings_articles_key),
                getString(R.string.settings_articles_default));

        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("q", "Poland");
        uriBuilder.appendQueryParameter("orderBy", orderBy);
        uriBuilder.appendQueryParameter("page-size", articlesNumber);
        uriBuilder.appendQueryParameter("api-key", "2183b31f-6225-46bc-9896-991f7c264008");


        // Example: https://content.guardianapis.com/search?section=technology&show-tags=contributor&q=Poland&api-key=2183b31f-6225-46bc-9896-991f7c264008
        return new NewsFeedLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsFeed>> loader, List<NewsFeed> newsfeeds) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        fieldEmptyTextView.setText(R.string.no_internet);

        fieldAdapter.clear();

        if (newsfeeds != null && !newsfeeds.isEmpty()) {
            fieldAdapter.addAll(newsfeeds);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsFeed>> loader) {
        fieldAdapter.clear();
//        if (!networkInfo.isConnected()) {
//            fieldEmptyTextView.setText(R.string.no_internet);
//        }
    }


    // This method initialize the contents of the Activity's options menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}