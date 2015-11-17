package com.codepath.apps.my1337tweets;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import java.util.HashMap;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "VbPEmG8XL7lQu0glZPzA8NIDf";       // Change this
	public static final String REST_CONSUMER_SECRET = "Vg45uyyADGFL0lf7pLWVcblxYTpt7RhlNdDT9loIRIHaHsKbwQ"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://my1337tweets"; // Change this (here and in manifest)

    public static final int TWEET_COUNT = 25;

    private HashMap<String, Long> oldestSeenTweetIds;

    public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
        oldestSeenTweetIds = new HashMap<>();
        oldestSeenTweetIds.put("home_timeline", Long.MAX_VALUE);
        oldestSeenTweetIds.put("mentions_timeline", Long.MAX_VALUE);
        oldestSeenTweetIds.put("user_timeline", Long.MAX_VALUE);
	}

	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	public void getInterestingnessList(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("format", "json");
		client.get(apiUrl, params, handler);
	}

	public void getHomeTimeline(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", TWEET_COUNT);
		params.put("since_id", 1);
        if (oldestHomeTimelineTweetIdIsSet()) {
            params.put("max_id", getOldestHomeTimelineTweetId() - 1);
        }
        getClient().get(apiUrl, params, handler);
	}

    public long getOldestHomeTimelineTweetId() { return oldestSeenTweetIds.get("home_timeline"); }
    public boolean oldestHomeTimelineTweetIdIsSet() { return getOldestHomeTimelineTweetId()  != Long.MAX_VALUE; }
    public long getOldestMentionsTweetId() { return oldestSeenTweetIds.get("mentions_timeline"); }
    public boolean oldestMentionsTweetIdIsSet() { return getOldestMentionsTweetId()  != Long.MAX_VALUE; }
    public long getOldestUserTimelineTweetId() { return oldestSeenTweetIds.get("user_timeline"); }
    public boolean oldestUserTimelineTweetIdIsSet() { return getOldestUserTimelineTweetId()  != Long.MAX_VALUE; }

    public void sendTweet(String tweetBody, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", tweetBody);
        getClient().post(apiUrl, params, handler);
    }

    public void seenHomeTimelineTweetId(long tweetId) {
        if (tweetId < getOldestHomeTimelineTweetId()) {
            oldestSeenTweetIds.put("home_timeline", tweetId);
        }
    }
    public void seenUserTimelineTweetId(long tweetId) {
        if (tweetId < getOldestUserTimelineTweetId()) {
            oldestSeenTweetIds.put("user_timeline", tweetId);
        }
    }
    public void seenMentionsTweetId(long tweetId) {
        if (tweetId < getOldestMentionsTweetId()) {
            oldestSeenTweetIds.put("mentions_timeline", tweetId);
        }
    }

    public void clearSeenHomeTimelineTweets() {
        oldestSeenTweetIds.put("home_timeline", Long.MAX_VALUE);
    }
    public void clearSeenUserTimelineTweets() {
        oldestSeenTweetIds.put("user_timeline", Long.MAX_VALUE);
    }
    public void clearSeenMentionsTweets() {
        oldestSeenTweetIds.put("mentions_timeline", Long.MAX_VALUE);
    }

    public void getMentionsTimeline(JsonHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", TWEET_COUNT);
        if (oldestMentionsTweetIdIsSet()) {
            params.put("max_id", getOldestMentionsTweetId() - 1);
        }
        getClient().get(apiUrl, params, handler);
    }

    public void getUserTimeline(String screenName, JsonHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", TWEET_COUNT);
        params.put("screen_name", screenName);
        if (oldestUserTimelineTweetIdIsSet()) {
            params.put("max_id", getOldestUserTimelineTweetId() - 1);
        }
        getClient().get(apiUrl, params, handler);
    }

    public void getUserInfo(JsonHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        getClient().get(apiUrl, null, handler);

    }

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}