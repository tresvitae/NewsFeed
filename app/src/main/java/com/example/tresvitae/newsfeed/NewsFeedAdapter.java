package com.example.tresvitae.newsfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * An {@link NewsFeedAdapter} knows how to create a list item layout for each article
 * in the data source (a list of {@link NewsFeed} objects).
 */

public class NewsFeedAdapter extends ArrayAdapter<NewsFeed> {

    /**
     * Variable to get day, month and year value
     */
    private static final String LOCATION_SEPARATOR = "T";

    public NewsFeedAdapter(Context context, List<NewsFeed> newsfeeds) {
        super(context, 0, newsfeeds);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        final NewsFeed currentNewsFeed = getItem(position);

        TextView sectionNameView = (TextView) listItemView.findViewById(R.id.section_view);
        TextView webTitleView = (TextView) listItemView.findViewById(R.id.title_view);
        TextView webPublicationDateView = (TextView) listItemView.findViewById(R.id.data_time_view);
        TextView authorNameView = (TextView) listItemView.findViewById(R.id.author_name);

        sectionNameView.setText(currentNewsFeed.getSectionName());
        webTitleView.setText(currentNewsFeed.getWebTitle());
        webPublicationDateView.setText(formatTime(currentNewsFeed.getWebPublicationDate()));
        authorNameView.setText(currentNewsFeed.getAuthorName());

        return listItemView;
    }

    /**
     * Input data time from JSON and return the formatted date string.
     */
    private String formatTime(String originalTimeShow) {
        String[] parts = originalTimeShow.split(LOCATION_SEPARATOR);
        String properTimeShow = parts[0];
        return properTimeShow;
    }
}

