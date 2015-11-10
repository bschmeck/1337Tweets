package com.codepath.apps.my1337tweets;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.my1337tweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by bschmeckpeper on 11/8/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {
    private class ViewHolder {
        ImageView ivProfileImage;
        TextView tvUsername;
        TextView tvBody;
        TextView tvTimestamp;
    }

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
            viewHolder.tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.tvTimestamp = (TextView) convertView.findViewById(R.id.tvTimestamp);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvUsername.setText(tweet.getUser().getName());
        viewHolder.tvBody.setText(Html.fromHtml(tweet.getBody()));
        viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);
        viewHolder.tvTimestamp.setText(relativeTime(tweet.getCreatedAt()));
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(viewHolder.ivProfileImage);

        return convertView;
    }

    private String relativeTime(Date other) {
        long distance = Math.abs(other.getTime() - (new Date()).getTime());

        if (distance < DateUtils.MINUTE_IN_MILLIS) {
            return "just now";
        } else if (distance > DateUtils.WEEK_IN_MILLIS) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return formatter.format(other);
        }

        long threshold;
        String timeUnit;

        if (distance < DateUtils.HOUR_IN_MILLIS) {
            threshold = DateUtils.MINUTE_IN_MILLIS;
            timeUnit = "minute";
        } else if (distance < DateUtils.DAY_IN_MILLIS) {
            threshold = DateUtils.HOUR_IN_MILLIS;
            timeUnit = "hour";
        } else {
            threshold = DateUtils.DAY_IN_MILLIS;
            timeUnit = "day";
        }

        long numeral = distance / threshold;
        String pluralSuffix = "";
        if (numeral > 1) {
            pluralSuffix = "s";
        }

        return String.format("%d %s%s ago", numeral, timeUnit, pluralSuffix);
    }
}
