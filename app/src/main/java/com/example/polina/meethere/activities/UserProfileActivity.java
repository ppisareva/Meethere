package com.example.polina.meethere.activities;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.polina.meethere.MyEventsAdapter;
import com.example.polina.meethere.R;
import com.example.polina.meethere.Utils;
import com.example.polina.meethere.fragments.MyEventsFragment;
import com.example.polina.meethere.model.App;
import com.example.polina.meethere.model.UserProfile;
import com.example.polina.meethere.network.ServerApi;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

public class UserProfileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public final int CREATED_BY_USER_EVENTS = 46776;
    String userId;
    App app;
    ServerApi serverApi;
    TextView name;
    TextView location;
    TextView followings;
    TextView followers;
    SimpleDraweeView image;
    UserProfile userProfile;
    RecyclerView list;
    MyEventsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        list = (RecyclerView) findViewById(R.id.list_of_created_events);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new MyEventsAdapter(this);
        list.setAdapter(adapter);
        userId = getIntent().getStringExtra(Utils.USER_ID);
        app = (App)getApplication();
        serverApi = app.getServerApi();
        name = (TextView) findViewById(R.id.user_name);
        location = (TextView) findViewById(R.id.location);
        image = (SimpleDraweeView) findViewById(R.id.profile_image);
        followers = (TextView) findViewById(R.id.followers);
        followings = (TextView) findViewById(R.id.followings);
        new LoadUserInfo().execute(userId);

        Bundle bundle = new Bundle();
        bundle.putString(Utils.USER_ID, userId);
        getSupportLoaderManager().initLoader(CREATED_BY_USER_EVENTS, bundle, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String arr[] = new String[]{com.example.polina.meethere.model.Event.ID, com.example.polina.meethere.model.Event.NAME,
                com.example.polina.meethere.model.Event.DESCRIPTION, com.example.polina.meethere.model.Event.START,
                com.example.polina.meethere.model.Event.END, com.example.polina.meethere.model.Event.TAGS,
                com.example.polina.meethere.model.Event.PLACE, com.example.polina.meethere.model.Event.ADDRESS,
                com.example.polina.meethere.model.Event.AGE_MAX, com.example.polina.meethere.model.Event.AGE_MIN,
                com.example.polina.meethere.model.Event.BUDGET_MAX, com.example.polina.meethere.model.Event.BUDGET_MIN};
       String userId = args.getString(Utils.USER_ID);

           Uri uri =  Uri.parse("content://com.example.polina.meethere.data.data/userevents/"+(userId));

        return new CursorLoader(this, uri, arr, null, null, null);


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        System.err.println("onLoaderReset");
    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        getActivity().getSupportLoaderManager().restartLoader(tag, null, this);
//
//    }

    class LoadUserInfo extends AsyncTask<String, Void, JSONObject>{
        @Override
        protected JSONObject doInBackground(String... params) {
            return serverApi.loadUserProfile(params[0]);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            userProfile = UserProfile.parseUserProfile(jsonObject);
            name.setText(userProfile.getName());
            location.setText(userProfile.getLocation());
            image.setImageURI(Uri.parse(userProfile.getMiniProfileUrl()));
            followers.setText(userProfile.getFollowers()+"");
            followings.setText(userProfile.getFollowings()+"");




        }
    }
}
