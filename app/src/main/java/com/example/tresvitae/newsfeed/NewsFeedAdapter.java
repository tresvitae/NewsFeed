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

        class KeepView {
            private TextView sectionNameView;
            private TextView webTitleView;
            private TextView webPublicationDateView;
            private TextView authorNameView;
        }

        KeepView keepView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

            keepView = new KeepView();

            keepView.sectionNameView = listItemView.findViewById(R.id.section_view);
            keepView.webTitleView = listItemView.findViewById(R.id.title_view);
            keepView.webPublicationDateView = listItemView.findViewById(R.id.data_time_view);
            keepView.authorNameView = listItemView.findViewById(R.id.author_name);
        } else {
            keepView = (KeepView) listItemView.getTag();
        }


        NewsFeed currentNewsFeed = getItem(position);

        keepView.sectionNameView.setText(currentNewsFeed.getSectionName());
        keepView.webTitleView.setText(currentNewsFeed.getWebTitle());
        keepView.webPublicationDateView.setText(formatTime(currentNewsFeed.getWebPublicationDate()));
        keepView.authorNameView.setText(currentNewsFeed.getAuthorName());

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

