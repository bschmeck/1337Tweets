package com.codepath.apps.my1337tweets;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.my1337tweets.fragments.UserTimelineFragment;
import com.codepath.apps.my1337tweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user = (User) getIntent().getParcelableExtra("user");
        String screenName = null;
        if (user == null) {
            TwitterClient client = TwitterApplication.getRestClient();

            client.getUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJSON(response);
                    populate(user);
                }
            });
        } else {
            populate(user);
            screenName = user.getScreenName();
        }
        if (savedInstanceState == null) {
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void populate(User user) {
        getSupportActionBar().setTitle("@" + user.getScreenName());

        TextView tvName = (TextView) findViewById(R.id.tvName);
        tvName.setText(user.getName());

        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        tvTagline.setText(user.getTagline());

        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        tvFollowers.setText(user.getFollowersCount() + " Followers");

        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        tvFollowing.setText(user.getFriendsCount() + " Following");

        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
    }
}
