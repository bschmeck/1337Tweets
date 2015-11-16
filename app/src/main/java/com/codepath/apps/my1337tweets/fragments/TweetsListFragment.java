package com.codepath.apps.my1337tweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.my1337tweets.R;
import com.codepath.apps.my1337tweets.TweetsArrayAdapter;
import com.codepath.apps.my1337tweets.models.Tweet;

import java.util.ArrayList;

/**
 * Created by bschmeckpeper on 11/15/15.
 */
public class TweetsListFragment extends Fragment {
    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
    protected ListView lvTweets;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        lvTweets = (ListView) view.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);

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
}
