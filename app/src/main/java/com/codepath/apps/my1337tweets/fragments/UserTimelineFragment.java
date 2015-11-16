package com.codepath.apps.my1337tweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;

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
public class UserTimelineFragment extends TweetsListFragment {
    private TwitterClient client;
    private SwipeRefreshLayout swipeContainer;

    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screenName", screenName);
        userFragment.setArguments(args);
        return userFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();
        populateTimeline();
    }

    private void populateTimeline() {
        String screenName = getArguments().getString("screenName");
        client.getUserTimeline(screenName, new JsonHttpResponseHandler() {
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

    private void addTweets(List<Tweet> returnedTweets) {
        for (Tweet t : returnedTweets) {
            client.seenTweetId(t.getUid());
            addTweet(t);
        }
    }

    protected void refreshTimeline() {}
    protected void extendTweetList() {}
}
