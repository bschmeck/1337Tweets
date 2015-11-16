package com.codepath.apps.my1337tweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.my1337tweets.R;
import com.codepath.apps.my1337tweets.TweetsArrayAdapter;
import com.codepath.apps.my1337tweets.models.EndlessScrollListener;
import com.codepath.apps.my1337tweets.models.Tweet;

import java.util.ArrayList;

/**
 * Created by bschmeckpeper on 11/15/15.
 */
public abstract class TweetsListFragment extends Fragment {
    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
    protected ListView lvTweets;
    private SwipeRefreshLayout swipeContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        lvTweets = (ListView) view.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                extendTweetList();
                return true;
            }
        });

        swipeContainer = (SwipeRefreshLayout)view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTimeline();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
    }

    public void clearTweets() {
        aTweets.clear();
    }

    public void addTweet(Tweet tweet) {
        aTweets.add(tweet);
    }

    protected abstract void refreshTimeline();
    protected abstract void extendTweetList();
}
