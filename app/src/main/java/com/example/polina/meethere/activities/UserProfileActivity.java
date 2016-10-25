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
import android.view.View;
import android.widget.TextView;

import com.example.polina.meethere.MyEventsAdapter;
import com.example.polina.meethere.R;
import com.example.polina.meethere.RecyclerViewPositionHelper;
import com.example.polina.meethere.Utils;
import com.example.polina.meethere.fragments.MyEventsFragment;
import com.example.polina.meethere.model.App;
import com.example.polina.meethere.model.Event;
import com.example.polina.meethere.model.UserProfile;
import com.example.polina.meethere.network.ServerApi;
import com.facebook.drawee.generic.RoundingParams;
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
    Boolean follow;
    TextView followView;

    int offset = 0;
    private int STEP =10;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        list = (RecyclerView) findViewById(R.id.list_of_created_events);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new MyEventsAdapter(this);
        list.setAdapter(adapter);
        list.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerViewPositionHelper mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
                int visibleItemCount = recyclerView.getChildCount();
                int totalItemCount = mRecyclerViewHelper.getItemCount();
                int firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
                System.err.println("first visible id" + firstVisibleItem + "visibleItemCount " + visibleItemCount + "totalItemCount" + totalItemCount);
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 1) {
                    if(isFlag()) {
                        setFlag(false);
                        Bundle arg = new Bundle();
                        offset+=STEP;
                        arg.putInt(Utils.OFFSET, offset);
                        arg.putString(Utils.USER_ID, userId);
                        getSupportLoaderManager().restartLoader(CREATED_BY_USER_EVENTS, arg, UserProfileActivity.this);



                    }
                }


            }
        });
        userId = getIntent().getStringExtra(Utils.USER_ID);
        app = (App)getApplication();
        serverApi = app.getServerApi();
        name = (TextView) findViewById(R.id.user_name);
        location = (TextView) findViewById(R.id.location);
        image = (SimpleDraweeView) findViewById(R.id.profile_image);
        followers = (TextView) findViewById(R.id.followers);
        followings = (TextView) findViewById(R.id.followings);
        followView = (TextView) findViewById(R.id.follow);
        new LoadUserInfo().execute(userId);
        Bundle bundle = new Bundle();
        bundle.putString(Utils.USER_ID, userId);
        bundle.putInt(Utils.OFFSET, offset);
        getSupportLoaderManager().initLoader(CREATED_BY_USER_EVENTS, bundle, this);

    }

    public void onFollow(View v){
        new Follow().execute(follow);


    }

    public void onFollowers(View v){

    }
    public void onFollowings (View v){

    }

    private class Follow extends AsyncTask<Boolean, Void, Void> {
        protected Void doInBackground(Boolean... args) {
            Boolean follow = args[0];
            JSONObject jsonObject = new JSONObject();
            if (follow) {
                jsonObject = serverApi.unfollow(userId);
            } else {
                jsonObject = serverApi.follow(userId);
            }
            System.out.println(jsonObject);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(follow){
                follow= false;
                followView.setText(R.string.follow);
                followView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_white_18dp, 0, 0, 0);
            } else {
                follow = true;
                followView.setText(R.string.unfollow);
                followView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_white_18dp, 0, 0, 0);
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String arr[] =  new String[]{Event.ID, Event.NAME,
                Event.DESCRIPTION, Event.START,
                 Event.TAGS,
                Event.JOINED, Event.ADDRESS, Event.BUDGET_MIN, Event.LAT, Event.LNG, Event.ATTENDANCES};
       String userId = args.getString(Utils.USER_ID);
        String o = ""+args.getInt(Utils.OFFSET, 0);

           Uri uri =  Uri.parse(String.format("content://com.example.polina.meethere.data.data/userevents/?offset=%s&user_id=%s",  o, userId));

        return new CursorLoader(this, uri, arr, null, null, null);


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(adapter.getItemCount() < data.getCount()) {
            setFlag(true);
        }        adapter.swapCursor(data);
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
            RoundingParams roundingParams = RoundingParams.asCircle();
            image.getHierarchy().setRoundingParams(roundingParams);
            followers.setText(userProfile.getFollowers()+"");
            followings.setText(userProfile.getFollowings()+"");
            follow = userProfile.isFollow();
            if(follow){
                followView.setText(R.string.unfollow);
                followView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_white_18dp, 0, 0, 0);
            }

        }
    }
}
