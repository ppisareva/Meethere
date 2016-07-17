package com.example.polina.meethere.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.polina.meethere.R;
import com.example.polina.meethere.Utils;
import com.example.polina.meethere.fragments.MyEventsFragment;
import com.example.polina.meethere.model.App;
import com.example.polina.meethere.model.UserProfile;
import com.example.polina.meethere.network.ServerApi;

import org.json.JSONObject;

public class UserProfileActivity extends AppCompatActivity {

    public final int CREATED_BY_USER_EVENTS = 4343432;
    String userId;
    App app;
    ServerApi serverApi;
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        userId = getIntent().getStringExtra(Utils.USER_ID);
        app = (App)getApplication();
        serverApi = app.getServerApi();
        name = (TextView) findViewById(R.id.user_name);
        new LoadUserInfo().execute(userId);

        MyEventsFragment myEventsFragment = MyEventsFragment.newInstance(CREATED_BY_USER_EVENTS, Integer.parseInt(userId));
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_main, myEventsFragment).addToBackStack(null).commit();
    }

    class LoadUserInfo extends AsyncTask<String, Void, JSONObject>{
        @Override
        protected JSONObject doInBackground(String... params) {
            return serverApi.loadUserProfile(params[0]);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            UserProfile userProfile = UserProfile.parseUserProfile(jsonObject);
            name.setText(userProfile.getName());


        }
    }
}
