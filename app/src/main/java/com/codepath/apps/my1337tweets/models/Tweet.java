package com.codepath.apps.my1337tweets.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bschmeckpeper on 11/7/15.
 */
public class Tweet {
    private String body;
    private long uid;
    private User user;
    private Date createdAt;

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public static Tweet fromJSON(JSONObject json) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = json.getString("text");
            tweet.uid = json.getLong("id");
            String dateString = json.getString("created_at");
            // e.g. "Tue Nov 10 03:28:43 +0000 2015"
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
            try {
                tweet.createdAt = formatter.parse(dateString);
            } catch (ParseException e) {
                tweet.createdAt = null;
            }
            tweet.user = User.fromJSON(json.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }

    public static List<Tweet> fromJSONArray(JSONArray json) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            JSONObject obj = null;
            try {
                obj = json.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(obj);
                if (tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return tweets;
    }
}
