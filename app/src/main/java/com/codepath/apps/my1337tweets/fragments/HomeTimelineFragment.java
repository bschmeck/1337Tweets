package com.codepath.apps.my1337tweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.my1337tweets.TwitterApplication;
import com.codepath.apps.my1337tweets.TwitterClient;
import com.codepath.apps.my1337tweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by bschmeckpeper on 11/15/15.
 */
public class HomeTimelineFragment extends TweetsListFragment {
    private TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();
        populateTimeline();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);





        return view;
    }

    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                addTweets(Tweet.fromJSONArray(json));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    protected void extendTweetList() { populateTimeline(); }

    protected void refreshTimeline() {
        client.clearSeenTweets();
        clearTweets();
        populateTimeline();
    }

    private void addTweets(List<Tweet> returnedTweets) {
        for (Tweet t : returnedTweets) {
            client.seenTweetId(t.getUid());
            addTweet(t);
        }
        // aTweets.notifyDataSetChanged();
    }
}
