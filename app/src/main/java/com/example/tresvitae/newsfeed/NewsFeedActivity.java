package com.example.tresvitae.newsfeed;


import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class NewsFeedActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<NewsFeed>> {

    public static final String LOG_TAG = NewsFeedActivity.class.getName();

    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?section=technology&show-tags=contributor&q=Poland&api-key=2183b31f-6225-46bc-9896-991f7c264008";

    private static final int NEWSFEED_LOADER_ID = 1;

    private NewsFeedAdapter fieldAdapter;

    private TextView fieldEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        ListView newsfeedListView = (ListView) findViewById(R.id.tech_list_view);

        fieldEmptyTextView = (TextView) findViewById(R.id.empty_view);
        newsfeedListView.setEmptyView(fieldEmptyTextView);

        fieldAdapter = new NewsFeedAdapter(this, new ArrayList<NewsFeed>());

        newsfeedListView.setAdapter(fieldAdapter);

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
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(NEWSFEED_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            fieldEmptyTextView.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<NewsFeed>> onCreateLoader(int id, Bundle args) {
        return new NewsFeedLoader(this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsFeed>> loader, List<NewsFeed> newsfeeds) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        fieldEmptyTextView.setText(R.string.no_newfeeds);

        fieldAdapter.clear();

        if (newsfeeds != null && !newsfeeds.isEmpty()) {
            fieldAdapter.addAll(newsfeeds);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsFeed>> loader) {
        fieldAdapter.clear();
    }
}
