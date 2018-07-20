package com.example.tresvitae.newsfeed;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsFeedLoader extends AsyncTaskLoader<List<NewsFeed>> {

    private static final String LOG_TAG = NewsFeedLoader.class.getName();

    /** Variable's value is never going to be changed so declared it as final */
    final private String fieldUrl;

    public NewsFeedLoader(Context context, String url) {
        super(context);
        fieldUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsFeed> loadInBackground() {
        if (fieldUrl == null) {
            return null;
        }

        List<NewsFeed> newsfeeds = QueryUtils.fetchNewsFeedData(fieldUrl);
        return newsfeeds;
    }
}
